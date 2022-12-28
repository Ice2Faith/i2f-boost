package i2f.extension.filesystem.hdfs;

import i2f.core.filesystem.IFile;
import i2f.core.filesystem.abs.AbsFileSystem;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

public class HdfsFileSystem extends AbsFileSystem implements Closeable {
    private HdfsMeta meta;
    private FileSystem fs;

    public HdfsFileSystem(HdfsMeta meta) {
        this.meta = meta;
        getFileSystem();
    }

    public FileSystem getFileSystem() {
        if (fs == null) {
            Configuration conf = meta.getConfig();
            if (conf == null) {
                conf = new Configuration();
            }
            conf.set("fs.defaultFS", meta.getDefaultFs());
            try {
                if (meta.getUri() == null || meta.getUser() == null) {
                    fs = FileSystem.get(conf);
                } else {
                    fs = FileSystem.get(new URI(meta.getUri()), conf, meta.getUser());
                }
            } catch (Throwable e) {
                throw new IllegalStateException(e.getMessage(), e);
            }

        }
        return fs;
    }

    @Override
    public void close() throws IOException {
        if (fs != null) {
            fs.close();
        }
    }

    @Override
    public IFile getFile(String path) {
        return new HdfsFile(this, path);
    }

    @Override
    public String getAbsolutePath(String path) {
        try {
            Path pfs = new Path(path);
            return getFileSystem().getFileStatus(pfs)
                    .getPath().toUri().getPath();
        } catch (Throwable e) {

        }
        return path;
    }

    @Override
    public boolean isDirectory(String path) {
        try {
            Path pfs = new Path(path);
            return getFileSystem().getFileStatus(pfs)
                    .isDirectory();
        } catch (Throwable e) {

        }
        return false;
    }

    @Override
    public boolean isFile(String path) {
        try {
            Path pfs = new Path(path);
            return getFileSystem().getFileStatus(pfs)
                    .isFile();
        } catch (Throwable e) {

        }
        return false;
    }

    @Override
    public boolean isExists(String path) {
        try {
            Path pfs = new Path(path);
            return getFileSystem().getFileStatus(pfs)
                    != null;
        } catch (Throwable e) {

        }
        return false;
    }

    @Override
    public List<IFile> listFiles(String path) {
        List<IFile> ret = new LinkedList<>();
        try {
            Path pfs = new Path(path);
            FileStatus[] files = getFileSystem().listStatus(pfs);
            for (FileStatus file : files) {
                ret.add(getFile(file.getPath().toUri().getPath()));
            }
        } catch (Throwable e) {

        }
        return ret;
    }

    @Override
    public void delete(String path) {
        try {
            Path pfs = new Path(path);
            getFileSystem().delete(pfs, false);
        } catch (Throwable e) {

        }
    }

    @Override
    public boolean isAppendable(String path) {
        return true;
    }

    @Override
    public InputStream getInputStream(String path) throws IOException {
        return getFileSystem().open(new Path(path));
    }

    @Override
    public OutputStream getOutputStream(String path) throws IOException {
        return getFileSystem().create(new Path(path), true);
    }

    @Override
    public OutputStream getAppendOutputStream(String path) throws IOException {
        return getFileSystem().append(new Path(path));
    }

    @Override
    public void mkdir(String path) {
        try {
            getFileSystem().mkdirs(new Path(path));
        } catch (Throwable e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void mkdirs(String path) {
        mkdir(path);
    }

    @Override
    public long length(String path) {
        try {
            Path pfs = new Path(path);
            return getFileSystem().getFileStatus(pfs).getLen();
        } catch (Throwable e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
