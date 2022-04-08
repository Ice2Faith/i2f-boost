package i2f.core.compress;

import i2f.core.compress.data.CompressBindData;
import i2f.core.compress.data.CompressBindFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author ltb
 * @date 2022/3/31 15:31
 * @desc
 */
public interface ICompressProvider {
    void compress(File output,File ... inputs) throws IOException;
    void compress(File output, CompressBindFile ... inputs) throws IOException;
    void compress(File output,CompressBindData ... inputs) throws IOException;
    void release(InputStream input, File output) throws IOException;
    void release(File input,File output) throws IOException;
}
