package i2f.extension.filesystem.ftp;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/3/26 18:40
 * @desc
 */
@Data
@NoArgsConstructor
public class FtpMeta {
    protected String host;
    protected int port = 21;
    protected String username;
    protected String password;
}
