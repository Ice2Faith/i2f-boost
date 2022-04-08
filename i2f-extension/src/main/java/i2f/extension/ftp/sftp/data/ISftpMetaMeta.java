package i2f.extension.ftp.sftp.data;

import i2f.extension.ftp.IFtpMetaMeta;

import java.util.Properties;

/**
 * @author ltb
 * @date 2021/11/1
 */
public interface ISftpMetaMeta extends IFtpMetaMeta {
    //秘钥验证
    String getPrivateKey();

    //其他配置
    Properties getConfig();
}
