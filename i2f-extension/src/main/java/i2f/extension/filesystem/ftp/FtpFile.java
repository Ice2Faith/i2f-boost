package i2f.extension.filesystem.ftp;

import i2f.core.filesystem.IFileSystem;
import i2f.core.filesystem.abs.AbsFile;

public class FtpFile extends AbsFile {
    private IFileSystem fs;
    private String path;

    public FtpFile(IFileSystem fs, String path) {
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
        return path;
    }
}
