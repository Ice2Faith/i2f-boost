package i2f.core.container.iterator.impl;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Iterator;

public class CharReaderIterator implements Iterator<Character>, Closeable {
    private Reader reader;
    private int ch;

    public CharReaderIterator(Reader reader) {
        this.reader = reader;
    }


    public CharReaderIterator(InputStream is, Charset charset) {
        this.reader = new BufferedReader(new InputStreamReader(is, charset));
    }


    @Override
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
            reader = null;
        }
    }

    @Override
    public boolean hasNext() {
        try {
            if (reader != null) {
                ch = reader.read();
            }
            if (ch < 0) {
                close();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return ch >= 0;
    }

    @Override
    public Character next() {
        return (char) ch;
    }
}
