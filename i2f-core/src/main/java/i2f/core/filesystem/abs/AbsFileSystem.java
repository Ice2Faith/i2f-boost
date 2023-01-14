package i2f.core.filesystem.abs;

import i2f.core.filesystem.IFile;
import i2f.core.filesystem.IFileSystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbsFileSystem implements IFileSystem {
    public static final String DEFAULT_PATH_SEPARATOR = "/";

    @Override
    public String pathSeparator() {
        return DEFAULT_PATH_SEPARATOR;
    }

    @Override
    public IFile getFile(String path, String subPath) {
        return getFile(this.combinePath(path, subPath));
    }

    @Override
    public IFile getStrictFile(String rootPath, String path) {
        return FileSystemUtil.getStrictFile(this, rootPath, path);
    }

    @Override
    public String combinePath(String path, String subPath) {
        return FileSystemUtil.combinePath(path, subPath, pathSeparator());
    }

    @Override
    public String absPath(String path) {
        return FileSystemUtil.absPath(path, pathSeparator());
    }

    @Override
    public String getName(String path) {
        String fullPath = getAbsolutePath(path);
        int idx = fullPath.lastIndexOf(pathSeparator());
        if (idx >= 0) {
            return fullPath.substring(idx + 1);
        }
        if (fullPath.startsWith(pathSeparator())) {
            return fullPath.substring(pathSeparator().length());
        }
        return fullPath;
    }

    @Override
    public IFile getDirectory(String path) {
        String fullPath = getAbsolutePath(path);
        int idx = fullPath.lastIndexOf(pathSeparator());
        if (idx > 0) {
            return getFile(fullPath.substring(0, idx));
        } else if (idx == 0) {
            return getFile(pathSeparator());
        }
        if (fullPath.startsWith(pathSeparator())) {
            return getFile(pathSeparator());
        }
        return getFile(".");
    }

    @Override
    public boolean isReadable(String path) {
        return isExists(path) && isFile(path);
    }

    @Override
    public boolean isWritable(String path) {
        return isExists(path) && isFile(path);
    }

    @Override
    public void store(String path, InputStream is) throws IOException {
        FileSystemUtil.streamCopy(is, getOutputStream(path));
    }

    @Override
    public void load(String path, OutputStream os) throws IOException {
        FileSystemUtil.streamCopy(getInputStream(path), os);
    }

    @Override
    public void mkdirs(String path) {
        FileSystemUtil.recursiveMkdirs(getFile(path));
    }

    @Override
    public void copyTo(String srcPath, String dstPath) throws IOException {
        InputStream is = getInputStream(srcPath);
        OutputStream os = getOutputStream(dstPath);
        FileSystemUtil.streamCopy(is, os);
    }

    @Override
    public void moveTo(String srcPath, String dstPath) throws IOException {
        InputStream is = getInputStream(srcPath);
        OutputStream os = getOutputStream(dstPath);
        FileSystemUtil.streamCopy(is, os);
        delete(srcPath);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + "@" + Integer.toHexString(hashCode())
                + "("
                + "pathSeparator=" + pathSeparator()
                + ")";
    }
}
