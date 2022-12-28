package i2f.extension.filesystem.sftp;

import i2f.core.filesystem.IFileSystem;
import i2f.core.filesystem.abs.AbsFile;

public class SftpFile extends AbsFile {
    private IFileSystem fs;
    private String path;

    public SftpFile(IFileSystem fs, String path) {
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
}
