package i2f.extension.filesystem.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import i2f.core.filesystem.IFile;
import i2f.core.filesystem.abs.AbsFileSystem;
import i2f.core.tuple.impl.Tuple2;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class SftpFileSystem extends AbsFileSystem implements Closeable {
    private SftpMeta meta;
    private Session session;
    private ChannelSftp channel;

    public SftpFileSystem(SftpMeta meta) {
        this.meta = meta;
        getChannel();
    }

    public ChannelSftp getChannel() {
        if (channel == null || !channel.isConnected() || !channel.isClosed()) {
            try {
                if (channel != null) {
                    if (channel.isConnected()) {
                        channel.disconnect();
                    }
                }
                if (session != null) {
                    if (session.isConnected()) {
                        session.disconnect();
                    }
                }
            } catch (Throwable e) {

            }
            try {
                JSch jsch = new JSch();
                if (meta.getPrivateKey() != null) {
                    jsch.addIdentity(meta.getPrivateKey());
                }

                session = jsch.getSession(meta.getUsername(), meta.getHost(), meta.getPort());

                if (meta.getPassword() != null) {
                    session.setPassword(meta.getPassword());
                }

                Properties config = new Properties();
                config.put("StrictHostKeyChecking", "no");

                Properties cusConfig = meta.getConfig();
                for (Map.Entry<Object, Object> item : cusConfig.entrySet()) {
                    config.put(item.getKey(), item.getValue());
                }

                session.setConfig(config);
                session.connect();

                channel = (ChannelSftp) session.openChannel("sftp");
                channel.connect();

            } catch (Throwable e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
        return channel;
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
    public void close() throws IOException {
        if (channel != null) {
            if (channel.isConnected()) {
                channel.disconnect();
            }
        }
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
            }
        }
    }

    @Override
    public IFile getFile(String path) {
        return new SftpFile(this, path);
    }

    @Override
    public String getAbsolutePath(String path) {
        try {
            return getChannel().realpath(path);
        } catch (Exception e) {

        }
        return path;
    }

    @Override
    public boolean isDirectory(String path) {
        try {
            ChannelSftp channel = getChannel();
            String bak = channel.pwd();
            channel.cd(path);
            channel.cd(bak);
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    @Override
    public boolean isFile(String path) {
        try {
            ChannelSftp channel = getChannel();
            InputStream is = channel.get(path);
            if (is == null) {
                return false;
            }
            is.close();
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    @Override
    public boolean isExists(String path) {
        Tuple2<String, String> pair = getDirAndName(path);
        try {
            ChannelSftp channel = getChannel();
            Vector vec = channel.ls(pair.t1);
            for (Object obj : vec) {
                ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) obj;
                String name = entry.getFilename();
                if (name.equals(pair.t2)) {
                    return true;
                }
            }
        } catch (Exception e) {

        }
        return false;
    }

    @Override
    public List<IFile> listFiles(String path) {
        Tuple2<String, String> pair = getDirAndName(path);
        List<IFile> ret = new LinkedList<>();
        try {
            ChannelSftp channel = getChannel();
            Vector vec = channel.ls(pair.t1);
            for (Object obj : vec) {
                ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) obj;
                String name = entry.getFilename();
                ret.add(getFile(combinePath(pair.t1, name)));
            }
        } catch (Exception e) {

        }
        return ret;
    }

    @Override
    public void delete(String path) {
        try {
            ChannelSftp channel = getChannel();
            if (isFile(path)) {
                channel.rm(path);
            } else if (isDirectory(path)) {
                channel.rmdir(path);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public boolean isAppendable(String path) {
        return false;
    }

    @Override
    public InputStream getInputStream(String path) throws IOException {
        try {
            return getChannel().get(path);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    @Override
    public OutputStream getOutputStream(String path) throws IOException {
        try {
            return getChannel().put(path);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    @Override
    public OutputStream getAppendOutputStream(String path) throws IOException {
        throw new UnsupportedOperationException("sftp not support appendable stream");
    }

    @Override
    public void mkdir(String path) {
        try {
            getChannel().mkdir(path);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public long length(String path) {
        try {
            SftpATTRS stat = getChannel().lstat(path);
            return stat.getSize();
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
