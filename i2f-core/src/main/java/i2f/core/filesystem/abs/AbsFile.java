package i2f.core.filesystem.abs;

import i2f.core.filesystem.IFile;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public abstract class AbsFile implements IFile {

    @Override
    public IFile getFile(String path) {
        return getFileSystem().getFile(path);
    }

    @Override
    public String getAbsolutePath() {
        return getFileSystem().getAbsolutePath(getPath());
    }

    @Override
    public boolean isDirectory() {
        return getFileSystem().isDirectory(getPath());
    }

    @Override
    public boolean isFile() {
        return getFileSystem().isFile(getPath());
    }

    @Override
    public boolean isExists() {
        return getFileSystem().isExists(getPath());
    }

    @Override
    public List<IFile> listFiles() {
        return getFileSystem().listFiles(getPath());
    }

    @Override
    public void delete() {
        getFileSystem().delete(getPath());
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return getFileSystem().getInputStream(getPath());
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return getFileSystem().getOutputStream(getPath());
    }

    @Override
    public OutputStream getAppendOutputStream() throws IOException {
        return getFileSystem().getAppendOutputStream(getPath());
    }

    @Override
    public void store(InputStream is) throws IOException {
        getFileSystem().store(getPath(), is);
    }

    @Override
    public void load(OutputStream os) throws IOException {
        getFileSystem().load(getPath(), os);
    }

    @Override
    public void mkdir() {
        getFileSystem().mkdir(getPath());
    }

    @Override
    public long length() {
        return getFileSystem().length(getPath());
    }

    @Override
    public String pathSeparator() {
        return getFileSystem().pathSeparator();
    }

    @Override
    public IFile getFile(String path, String subPath) {
        return getFileSystem().getFile(path, subPath);
    }

    @Override
    public IFile getFile(IFile path, String subPath) {
        return getFile(path.getPath(), subPath);
    }

    @Override
    public String getName() {
        String path = this.getPath();
        int idx = path.lastIndexOf(pathSeparator());
        if (idx >= 0) {
            return path.substring(idx + 1);
        }
        return path;
    }

    @Override
    public String getExtension() {
        String name = this.getName();
        int idx = name.lastIndexOf(".");
        if (idx >= 0) {
            return name.substring(0, idx);
        }
        return name;
    }


    @Override
    public IFile getDirectory() {
        String sep = pathSeparator();
        String path = this.getPath();
        int idx = path.lastIndexOf(sep);
        if (idx >= 0) {
            return getFile(path.substring(0, idx));
        }
        if (path.startsWith(sep)) {
            return getFile(sep);
        }
        return getFile(".");
    }


    @Override
    public BufferedReader getReader(String charset) throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream(), charset));
    }

    @Override
    public BufferedWriter getWriter(String charset) throws IOException {
        return new BufferedWriter(new OutputStreamWriter(getOutputStream(), charset));
    }

    @Override
    public BufferedWriter getAppendWriter(String charset) throws IOException {
        return new BufferedWriter(new OutputStreamWriter(getAppendOutputStream(), charset));
    }

    @Override
    public void mkdirs() {
        FileSystemUtil.recursiveMkdirs(this);
    }

    @Override
    public void copyTo(IFile file) throws IOException {
        InputStream is = this.getInputStream();
        OutputStream os = file.getOutputStream();
        FileSystemUtil.streamCopy(is, os);
    }

    @Override
    public void moveTo(IFile file) throws IOException {
        InputStream is = this.getInputStream();
        OutputStream os = file.getOutputStream();
        FileSystemUtil.streamCopy(is, os);
        this.delete();
    }

    @Override
    public void writeBytes(byte[] data) throws IOException {
        OutputStream os = this.getOutputStream();
        os.write(data);
        os.flush();
        os.close();
    }

    @Override
    public void appendBytes(byte[] data) throws IOException {
        OutputStream os = this.getAppendOutputStream();
        os.write(data);
        os.flush();
        os.close();
    }

    @Override
    public void writeText(String text, String charset) throws IOException {
        byte[] data = text.getBytes(charset);
        this.writeBytes(data);
    }

    @Override
    public void appendText(String text, String charset) throws IOException {
        byte[] data = text.getBytes(charset);
        this.appendBytes(data);
    }

    @Override
    public byte[] readBytes() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream is = this.getInputStream();
        FileSystemUtil.streamCopy(is, bos);
        return bos.toByteArray();
    }

    @Override
    public String readText(String charset) throws IOException {
        byte[] data = this.readBytes();
        return new String(data, charset);
    }

    @Override
    public List<String> readLines(String charset) throws IOException {
        BufferedReader reader = null;
        List<String> ret = new LinkedList<>();
        try {
            new BufferedReader(new InputStreamReader(this.getInputStream(), charset));
            String line = null;
            while ((line = reader.readLine()) != null) {
                ret.add(line);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return ret;
    }

    @Override
    public void writeLines(Collection<String> lines, String charset) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = getWriter(charset);
            for (String item : lines) {
                writer.write(item);
                writer.write("\n");
            }
            writer.flush();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    @Override
    public void appendLines(Collection<String> lines, String charset) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = getAppendWriter(charset);
            for (String item : lines) {
                writer.write(item);
                writer.write("\n");
            }
            writer.flush();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + "@" + Integer.toHexString(hashCode())
                + "("
                + "fs=" + getFileSystem().getClass().getSimpleName()
                + ",path=" + getPath()
                + ")";
    }
}
