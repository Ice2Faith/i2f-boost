package i2f.extension.ftp.sftp.proxy.data;

import i2f.extension.ftp.sftp.basic.data.SftpMeta;

import java.util.Properties;

/**
 * @author Ice2Faith
 * @date 2023/3/23 8:59
 * @desc
 */
public class ProxySftpMeta extends SftpMeta implements IProxySftpMeta {
    private int localPort;
    private String remoteHost;
    private int remotePort;
    private String remoteUsername;
    private String remotePassword;
    private String remotePrivateKey;
    private Properties remoteConfig = new Properties();

    @Override
    public int getLocalPort() {
        return localPort;
    }

    @Override
    public String getRemoteHost() {
        return remoteHost;
    }

    @Override
    public int getRemotePort() {
        return remotePort;
    }

    @Override
    public String getRemoteUsername() {
        return remoteUsername;
    }

    @Override
    public String getRemotePassword() {
        return remotePassword;
    }

    @Override
    public String getRemotePrivateKey() {
        return remotePrivateKey;
    }

    @Override
    public Properties getRemoteConfig() {
        return remoteConfig;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public void setRemoteUsername(String remoteUsername) {
        this.remoteUsername = remoteUsername;
    }

    public void setRemotePassword(String remotePassword) {
        this.remotePassword = remotePassword;
    }

    public void setRemotePrivateKey(String remotePrivateKey) {
        this.remotePrivateKey = remotePrivateKey;
    }

    public void setRemoteConfig(Properties remoteConfig) {
        this.remoteConfig = remoteConfig;
    }
}
