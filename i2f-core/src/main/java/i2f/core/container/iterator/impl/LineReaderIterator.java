package i2f.core.container.iterator.impl;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Iterator;

public class LineReaderIterator implements Iterator<String>, Closeable {
    private BufferedReader reader;
    private String line;

    public LineReaderIterator(BufferedReader reader) {
        this.reader = reader;
    }

    public LineReaderIterator(Reader reader) {
        if (reader instanceof BufferedReader) {
            this.reader = (BufferedReader) reader;
        } else {
            this.reader = new BufferedReader(reader);
        }
    }

    public LineReaderIterator(InputStream is, Charset charset) {
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
                line = reader.readLine();
            }
            if (line == null) {
                close();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return line == null;
    }

    @Override
    public String next() {
        return line;
    }
}
