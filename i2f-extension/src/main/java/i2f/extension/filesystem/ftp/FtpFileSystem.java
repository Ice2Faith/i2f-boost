package i2f.extension.filesystem.ftp;

import i2f.core.io.filesystem.IFile;
import i2f.core.io.filesystem.abs.AbsFileSystem;
import i2f.core.type.tuple.impl.Tuple2;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

public class FtpFileSystem extends AbsFileSystem implements Closeable {
    private FtpMeta meta;
    private FTPClient client;
    private boolean enableNewClient = true;

    public FtpFileSystem(FtpMeta meta, boolean enableNewClient) {
        this.meta = meta;
        this.enableNewClient = enableNewClient;
        getClient();
    }

    public FtpFileSystem(FtpMeta meta) {
        this.meta = meta;
        getClient();
    }


    public FTPClient getClient() {
        if (enableNewClient || client == null || !client.isConnected() || !client.isAvailable()) {
            try {
                try {
                    if (client != null) {
                        client.logout();
                        client.disconnect();
                    }
                } catch (Exception e) {

                }
                client = new FTPClient();

                client.setControlEncoding("UTF-8");
                client.connect(meta.getHost(), meta.getPort());

                client.login(meta.getUsername(), meta.getPassword());
//                client.enterLocalPassiveMode();
//                client.execPBSZ(0);
//                client.execPROT("P");
                client.type(FTP.BINARY_FILE_TYPE);
                int reply = client.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    client.logout();
                    client.disconnect();
                    client = null;
                    throw new IllegalStateException("ftp not positive completion");
                }
            } catch (Throwable e) {
                if (!(e instanceof RuntimeException)) {
                    e = new IllegalStateException(e.getMessage(), e);
                }
                throw (RuntimeException) e;
            }
        }
        return client;
    }

    @Override
    public void close() throws IOException {
        if (client != null) {
            client.logout();
            if (client.isConnected()) {
                client.disconnect();
            }
        }
    }

    public Tuple2<String, String> getDirAndName(String path) {
        String dirName = null;
        String fileName = null;
        if (path == null) {
            path = "";
        }
        if (path.endsWith(pathSeparator())) {
            path = path.substring(0, path.length() - pathSeparator().length());
        }
        int idx = path.lastIndexOf(pathSeparator());
        if (idx > 0) {
            dirName = path.substring(0, idx);
            fileName = path.substring(idx + 1);
        } else {
            if (path.startsWith(pathSeparator())) {
                dirName = pathSeparator();
                fileName = path.substring(pathSeparator().length());
            } else {
                dirName = ".";
                fileName = path;
            }
        }

        return new Tuple2<>(dirName, fileName);
    }

    @Override
    public IFile getFile(String path) {
        return new FtpFile(this, path);
    }

    @Override
    public String getAbsolutePath(String path) {
        return path;
    }

    @Override
    public boolean isDirectory(String path) {
        Tuple2<String, String> pair = getDirAndName(path);
        try {
            FTPClient client = getClient();
            client.enterLocalPassiveMode();
            FTPFile[] files = client.listFiles(pair.t1);
            for (FTPFile file : files) {
                if (file.isDirectory()) {
                    if (file.getName().equals(pair.t2)) {
                        return true;
                    }
                }
            }
        } catch (Throwable e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean isFile(String path) {
        Tuple2<String, String> pair = getDirAndName(path);
        try {
            FTPClient client = getClient();
            client.enterLocalPassiveMode();
            FTPFile[] files = client.listFiles(pair.t1);
            for (FTPFile file : files) {
                if (file.isFile()) {
                    if (file.getName().equals(pair.t2)) {
                        return true;
                    }
                }
            }
        } catch (Throwable e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return false;
    }


    @Override
    public boolean isExists(String path) {
        Tuple2<String, String> pair = getDirAndName(path);
        try {
            FTPClient client = getClient();
            client.enterLocalPassiveMode();
            FTPFile[] files = client.listFiles(pair.t1);
            for (FTPFile file : files) {
                if (file.getName().equals(pair.t2)) {
                    return true;
                }
            }
        } catch (Throwable e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public List<IFile> listFiles(String path) {
        List<IFile> ret = new LinkedList<>();
        try {
            FTPClient client = getClient();
            client.enterLocalPassiveMode();
            FTPFile[] files = client.listFiles(path);
            for (FTPFile file : files) {
                ret.add(getFile(combinePath(path, file.getName())));
            }
        } catch (Throwable e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return ret;
    }

    @Override
    public void delete(String path) {
        Tuple2<String, String> pair = getDirAndName(path);
        try {
            FTPClient client = getClient();
            client.enterLocalPassiveMode();
            FTPFile[] files = client.listFiles(pair.t1);
            for (FTPFile file : files) {
                if (file.getName().equals(pair.t2)) {
                    if (file.isFile()) {
                        client.deleteFile(path);
                    }
                    if (file.isDirectory()) {
                        client.removeDirectory(path);
                    }
                    break;
                }
            }
        } catch (Throwable e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public boolean isAppendable(String path) {
        return isFile(path);
    }

    @Override
    public InputStream getInputStream(String path) throws IOException {
        return getClient().retrieveFileStream(path);
    }

    @Override
    public OutputStream getOutputStream(String path) throws IOException {
        return getClient().storeFileStream(path);
    }

    @Override
    public OutputStream getAppendOutputStream(String path) throws IOException {
        return getClient().appendFileStream(path);
    }

    @Override
    public void mkdir(String path) {
        try {
            getClient().makeDirectory(path);
        } catch (Throwable e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public long length(String path) {
        Tuple2<String, String> pair = getDirAndName(path);
        try {
            FTPClient client = getClient();
            client.enterLocalPassiveMode();
            FTPFile[] files = client.listFiles(pair.t1);
            for (FTPFile file : files) {
                if (file.getName().equals(pair.t2)) {
                    return file.getSize();
                }
            }
        } catch (Throwable e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        throw new IllegalStateException("file not found.");
    }
}
