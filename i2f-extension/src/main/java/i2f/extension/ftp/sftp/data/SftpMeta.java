package i2f.extension.ftp.sftp.data;

import i2f.extension.ftp.data.FtpMeta;

import java.util.Properties;

/**
 * @author ltb
 * @date 2022/3/26 18:47
 * @desc
 */
public class SftpMeta extends FtpMeta implements ISftpMetaMeta {
    protected String privateKey;
    protected Properties config=new Properties();
    public SftpMeta(){

    }
    public SftpMeta setPrivateKey(String privateKey){
        this.privateKey=privateKey;
        return this;
    }
    @Override
    public String getPrivateKey() {
        return privateKey;
    }

    @Override
    public Properties getConfig() {
        return config;
    }
}
