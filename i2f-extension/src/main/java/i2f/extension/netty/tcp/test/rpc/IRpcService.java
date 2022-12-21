package i2f.extension.netty.tcp.test.rpc;

public interface IRpcService {
    boolean login(String username, String password);

    User getUserByName(String username);

    User updateUser(User user);

    User exceptionUser(User user);
}
