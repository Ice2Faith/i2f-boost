package i2f.core.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @author ltb
 * @date 2022/5/7 9:44
 * @desc
 */
public interface IFileSystem {
    String getName(String path);
    String getExtension(String path);
    String getPath(String path);
    String getParentPath(String path);
    String getPathOperator();
    String getExtensionOperator();

    String getAbsolutePath(String path) throws IOException;

    boolean isFile(String path) throws IOException;
    boolean isDirectory(String path) throws IOException;

    boolean create(String path) throws IOException;
    boolean delete(String path) throws IOException;
    boolean exists(String path) throws IOException;
    boolean renameTo(String srcPath,String dstPath,boolean overwrite) throws IOException;

    boolean mkdir(String path) throws IOException;
    boolean mkdirs(String path) throws IOException;

    boolean copyTo(String srcPath,String dstPath,boolean overwrite) throws IOException;
    boolean moveTo(String srcPath,String dstPath,boolean overwrite) throws IOException;

    InputStream readOpen(String path) throws IOException;
    OutputStream writeOpen(String path) throws IOException;

    List<String> list(String path) throws IOException;
    List<String> listFiles(String path) throws IOException;
    List<String> listDirectories(String path) throws IOException;
}
