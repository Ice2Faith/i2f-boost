package i2f.core.io.filesystem;

import java.io.*;
import java.util.Collection;
import java.util.List;

public interface IFile {
    String DEFAULT_CHARSET = "UTF-8";

    void setFileSystem(IFileSystem fs);

    IFileSystem getFileSystem();

    String pathSeparator();

    String getPath();

    IFile getFile(String path);

    IFile getFile(String path, String subPath);

    IFile getFile(IFile path, String subPath);

    String getName();

    String getExtension();

    String getAbsolutePath();

    IFile getDirectory();

    boolean isDirectory();

    boolean isFile();

    boolean isExists();

    List<IFile> listFiles();

    void delete();

    InputStream getInputStream() throws IOException;

    OutputStream getOutputStream() throws IOException;

    OutputStream getAppendOutputStream() throws IOException;

    BufferedReader getReader(String charset) throws IOException;

    BufferedWriter getWriter(String charset) throws IOException;

    BufferedWriter getAppendWriter(String charset) throws IOException;

    default BufferedReader getReader() throws IOException {
        return getReader(DEFAULT_CHARSET);
    }

    default BufferedWriter getWriter() throws IOException {
        return getWriter(DEFAULT_CHARSET);
    }

    default BufferedWriter getAppendWriter() throws IOException {
        return getAppendWriter(DEFAULT_CHARSET);
    }

    void store(InputStream is) throws IOException;

    void load(OutputStream os) throws IOException;

    void mkdir();

    void mkdirs();

    void copyTo(IFile file) throws IOException;

    void moveTo(IFile file) throws IOException;

    long length();

    void writeBytes(byte[] data) throws IOException;

    void appendBytes(byte[] data) throws IOException;

    void writeText(String text, String charset) throws IOException;

    void appendText(String text, String charset) throws IOException;

    default void writeText(String text) throws IOException {
        writeText(text, DEFAULT_CHARSET);
    }

    default void appendText(String text) throws IOException {
        appendText(text, DEFAULT_CHARSET);
    }

    byte[] readBytes() throws IOException;

    String readText(String charset) throws IOException;

    List<String> readLines(String charset) throws IOException;

    void writeLines(Collection<String> lines, String charset) throws IOException;

    void appendLines(Collection<String> lines, String charset) throws IOException;

    default String readText() throws IOException {
        return readText(DEFAULT_CHARSET);
    }

    default List<String> readLines() throws IOException {
        return readLines(DEFAULT_CHARSET);
    }

    default void writeLines(Collection<String> lines) throws IOException {
        writeLines(lines, DEFAULT_CHARSET);
    }

    default void appendLines(Collection<String> lines) throws IOException {
        appendLines(lines, DEFAULT_CHARSET);
    }
}
