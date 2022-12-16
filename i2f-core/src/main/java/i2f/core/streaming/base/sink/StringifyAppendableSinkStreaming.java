package i2f.core.streaming.base.sink;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 10:19
 * @desc
 */
public class StringifyAppendableSinkStreaming<R extends Appendable, E> extends AbsSinkStreaming<R, E, E> {

    public R appender;
    public String open;
    public String separator;
    public String close;

    public StringifyAppendableSinkStreaming(R appender, String open, String separator, String close) {
        this.appender = appender;
        this.open = open;
        this.separator = separator;
        this.close = close;
    }

    @Override
    protected R sink(Iterator<E> iterator, ExecutorService pool) {
        boolean isFirst = true;
        while (iterator.hasNext()) {
            E item = iterator.next();
            if (isFirst) {
                if (open != null) {
                    append(appender, open);
                }
            }
            append(appender, item);
            if (!isFirst) {
                if (separator != null) {
                    append(appender, separator);
                }
            }
            isFirst = false;
        }
        if (!isFirst) {
            if (close != null) {
                append(appender, close);
            }
        }
        return appender;
    }

    private void append(R appender, Object obj) {
        if (appender instanceof StringBuilder) {
            StringBuilder builder = (StringBuilder) appender;
            builder.append(obj);
        } else if (appender instanceof StringBuffer) {
            StringBuffer buffer = (StringBuffer) appender;
            buffer.append(obj);
        } else {
            try {
                appender.append(String.valueOf(obj));
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    }
}
