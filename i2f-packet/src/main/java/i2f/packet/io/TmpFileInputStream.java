package i2f.packet.io;

import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Ice2Faith
 * @date 2024/3/8 14:34
 * @desc
 */
public class TmpFileInputStream extends FilterInputStream {
    private File tmpFile;

    public TmpFileInputStream(InputStream in) {
        super(in);
    }

    public TmpFileInputStream(InputStream in, File tmpFile) {
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
}
