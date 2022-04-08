package i2f.extension.ftp.data;

import i2f.core.net.INetAddressMeta;
import i2f.core.secure.ILoginMeta;
import i2f.extension.ftp.IFtpMetaMeta;

/**
 * @author ltb
 * @date 2022/3/26 18:40
 * @desc
 */

public class FtpMeta implements IFtpMetaMeta {
    protected String host;
    protected int port;
    protected String username;
    protected String password;
    public FtpMeta(){

    }
    public FtpMeta setHost(String host){
        this.host=host;
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
    public FtpMeta setNetAddress(INetAddressMeta addr){
        this.host= addr.getHost();
        this.port= addr.getPort();
        return this;
    }
    public FtpMeta setLogin(ILoginMeta login){
        this.username= login.getUsername();
        this.password=login.getPassword();
        return this;
    }
    @Override
    public String getHost() {
        return null;
    }

    @Override
    public int getPort() {
        return 0;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }
}
