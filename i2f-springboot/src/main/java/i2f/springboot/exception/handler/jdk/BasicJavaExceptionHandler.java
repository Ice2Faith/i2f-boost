package i2f.springboot.exception.handler.jdk;

import i2f.core.std.api.ApiResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLDataException;
import java.sql.SQLSyntaxErrorException;
import java.sql.SQLTimeoutException;
import java.time.format.DateTimeParseException;
import java.util.IllegalFormatException;

/**
 * @author ltb
 * @date 2022/7/3 20:22
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.exception.java.enable:true}")
@Order(BasicJavaExceptionHandler.ORDER)
@Slf4j
@RestControllerAdvice
public class BasicJavaExceptionHandler {
    public static final int ORDER = 8979;

    @ExceptionHandler(FileNotFoundException.class)
    public ApiResp<String> fileNotFoundEx(FileNotFoundException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(500, "文件找不到");
    }

    @ExceptionHandler(ClassNotFoundException.class)
    public ApiResp<String> classNotFoundEx(ClassNotFoundException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(500,"类丢失异常");
    }

    @ExceptionHandler(ConnectException.class)
    public ApiResp<String> connectEx(ConnectException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"数据库异常");
    }

    @ExceptionHandler(SQLDataException.class)
    public ApiResp<String> sqlDataEx(SQLDataException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"数据库数据异常");
    }

    @ExceptionHandler(SQLTimeoutException.class)
    public ApiResp<String> sqlTimeoutEx(SQLTimeoutException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"数据库超时异常");
    }

    @ExceptionHandler(SQLSyntaxErrorException.class)
    public ApiResp<String> sqlSyntaxErrorEx(SQLSyntaxErrorException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"数据库语法错误异常");
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ApiResp<String> dateTimeParseEx(DateTimeParseException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"时间解析异常");
    }

    @ExceptionHandler(NumberFormatException.class)
    public ApiResp<String> numberFormatEx(NumberFormatException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"数值格式异常");
    }

    @ExceptionHandler(MalformedURLException.class)
    public ApiResp<String> malformedUrlEx(MalformedURLException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"不支持的超链接异常");
    }

    @ExceptionHandler(RemoteException.class)
    public ApiResp<String> remoteEx(RemoteException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"远程异常");
    }

    @ExceptionHandler(InstantiationException.class)
    public ApiResp<String> instantiationEx(InstantiationException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"实例化异常");
    }

    @ExceptionHandler(ReflectiveOperationException.class)
    public ApiResp<String> reflectiveOperationEx(ReflectiveOperationException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"反射异常");
    }

    @ExceptionHandler(InvocationTargetException.class)
    public ApiResp<String> invocationTargetEx(InvocationTargetException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"调用异常");
    }

    @ExceptionHandler(NoSuchFieldException.class)
    public ApiResp<String> noSuchFieldEx(NoSuchFieldException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"无属性异常");
    }

    @ExceptionHandler(NoSuchMethodException.class)
    public ApiResp<String> noSuchMethodEx(NoSuchMethodException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"无方法异常");
    }

    @ExceptionHandler(NoSuchAlgorithmException.class)
    public ApiResp<String> arithmeticEx(NoSuchAlgorithmException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"算法缺失异常");
    }

    @ExceptionHandler(UnsupportedEncodingException.class)
    public ApiResp<String> unsupportedEncodingEx(UnsupportedEncodingException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"不支持的编码异常");
    }

    @ExceptionHandler(SocketException.class)
    public ApiResp<String> socketEx(SocketException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"套接字异常");
    }

    @ExceptionHandler(SocketTimeoutException.class)
    public ApiResp<String> socketTimeoutEx(SocketTimeoutException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"套接字超时异常");
    }

    @ExceptionHandler(EOFException.class)
    public ApiResp<String> eofEx(EOFException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"文件终结异常");
    }

    @ExceptionHandler(IllegalAccessException.class)
    public ApiResp<String> illegalAccessEx(IllegalAccessException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"非法访问异常");
    }

    @ExceptionHandler(IllegalFormatException.class)
    public ApiResp<String> illegalFormatEx(IllegalFormatException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"非法格式化异常");
    }

    @ExceptionHandler(InvalidClassException.class)
    public ApiResp<String> invalidClassEx(InvalidClassException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"非法类异常");
    }

    @ExceptionHandler(InvalidKeyException.class)
    public ApiResp<String> invalidKeyEx(InvalidKeyException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"非法秘钥异常");
    }

    @ExceptionHandler(NotSerializableException.class)
    public ApiResp<String> notSerializableEx(NotSerializableException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"不可序列化异常");
    }

    @ExceptionHandler(IllegalThreadStateException.class)
    public ApiResp<String> illegalThreadStateEx(IllegalThreadStateException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"非法线程状态异常");
    }

    @ExceptionHandler(SyncFailedException.class)
    public ApiResp<String> illegalThreadStateEx(SyncFailedException e){
        log.error(e.getClass().getName(), e);
        return ApiResp.error(500, "同步失败异常");
    }

    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    public ApiResp<String> arrayIndexOutOfBoundsEx(ArrayIndexOutOfBoundsException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(500, "数组越界异常");
    }

}
