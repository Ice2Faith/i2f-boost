package i2f.extension.flink.test.source;

import i2f.extension.flink.test.data.EventData;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.util.Random;

/**
 * @author ltb
 * @date 2022/11/14 15:09
 * @desc 定义一个随机词条的输入源
 * 从给定的一片文章中，拆分出词条随机发送作为数据源
 * 它具有生命周期函数
 * 可以进行资源的初始化与释放操作
 */
@Slf4j
public class RandomSource extends RichSourceFunction<EventData> {
    private String dataLine;
    private String[] datas;
    private boolean run = true;
    private Random random = new Random();

    public RandomSource(String dataLine) {
        this.dataLine = dataLine;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        // 这里的初始化，没有太多意义，仅仅表示资源的初始化操作
        String[] arr = dataLine.split("\n|,|，|;|；");
        datas = arr;
        log.info(this.getClass().getSimpleName() + " open.");
    }

    @Override
    public void run(SourceContext<EventData> sourceContext) throws Exception {
        // 输入源，一般是一个无界流，也就是不断的输出，无终止的，因此这也是最常见的写法
        // 不论是从数据库，还是文件，都是无休止的读取之后进行输出
        // 直到用户点击了取消操作或者任务被终止而产生操作才退出
        while (run) {
            // 生成并输出一条随机数据
            int idx = random.nextInt(datas.length);
            EventData data = new EventData(System.currentTimeMillis(), datas[idx]);
            sourceContext.collect(data);
            // 睡眠一段随机的时间，模拟真实的数据情况
            Thread.sleep(random.nextInt(5000) + 200);
        }
    }

    @Override
    public void cancel() {
        // 当被取消时，取消任务执行
        run = false;
        log.info(this.getClass().getSimpleName() + " cancel.");
    }

    @Override
    public void close() throws Exception {
        log.info(this.getClass().getSimpleName() + " close.");
    }
}
