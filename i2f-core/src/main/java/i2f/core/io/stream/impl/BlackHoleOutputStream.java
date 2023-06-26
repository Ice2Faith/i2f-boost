package i2f.core.io.stream.impl;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Ice2Faith
 * @date 2023/6/26 10:54
 * @desc
 */
public class BlackHoleOutputStream extends OutputStream {
    @Override
    public void write(int b) throws IOException {
        // do nothing
    }

    @Override
    public void write(byte[] b) throws IOException {
        // do nothing
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        // do nothing
    }

    @Override
    public void flush() throws IOException {
        // do nothing
    }

    @Override
    public void close() throws IOException {
        // do nothing
    }
}
