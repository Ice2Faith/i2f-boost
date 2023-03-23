package i2f.extension.ftp.sftp.proxy.data;

import i2f.extension.ftp.sftp.basic.data.ISftpMeta;

import java.util.Properties;

/**
 * @author Ice2Faith
 * @date 2023/3/23 8:56
 * @desc
 */
public interface IProxySftpMeta extends ISftpMeta {
    /**
     * 代理机的占用端口
     */
    int getLocalPort();

    /**
     * 远程主机的连接配置
     */
    String getRemoteHost();

    int getRemotePort();

    String getRemoteUsername();

    String getRemotePassword();

    //秘钥验证
    String getRemotePrivateKey();

    //其他配置
    Properties getRemoteConfig();
}
