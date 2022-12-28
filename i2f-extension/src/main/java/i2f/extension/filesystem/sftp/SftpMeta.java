package i2f.extension.filesystem.sftp;

import lombok.Data;

import java.util.Properties;

/**
 * @author ltb
 * @date 2022/3/26 18:47
 * @desc
 */
@Data
public class SftpMeta {
    protected String host;
    protected int port = 22;
    protected String username;
    protected String password;

    protected String privateKey;
    protected Properties config = new Properties();

}
