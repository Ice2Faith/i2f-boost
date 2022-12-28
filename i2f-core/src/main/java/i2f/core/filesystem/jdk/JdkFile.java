package i2f.core.filesystem.jdk;

import i2f.core.filesystem.IFile;
import i2f.core.filesystem.IFileSystem;
import i2f.core.filesystem.abs.AbsFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JdkFile extends AbsFile {
    private static volatile IFileSystem sharedFs = new JdkFileSystem();

    private IFileSystem fs;

    {
        fs = sharedFs;
    }

    protected File file;

    public JdkFile(File file) {
        this.file = file;
    }

    public JdkFile(String path) {
        this.file = new File(path);
    }

    @Override
    public void setFileSystem(IFileSystem fs) {
        this.fs = fs;
    }

    @Override
    public IFileSystem getFileSystem() {
        return this.fs;
    }

    public File getFile() {
        return this.file;
    }

    @Override
    public String pathSeparator() {
        return File.separator;
    }

    @Override
    public String getPath() {
        return this.file.getPath();
    }

    @Override
    public IFile getFile(String path) {
        return new JdkFile(path);
    }

    @Override
    public String getName() {
        return this.file.getName();
    }

    @Override
    public String getAbsolutePath() {
        return this.file.getAbsolutePath();
    }

    @Override
    public IFile getDirectory() {
        return new JdkFile(this.file.getParentFile());
    }

    @Override
    public boolean isDirectory() {
        return this.file.isDirectory();
    }

    @Override
    public boolean isFile() {
        return this.file.isFile();
    }

    @Override
    public boolean isExists() {
        return this.file.exists();
    }

    @Override
    public List<IFile> listFiles() {
        File[] files = this.file.listFiles();
        if (files == null) {
            files = new File[0];
        }
        List<IFile> ret = new ArrayList<>(Math.max(files.length, 32));
        for (File item : files) {
            ret.add(new JdkFile(item));
        }
        return ret;
    }

    @Override
    public void delete() {
        this.file.delete();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.file);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return new FileOutputStream(this.file);
    }

    @Override
    public OutputStream getAppendOutputStream() throws IOException {
        return new FileOutputStream(this.file, true);
    }

    @Override
    public void mkdir() {
        this.file.mkdir();
    }

    @Override
    public void mkdirs() {
        this.file.mkdirs();
    }

    @Override
    public void moveTo(IFile file) throws IOException {
        if (file instanceof JdkFile) {
            JdkFile jfile = (JdkFile) file;
            this.file.renameTo(jfile.getFile());
        } else {
            super.moveTo(file);
        }
    }

    @Override
    public long length() {
        return this.file.length();
    }
}
