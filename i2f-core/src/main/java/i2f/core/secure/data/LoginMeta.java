package i2f.core.secure.data;

import i2f.core.secure.ILoginMeta;

/**
 * @author ltb
 * @date 2022/3/26 18:52
 * @desc
 */
public class LoginMeta implements ILoginMeta {
    protected String username;
    protected String password;
    public LoginMeta(){

    }
    public LoginMeta setUsername(String username){
        this.username=username;
        return this;
    }
    public LoginMeta setPassword(String password){
        this.password=password;
        return this;
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
