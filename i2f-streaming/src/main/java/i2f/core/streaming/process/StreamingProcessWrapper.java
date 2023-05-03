package i2f.core.streaming.process;

import i2f.core.streaming.rich.RichStreaming;
import i2f.core.streaming.rich.RichStreamingWrapper;

/**
 * @author Ice2Faith
 * @date 2023/4/22 22:13
 * @desc
 */
public abstract class StreamingProcessWrapper<IN, OUT> extends RichStreamingWrapper implements StreamingProcessor<IN, OUT>, RichStreaming {

}
