package i2f.extension.flink.test.demo1;

import i2f.extension.flink.test.data.EventData;
import i2f.extension.flink.test.sink.PrintSink;
import i2f.extension.flink.test.source.RandomSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.*;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.sql.Timestamp;
import java.time.Duration;

/**
 * @author ltb
 * @date 2022/11/14 10:11
 * @desc
 */
@Slf4j
public class StreamingFlinkMain {
    public static void main(String[] args) throws Exception {
        log.info("flink 初始化任务");
        Configuration conf = new Configuration();
        conf.setInteger("rest.port", 9999);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(conf);
//        env.setParallelism(1);
//
//        // 创建数据源
//        SingleOutputStreamOperator<String> source = env.socketTextStream("localhost", 9999, "\n")
//                .uid("source-socket");
//
//        // 打印数据源
//        source.print("src");
//
//        // 映射带上时间戳
//        SingleOutputStreamOperator<EventData> timedStream = source.map(str -> new EventData(System.currentTimeMillis(), str))
//                .uid("map-time");

        // 创建随机数据源
        String randText = "root,admin,manager,test,operator,developer,user";
        RandomSource randomSource = new RandomSource(randText);
        SingleOutputStreamOperator<EventData> timedStream = env.addSource(randomSource)
                .uid("random-source");

        timedStream.print("timed");

        // 设置窗口策略和水印
        SingleOutputStreamOperator<EventData> eventStream = timedStream.assignTimestampsAndWatermarks(new WatermarkStrategy<EventData>() {
            @Override
            public WatermarkGenerator<EventData> createWatermarkGenerator(WatermarkGeneratorSupplier.Context context) {
                // 处理乱序数据，最多等待1秒
                return new BoundedOutOfOrdernessWatermarks<EventData>(Duration.ofSeconds(1));
            }
        }.withTimestampAssigner(new TimestampAssignerSupplier<EventData>() {
            @Override
            public TimestampAssigner<EventData> createTimestampAssigner(Context context) {
                return new SerializableTimestampAssigner<EventData>() {
                    @Override
                    public long extractTimestamp(EventData eventData, long l) {
                        // 从数据中提取时间戳
                        return eventData.timestamp;
                    }
                };
            }
        })).uid("event-watermark");

        eventStream.print("event");

        // 按照数据分组
        // 这个流已经分组了，因此之后的所有使用这个流的，都已经按照键分组了
        KeyedStream<EventData, String> keyedStream = eventStream.keyBy(new KeySelector<EventData, String>() {
            @Override
            public String getKey(EventData event) throws Exception {
                // 从数据中提取键
                return event.data;
            }
        });

        keyedStream.print("key");

        // 统计历史所有累计，每10秒输出一次，由于基于key分组的，因此，进入此算子的数据，只会是符合分组key的数据，因此内部针对单个key处理即可
        SingleOutputStreamOperator<Tuple2<String, Integer>> historyStream = keyedStream.process(new KeyedProcessFunction<String, EventData, Tuple2<String, Integer>>() {
            // 定义状态：累计次数，发送时间
            protected ValueState<Integer> countState;
            protected ValueState<Long> sendState;

            @Override
            public void open(Configuration parameters) throws Exception {
                // 在open周期中初始化状态
                countState = getRuntimeContext().getState(new ValueStateDescriptor<Integer>("key-count", Integer.class));
                sendState = getRuntimeContext().getState(new ValueStateDescriptor<Long>("send-time", Long.class));
            }

            @Override
            public void processElement(EventData eventData, Context context, Collector<Tuple2<String, Integer>> collector) throws Exception {
                // 处理历史个数
                Integer cnt = countState.value();
                countState.update(cnt == null ? 1 : cnt + 1);
                if (sendState.value() == null) {
                    // 当没有发送时间的时候，初始化一条发送时间
                    sendState.update(context.timestamp() + 10 * 1000L);
                    // 注册定时器，定时发送
                    context.timerService().registerEventTimeTimer(sendState.value());
                }
            }

            @Override
            public void onTimer(long timestamp, OnTimerContext ctx, Collector<Tuple2<String, Integer>> out) throws Exception {
                out.collect(Tuple2.of(ctx.getCurrentKey(), countState.value()));
                // 输出之后，直接定义下一个输出触发器，实现每10秒输出一次
                sendState.update(timestamp + 10 * 1000L);
                // 注册定时器，定时发送
                ctx.timerService().registerEventTimeTimer(sendState.value());
            }
        });

        historyStream.addSink(new PrintSink<>("history"));

        // 开启滑动窗口，10秒一个窗口，每次滑动5秒
        WindowedStream<EventData, String, TimeWindow> windowStream = keyedStream.window(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5)));

        // 按照窗口聚合，得到每条数据的频度，统计最近10秒的频度，每5秒输出一次
        // 使用聚合+操作窗口方式，同时获取聚合结果和窗口信息
        SingleOutputStreamOperator<Tuple3<String, String, Integer>> aggStream = windowStream.aggregate(new AggregateFunction<EventData, Integer, Integer>() {
            @Override
            public Integer createAccumulator() {
                // 频度初始为0
                return 0;
            }

            @Override
            public Integer add(EventData eventData, Integer integer) {
                // 每来一条数据，频度+1，原因是基于分组key的，数据都是这个key的，直接+1即可
                return integer + 1;
            }

            @Override
            public Integer getResult(Integer integer) {
                // 获取聚合的结果
                return integer;
            }

            @Override
            public Integer merge(Integer integer, Integer acc1) {
                return integer + acc1;
            }
        }, new ProcessWindowFunction<Integer, Tuple3<String, String, Integer>, String, TimeWindow>() {
            @Override
            public void process(String key, Context context, Iterable<Integer> iterable, Collector<Tuple3<String, String, Integer>> collector) throws Exception {
                // 获取窗口信息
                long start = context.window().getStart();
                long end = context.window().getEnd();
                String windowRange = new Timestamp(start) + " ~ " + new Timestamp(end);
                // 将本次窗口中的所有结果输出
                for (Integer item : iterable) {
                    collector.collect(Tuple3.of(key, windowRange, item));
                }
            }
        });

        // 获取每条数据在每个窗口中出现的时间戳列表
        // 直接基于全窗口自行处理，直接得到窗口内全部数据
        SingleOutputStreamOperator<Tuple2<String, String>> processedStream = windowStream.process(new ProcessWindowFunction<EventData, Tuple2<String, String>, String, TimeWindow>() {
            @Override
            public void process(String key, Context context, Iterable<EventData> iterable, Collector<Tuple2<String, String>> collector) throws Exception {
                // 把窗口内的每条数据的时间戳拿出来显示
                String str = "";
                for (EventData item : iterable) {
                    str += " ~ " + new Timestamp(item.timestamp);
                }
                collector.collect(Tuple2.of(key, str));
            }
        });

        aggStream.print("agg");

        processedStream.print("process");

        // 将窗口内的频度与窗口内的时间戳组合
        // api调用方式固定，对比sql: select * from a join b on a.key=b.key group by window
        // select apply a join b where a.key equalTo b.key group by window
        DataStream<String> joinStream = aggStream.join(processedStream)
                .where(new KeySelector<Tuple3<String, String, Integer>, String>() {
                    @Override
                    public String getKey(Tuple3<String, String, Integer> val) throws Exception {
                        // 使用分组键作为键
                        return val.f0;
                    }
                })
                .equalTo(new KeySelector<Tuple2<String, String>, String>() {
                    @Override
                    public String getKey(Tuple2<String, String> val) throws Exception {
                        // 使用分组键作为键
                        return val.f0;
                    }
                }).window(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5)))
                .apply(new JoinFunction<Tuple3<String, String, Integer>, Tuple2<String, String>, String>() {
                    @Override
                    public String join(Tuple3<String, String, Integer> left, Tuple2<String, String> right) throws Exception {
                        // 拼接得到 分组键：窗口：频度：时间戳列表
                        return left.f0 + " : " + left.f1 + " : " + left.f2 + " : " + right.f1;
                    }
                });

        joinStream.print("join");

        joinStream.addSink(new PrintSink<>("print"));

        env.execute();
    }
}
