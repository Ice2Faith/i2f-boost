package i2f.core.io.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface IFileSystem {
    String pathSeparator();

    IFile getFile(String path);

    IFile getFile(String path, String subPath);

    /**
     * 严格模式获取文件对象
     * path必须是rootPath的子树路径
     * 也就是说，限定了path必须在rootPath目录下
     *
     * @param rootPath
     * @param path
     * @return
     */
    IFile getStrictFile(String rootPath, String path);

    String combinePath(String path, String subPath);

    /**
     * 相对路径转换
     * 去除符号：../ ./ // %00 等相对路径组成部分
     * 还原相对路径表达的实际路径
     * 可以用来防止利用相对路径非法访问或者截断路径攻击
     *
     * @param path
     * @return
     */
    String absPath(String path);

    String getName(String path);

    String getAbsolutePath(String path);

    IFile getDirectory(String path);

    boolean isDirectory(String path);

    boolean isFile(String path);

    boolean isExists(String path);

    List<IFile> listFiles(String path);

    void delete(String path);

    boolean isReadable(String path);

    boolean isWritable(String path);

    boolean isAppendable(String path);

    InputStream getInputStream(String path) throws IOException;

    OutputStream getOutputStream(String path) throws IOException;

    OutputStream getAppendOutputStream(String path) throws IOException;

    void store(String path, InputStream is) throws IOException;

    void load(String path, OutputStream os) throws IOException;

    void mkdir(String path);

    void mkdirs(String path);

    void copyTo(String srcPath, String dstPath) throws IOException;

    void moveTo(String srcPath, String dstPath) throws IOException;

    long length(String path);
}
