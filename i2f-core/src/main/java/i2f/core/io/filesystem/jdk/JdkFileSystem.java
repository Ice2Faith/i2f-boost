package i2f.core.io.filesystem.jdk;

import i2f.core.io.filesystem.IFile;
import i2f.core.io.filesystem.abs.AbsFileSystem;
import i2f.core.io.filesystem.abs.FileSystemUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JdkFileSystem extends AbsFileSystem {
    @Override
    public String pathSeparator() {
        return File.separator;
    }

    @Override
    public IFile getFile(String path) {
        JdkFile ret = new JdkFile(path);
        ret.setFileSystem(this);
        return ret;
    }


    @Override
    public String getName(String path) {
        File file = new File(path);
        return file.getName();
    }

    @Override
    public String getAbsolutePath(String path) {
        File file = new File(path);
        return file.getAbsolutePath();
    }

    @Override
    public IFile getDirectory(String path) {
        File file = new File(path);
        return getFile(file.getParentFile().getAbsolutePath());
    }

    @Override
    public boolean isDirectory(String path) {
        File file = new File(path);
        return file.isDirectory();
    }

    @Override
    public boolean isFile(String path) {
        File file = new File(path);
        return file.isFile();
    }

    @Override
    public boolean isExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    @Override
    public List<IFile> listFiles(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null) {
            files = new File[0];
        }
        List<IFile> ret = new ArrayList<>(Math.max(files.length, 32));
        for (File item : files) {
            ret.add(getFile(item.getAbsolutePath()));
        }
        return ret;
    }

    @Override
    public void delete(String path) {
        File file = new File(path);
        file.delete();
    }

    @Override
    public boolean isReadable(String path) {
        File file = new File(path);
        return file.exists() && file.isFile();
    }

    @Override
    public boolean isWritable(String path) {
        File file = new File(path);
        return file.exists() && file.isFile();
    }

    @Override
    public boolean isAppendable(String path) {
        File file = new File(path);
        return file.exists() && file.isFile();
    }

    @Override
    public InputStream getInputStream(String path) throws IOException {
        File file = new File(path);
        return new FileInputStream(file);
    }

    @Override
    public OutputStream getOutputStream(String path) throws IOException {
        File file = new File(path);
        return new FileOutputStream(file);
    }

    @Override
    public OutputStream getAppendOutputStream(String path) throws IOException {
        File file = new File(path);
        return new FileOutputStream(file, true);
    }

    @Override
    public void mkdir(String path) {
        File file = new File(path);
        file.mkdir();
    }

    @Override
    public void mkdirs(String path) {
        File file = new File(path);
        file.mkdirs();
    }

    @Override
    public void copyTo(String srcPath, String dstPath) throws IOException {
        InputStream is = getInputStream(srcPath);
        OutputStream os = getOutputStream(dstPath);
        FileSystemUtil.streamCopy(is, os);
    }

    @Override
    public void moveTo(String srcPath, String dstPath) throws IOException {
        File file = new File(srcPath);
        file.renameTo(new File(dstPath));
    }

    @Override
    public long length(String path) {
        File file = new File(path);
        return file.length();
    }
}
