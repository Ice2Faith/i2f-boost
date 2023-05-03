package i2f.core.streaming.rich;

import i2f.core.streaming.impl.StreamingContext;

/**
 * @author Ice2Faith
 * @date 2023/4/22 22:13
 * @desc
 */
public abstract class RichStreamingWrapper implements RichStreaming {
    private StreamingContext context;
    private Object[] args;

    public void setContext(StreamingContext context) {
        this.context = context;
    }

    public void setArgs(Object... args) {
        this.args = args;
    }

    @Override
    public StreamingContext getContext() {
        return this.context;
    }

    @Override
    public Object[] getArgs() {
        return this.args;
    }

}
