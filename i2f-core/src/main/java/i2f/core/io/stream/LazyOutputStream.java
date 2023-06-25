package i2f.core.io.stream;

import i2f.core.lang.functional.jvf.Supplier;
import i2f.core.lazy.Lazyable;

import java.io.IOException;
import java.io.OutputStream;

public class LazyOutputStream extends OutputStream implements Lazyable {
    private OutputStream os;
    private Supplier<OutputStream> supplier;
    private volatile boolean isRequire = false;

    public LazyOutputStream(Supplier<OutputStream> supplier) {
        this.supplier = supplier;
    }

    private void requireCheck() {
        if (isRequire) {
            return;
        }
        synchronized (this) {
            if (!isRequire) {
                this.os = supplier.get();
            }
            isRequire = true;
        }
    }

    @Override
    public void write(int b) throws IOException {
        requireCheck();
        os.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        requireCheck();
        os.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        requireCheck();
        os.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        if (os != null) {
            os.flush();
        }
    }

    @Override
    public void close() throws IOException {
        if (os != null) {
            os.close();
        }
    }
}
