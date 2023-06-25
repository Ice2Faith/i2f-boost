package i2f.extension.filesystem.hdfs;

import i2f.core.io.filesystem.IFileSystem;
import i2f.core.io.filesystem.abs.AbsFile;

public class HdfsFile extends AbsFile {
    private IFileSystem fs;
    private String path;

    public HdfsFile(IFileSystem fs, String path) {
        this.fs = fs;
        this.path = path;
    }

    @Override
    public void setFileSystem(IFileSystem fs) {
        this.fs = fs;
    }

    @Override
    public IFileSystem getFileSystem() {
        return this.fs;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public void mkdirs() {
        this.mkdir();
    }
}
