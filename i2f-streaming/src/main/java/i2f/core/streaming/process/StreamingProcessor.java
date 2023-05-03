package i2f.core.streaming.process;

import java.util.Iterator;

/**
 * @author Ice2Faith
 * @date 2023/4/22 22:06
 * @desc
 */
public interface StreamingProcessor<IN, OUT> {
    Iterator<OUT> process(Iterator<IN> iterator);
}
