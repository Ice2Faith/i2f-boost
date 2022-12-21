package i2f.extension.flink.test.sink;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Timestamp;

/**
 * @author ltb
 * @date 2022/11/14 16:03
 * @desc 定义一个类似日志输出的下沉函数
 * 这里使用更为广泛使用的RichSinkFunction
 * 它具有声明周期，一般来说，输出都是输出到外部，一般都会涉及到输出的初始化
 * 比如：建立连接，打开文件等
 */
@Slf4j
public class PrintSink<T> extends RichSinkFunction<T> {
    private String prefix;

    public PrintSink(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        // 这里的初始化操作，没有什么操作，仅仅为了表示生命周期
        if (prefix == null) {
            prefix = "";
        }
        log.info(this.getClass().getSimpleName() + " open.");
    }

    @Override
    public void invoke(T value, Context context) throws Exception {
        long timestamp = context.currentProcessingTime();
        log.info(prefix + " " + new Timestamp(timestamp) + " > " + value);
    }

    @Override
    public void close() throws Exception {
        log.info(this.getClass().getSimpleName() + " close.");
    }
}
