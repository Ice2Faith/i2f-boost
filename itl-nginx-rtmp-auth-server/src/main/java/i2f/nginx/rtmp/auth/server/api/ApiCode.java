package i2f.nginx.rtmp.auth.server.api;

/**
 * @author Ice2Faith
 * @date 2023/1/14 20:47
 * @desc
 */
public interface ApiCode {
    int SUCCESS = 200;
    int ERROR = 0;
    int NO_LOGIN = 400;
    int NO_AUTH = 401;
    int UNKNOWN = 402;
    int NOT_FOUND = 404;
    int SYS_EXCEPTION = 500;
}
