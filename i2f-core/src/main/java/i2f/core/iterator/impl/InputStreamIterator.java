package i2f.core.iterator.impl;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class InputStreamIterator implements Iterator<Byte>, Closeable {
    private InputStream is;
    private int bt;

    public InputStreamIterator(InputStream is) {
        this.is = is;
    }

    @Override
    public void close() throws IOException {
        if (is != null) {
            is.close();
            is = null;
        }
    }

    @Override
    public boolean hasNext() {
        try {
            if (is != null) {
                bt = is.read();
            }
            if (bt < 0) {
                close();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return bt >= 0;
    }

    @Override
    public Byte next() {
        return (byte) (bt & 0x0ff);
    }
}
