package i2f.core.streaming.rich;

import i2f.core.streaming.impl.StreamingContext;

/**
 * @author Ice2Faith
 * @date 2023/4/22 22:54
 * @desc
 */
public interface RichStreaming {
    StreamingContext getContext();

    Object[] getArgs();
}
