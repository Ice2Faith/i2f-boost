package i2f.core.io.stream;

import i2f.core.lang.functional.jvf.Supplier;
import i2f.core.lazy.Lazyable;

import java.io.IOException;
import java.io.InputStream;

public class LazyInputStream extends InputStream implements Lazyable {
    private InputStream is;
    private Supplier<InputStream> supplier;
    private volatile boolean isRequire = false;

    public LazyInputStream(Supplier<InputStream> supplier) {
        this.supplier = supplier;
    }

    private void requireCheck() {
        if (isRequire) {
            return;
        }
        synchronized (this) {
            if (!isRequire) {
                this.is = supplier.get();
            }
            isRequire = true;
        }
    }

    @Override
    public int read() throws IOException {
        requireCheck();
        return is.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        requireCheck();
        return is.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        requireCheck();
        return is.read(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        requireCheck();
        return is.skip(n);
    }

    @Override
    public int available() throws IOException {
        requireCheck();
        return is.available();
    }

    @Override
    public void close() throws IOException {
        if (is != null) {
            is.close();
        }
    }

    @Override
    public synchronized void mark(int readlimit) {
        requireCheck();
        is.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        requireCheck();
        is.reset();
    }

    @Override
    public boolean markSupported() {
        requireCheck();
        return is.markSupported();
    }
}
