package i2f.packet.io;

import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/3/8 14:34
 * @desc
 */
public class TempFileInputStream extends FilterInputStream {
    protected File tmpFile;

    public TempFileInputStream(InputStream in) {
        super(in);
    }

    public TempFileInputStream(InputStream in, File tmpFile) {
        super(in);
        this.tmpFile = tmpFile;
    }

    @Override
    public void close() throws IOException {
        super.close();
        if(tmpFile!=null){
            tmpFile.delete();
        }
    }

    public File getTmpFile() {
        return tmpFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TempFileInputStream that = (TempFileInputStream) o;
        return Objects.equals(tmpFile, that.tmpFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tmpFile);
    }

    @Override
    public String toString() {
        return "TempFileInputStream{" +
                "tmpFile=" + tmpFile +
                '}';
    }
}
