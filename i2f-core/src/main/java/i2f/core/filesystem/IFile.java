package i2f.core.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @author ltb
 * @date 2022/5/7 10:22
 * @desc
 */
public interface IFile {
    IFile setFileSystem(IFileSystem fileSystem);
    IFileSystem getFileSystem();
    IFile ofPath(String path);

    String getName();
    String getExtension();
    String getPath();
    String getParentPath();
    String getPathOperator();
    String getExtensionOperator();

    String getAbsolutePath() throws IOException;

    boolean isFile() throws IOException;
    boolean isDirectory() throws IOException;

    boolean create() throws IOException;
    boolean delete() throws IOException;
    boolean deleteAll() throws IOException;
    boolean exists() throws IOException;
    boolean renameTo(String dstPath,boolean overwrite) throws IOException;
    boolean renameTo(IFile dstFile,boolean overwrite) throws IOException;

    boolean mkdir() throws IOException;
    boolean mkdirs() throws IOException;

    boolean copyTo(String dstPath,boolean overwrite) throws IOException;
    boolean copyAllTo(String dstPath,boolean overwrite) throws IOException;
    boolean moveTo(String dstPath,boolean overwrite) throws IOException;
    boolean moveAllTo(String dstPath,boolean overwrite) throws IOException;
    boolean copyTo(IFile dstFile,boolean overwrite) throws IOException;
    boolean moveTo(IFile dstFile,boolean overwrite) throws IOException;

    InputStream readOpen() throws IOException;
    OutputStream writeOpen() throws IOException;

    List<IFile> list() throws IOException;
    List<IFile> listFiles() throws IOException;
    List<IFile> listDirectories() throws IOException;
}
