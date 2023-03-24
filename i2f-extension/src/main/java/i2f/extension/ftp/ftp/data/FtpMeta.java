package i2f.extension.ftp.ftp.data;


/**
 * @author ltb
 * @date 2022/3/26 18:40
 * @desc
 */

public class FtpMeta implements IFtpMeta {
    protected String host;
    protected int port;
    protected String username;
    protected String password;

    public FtpMeta() {

    }

    public FtpMeta setHost(String host) {
        this.host = host;
        return this;
    }
    public FtpMeta setPort(int port){
        this.port=port;
        return this;
    }
    public FtpMeta setUsername(String username){
        this.username=username;
        return this;
    }
    public FtpMeta setPassword(String password){
        this.password=password;
        return this;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
