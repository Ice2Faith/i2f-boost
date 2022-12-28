package i2f.extension.filesystem.minio;

import i2f.core.filesystem.IFileSystem;
import i2f.core.filesystem.abs.AbsFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class MinioFile extends AbsFile {
    private IFileSystem fs;
    private String path;

    public MinioFile(String path) {
        this.path = path;
    }

    public MinioFile(IFileSystem fs, String path) {
        this.fs = fs;
        this.path = path;
    }

    @Override
    public void setFileSystem(IFileSystem fs) {
        this.fs = fs;
    }

    @Override
    public IFileSystem getFileSystem() {
        return fs;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void writeBytes(byte[] data) throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        getFileSystem().store(getPath(), is);
    }

}
