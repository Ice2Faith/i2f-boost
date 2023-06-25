package i2f.extension.ftp.sftp.proxy;

import com.jcraft.jsch.*;
import i2f.core.io.file.FileUtil;
import i2f.extension.ftp.sftp.proxy.data.IProxySftpMeta;

import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

/**
 * @author ltb
 * @date 2021/11/1
 */
public class ProxySftpUtil implements Closeable {
    private IProxySftpMeta meta;
    private Session remoteSession;
    private Session session;
    private ChannelSftp channelSftp;

    public ProxySftpUtil(IProxySftpMeta meta) {
        setMeta(meta);
    }

    public ProxySftpUtil setMeta(IProxySftpMeta meta) {
        this.meta = meta;
        return this;
    }

    public ProxySftpUtil login() throws JSchException {
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


        session.setPortForwardingL(meta.getLocalPort(), meta.getRemoteHost(), meta.getRemotePort());

        remoteSession = jsch.getSession(meta.getRemoteUsername(), "127.0.0.1", meta.getLocalPort());
        if (meta.getRemotePassword() != null) {
            remoteSession.setPassword(meta.getRemotePassword());
        }

        Properties remoteConfig = new Properties();
        remoteConfig.put("StrictHostKeyChecking", "no");

        Properties remoteCustConfig = meta.getRemoteConfig();
        for (Map.Entry<Object, Object> item : remoteCustConfig.entrySet()) {
            remoteConfig.put(item.getKey(), item.getValue());
        }

        remoteSession.setConfig(remoteConfig);
        remoteSession.connect();

        Channel remoteChannel = remoteSession.openChannel("sftp");
        remoteChannel.connect();

        channelSftp = (ChannelSftp) remoteChannel;

        return this;
    }

    public ProxySftpUtil logout() {
        if (channelSftp != null) {
            if (channelSftp.isConnected()) {
                channelSftp.disconnect();
            }
        }
        if (remoteSession != null) {
            if (remoteSession.isConnected()) {
                remoteSession.disconnect();
            }
        }
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
            }
        }
        return this;
    }

    public Session getSession() {
        return session;
    }

    public Session getRemoteSession() {
        return remoteSession;
    }

    public ChannelShell getChannelShell() throws JSchException {
        Channel shell = remoteSession.openChannel("shell");
        shell.connect();
        return (ChannelShell) shell;
    }

    public ProxySftpUtil mkdirs(String serverPath) throws SftpException {
        serverPath = serverPath.replaceAll("\\\\", "/");
        String[] dirs = serverPath.split("\\/");
        StringBuilder builder = new StringBuilder();
        for (String item : dirs) {
            if ("".equals(item)) {
                continue;
            }
            builder.append("/");
            builder.append(item);
            String ppath = builder.toString();
            if (!existDir(ppath)) {
                channelSftp.mkdir(ppath);
            }
        }
        return this;
    }

    public boolean existDir(String path) {
        try {
            String bak = channelSftp.pwd();
            channelSftp.cd(path);
            channelSftp.cd(bak);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public ProxySftpUtil upload(String serverPath, String serverFileName, InputStream is) throws SftpException {
        mkdirs(serverPath);
        channelSftp.cd(serverPath);
        channelSftp.put(is, serverFileName);
        return this;
    }

    public ProxySftpUtil upload(String serverPath, File localFile) throws SftpException, IOException {
        String fileName = localFile.getName();
        InputStream is = new FileInputStream(localFile);
        upload(serverPath, fileName, is);
        is.close();
        return this;
    }

    public OutputStream download(String serverPath, String serverFileName, OutputStream os) throws SftpException {
        if (serverPath != null && !"".equals(serverPath)) {
            channelSftp.cd(serverPath);
        }
        channelSftp.get(serverFileName, os);
        return os;
    }

    public File download(String serverPath, String serverFileName, File localFile) throws IOException, SftpException {
        FileUtil.useParentDir(localFile);
        OutputStream os = new FileOutputStream(localFile);
        download(serverPath, serverFileName, os);
        os.close();
        return localFile;
    }

    public File download2Dir(String serverPath, String serverFileName, File localDir) throws SftpException {
        FileUtil.useDir(localDir);
        File localFile = new File(localDir, serverFileName);
        try {
            OutputStream os = new FileOutputStream(localFile);
            download(serverPath, serverFileName, os);
            os.close();
        } catch (IOException e) {
            throw new SftpException(0, e.getMessage(), e);
        }
        return localFile;
    }

    public ProxySftpUtil delete(String serverPath, String serverFileName) throws SftpException {
        if (serverPath != null && !"".equals(serverPath)) {
            channelSftp.cd(serverPath);
        }
        channelSftp.rm(serverFileName);
        return this;
    }

    public ProxySftpUtil deleteDir(String serverPath) throws SftpException {
        channelSftp.rmdir(serverPath);
        return this;
    }

    public Vector<?> listFiles(String serverPath) throws SftpException {
        return channelSftp.ls(serverPath);
    }

    public String exec(String command) throws IOException {
        ChannelShell channel = null;
        PrintWriter printWriter = null;
        BufferedReader input = null;
        StringBuilder builder = new StringBuilder();
        try {
            channel = getChannelShell();

            InputStreamReader inputStreamReader = new InputStreamReader(channel.getInputStream());
            input = new BufferedReader(inputStreamReader);

            printWriter = new PrintWriter(channel.getOutputStream());
            printWriter.println(command);
            printWriter.println("exit");
            printWriter.flush();
            String line = null;
            while ((line = input.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
        } catch (JSchException e) {
            throw new IOException(e.getMessage(), e);
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
            if (input != null) {
                input.close();
            }
            if (channel != null) {
                channel.disconnect();
            }
        }
        return builder.toString();
    }

    @Override
    public void close() throws IOException {
        logout();
    }
}
