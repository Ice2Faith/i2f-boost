package i2f.springboot.exception;

import i2f.core.api.ApiResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
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
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.SQLTimeoutException;
import java.text.ParseException;
import java.time.DateTimeException;
import java.time.format.DateTimeParseException;
import java.util.IllegalFormatException;
import java.util.concurrent.TimeoutException;

/**
 * @author ltb
 * @date 2022/7/3 20:22
 * @desc
 */
@Slf4j
@RestControllerAdvice
public class JavaExceptionHandler {

    @ExceptionHandler(FileNotFoundException.class)
    public ApiResp<String> fileNotFoundEx(FileNotFoundException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"文件找不到");
    }

    @ExceptionHandler(IOException.class)
    public ApiResp<String> ioEx(IOException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"输入输出异常");
    }

    @ExceptionHandler(NullPointerException.class)
    public ApiResp<String> nullPointerEx(NullPointerException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"空指针异常");
    }

    @ExceptionHandler(ClassNotFoundException.class)
    public ApiResp<String> classNotFoundEx(ClassNotFoundException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"类丢失异常");
    }

    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ApiResp<String> indexOutOfBoundsEx(IndexOutOfBoundsException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"越界异常");
    }

    @ExceptionHandler(ConnectException.class)
    public ApiResp<String> connectEx(ConnectException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"数据库异常");
    }

    @ExceptionHandler(SQLException.class)
    public ApiResp<String> sqlEx(SQLException e){
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

    @ExceptionHandler(DateTimeException.class)
    public ApiResp<String> dateTimeEx(DateTimeException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"时间异常");
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

    @ExceptionHandler(ParseException.class)
    public ApiResp<String> parseEx(ParseException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"解析异常");
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

    @ExceptionHandler(ArithmeticException.class)
    public ApiResp<String> arithmeticEx(ArithmeticException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"算术异常");
    }

    @ExceptionHandler(NoSuchAlgorithmException.class)
    public ApiResp<String> arithmeticEx(NoSuchAlgorithmException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"算法缺失异常");
    }

    @ExceptionHandler(ClassCastException.class)
    public ApiResp<String> classCastEx(ClassCastException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"类转型异常");
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ApiResp<String> unsupportedOperationEx(UnsupportedOperationException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"不支持的操作异常");
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

    @ExceptionHandler(TimeoutException.class)
    public ApiResp<String> timeoutEx(TimeoutException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"超时异常");
    }

    @ExceptionHandler(EOFException.class)
    public ApiResp<String> eofEx(EOFException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"文件终结异常");
    }

    @ExceptionHandler(SecurityException.class)
    public ApiResp<String> securityEx(SecurityException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"安全异常");
    }

    @ExceptionHandler(IllegalAccessException.class)
    public ApiResp<String> illegalAccessEx(IllegalAccessException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"非法访问异常");
    }

    @ExceptionHandler(IllegalStateException.class)
    public ApiResp<String> illegalStateEx(IllegalStateException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"非法状态异常");
    }

    @ExceptionHandler(IllegalFormatException.class)
    public ApiResp<String> illegalFormatEx(IllegalFormatException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"非法格式化异常");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResp<String> illegalArgumentEx(IllegalArgumentException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"非法参数异常");
    }

    @ExceptionHandler(IllegalBlockSizeException.class)
    public ApiResp<String> illegalBlockSizeEx(IllegalBlockSizeException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"非法块大小异常");
    }

    @ExceptionHandler(InterruptedException.class)
    public ApiResp<String> interruptedEx(InterruptedException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"中断异常");
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

    @ExceptionHandler(BadPaddingException.class)
    public ApiResp<String> badPaddingEx(BadPaddingException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"错误填充异常");
    }

    @ExceptionHandler(NotSerializableException.class)
    public ApiResp<String> notSerializableEx(NotSerializableException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"不可序列化异常");
    }

    @ExceptionHandler(CloneNotSupportedException.class)
    public ApiResp<String> cloneNotSupportedEx(CloneNotSupportedException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"不可克隆异常");
    }

    @ExceptionHandler(IllegalThreadStateException.class)
    public ApiResp<String> illegalThreadStateEx(IllegalThreadStateException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"非法线程状态异常");
    }

    @ExceptionHandler(SyncFailedException.class)
    public ApiResp<String> illegalThreadStateEx(SyncFailedException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"同步失败异常");
    }

    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    public ApiResp<String> arrayIndexOutOfBoundsEx(ArrayIndexOutOfBoundsException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"数组越界异常");
    }

    @ExceptionHandler(RuntimeException.class)
    public ApiResp<String> runtimeEx(RuntimeException e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"系统运行异常");
    }

    @ExceptionHandler(Exception.class)
    public ApiResp<String> ex(Exception e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"系统异常");
    }

    @ExceptionHandler(Throwable.class)
    public ApiResp<String> thr(Throwable e){
        log.error(e.getClass().getName(),e);
        return ApiResp.error(500,"抛出异常");
    }
}
