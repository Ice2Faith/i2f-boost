package i2f.extension.ftp.sftp.basic.data;


import i2f.extension.ftp.ftp.data.IFtpMeta;

import java.util.Properties;

/**
 * @author ltb
 * @date 2021/11/1
 */
public interface ISftpMeta extends IFtpMeta {
    //秘钥验证
    String getPrivateKey();

    //其他配置
    Properties getConfig();
}
