package i2f.extension.flink.test.demo2;

import i2f.extension.flink.test.data.EventData;
import i2f.extension.flink.test.source.RandomSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.TimestampAssigner;
import org.apache.flink.api.common.eventtime.TimestampAssignerSupplier;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.time.Duration;

import static org.apache.flink.table.api.Expressions.$;

@Slf4j
public class TableFlinkMain {
    public static void main(String[] args) throws Exception {
        log.info("flink 初始化任务");
        Configuration conf = new Configuration();
        conf.setInteger("rest.port", 9999);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(conf);

        StreamTableEnvironment tabEnv = StreamTableEnvironment.create(env);

        String randText = "root,admin,manager,test,operator,developer,user";
        RandomSource randomSource = new RandomSource(randText);
        SingleOutputStreamOperator<EventData> inputStream = env.addSource(randomSource)
                .uid("random-source");

        SingleOutputStreamOperator<EventData> timedInputStream = inputStream.assignTimestampsAndWatermarks(WatermarkStrategy.<EventData>forBoundedOutOfOrderness(Duration.ofSeconds(0))
                .withTimestampAssigner(new TimestampAssignerSupplier<EventData>() {
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
                }));

        Table inputTable = tabEnv.fromDataStream(timedInputStream
                , $("timestamp").rowtime(), $("data")
        );


        tabEnv.createTemporaryView("input_table", inputTable);


        Table windowsTable = tabEnv.sqlQuery("select window_start,window_end,\n" +
                "                `data` as words,count(*) `cnt`\n" +
                "                from TABLE (\n" +
                "                        HOP(TABLE input_table,DESCRIPTOR(`timestamp`),INTERVAL '5' SECOND,INTERVAL '10' SECOND)\n" +
                "                )\n" +
                "                group by window_start,window_end,`data`");

        tabEnv.toChangelogStream(windowsTable).print("output");

        env.execute();
    }
}
