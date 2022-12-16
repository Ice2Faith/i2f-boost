package i2f.core.streaming.base.sink;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 10:19
 * @desc
 */
public class StringifySinkStreaming<E> extends AbsSinkStreaming<String, E, E> {

    public String open;
    public String separator;
    public String close;

    public StringifySinkStreaming(String open, String separator, String close) {
        this.open = open;
        this.separator = separator;
        this.close = close;
    }

    @Override
    protected String sink(Iterator<E> iterator, ExecutorService pool) {
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        while (iterator.hasNext()) {
            E item = iterator.next();
            if (isFirst) {
                if (open != null) {
                    builder.append(open);
                }
            }
            builder.append(item);
            if (!isFirst) {
                if (separator != null) {
                    builder.append(separator);
                }
            }
            isFirst = false;
        }
        if (!isFirst) {
            if (close != null) {
                builder.append(close);
            }
        }
        return builder.toString();
    }
}
