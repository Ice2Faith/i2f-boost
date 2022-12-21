package i2f.extension.netty.tcp.test.rpc;

import lombok.ToString;

@ToString
public class User {
    public String username;
    public String password;
    public int age;
    public Object attr;
}
