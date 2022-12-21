package i2f.extension.netty.tcp.test.rpc;

import java.util.HashMap;
import java.util.Map;

public class RpcServiceImpl implements IRpcService {
    @Override
    public boolean login(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("username or password not allow null");
        }
        if ("admin".equals(username) && "123456".equals(password)) {
            return true;
        }
        return false;
    }

    @Override
    public User getUserByName(String username) {
        User user = new User();
        user.username = username;
        user.password = "123456";
        user.age = 23;
        Map<String, Object> map = new HashMap<>();
        map.put("country", "cn");
        map.put("sex", "man");
        map.put("intro", "fight!");
        user.attr = map;
        return user;
    }

    @Override
    public User updateUser(User user) {
        user.age = 24;
        return user;
    }

    @Override
    public User exceptionUser(User user) {
        throw new IllegalArgumentException("user is illegal");
    }
}
