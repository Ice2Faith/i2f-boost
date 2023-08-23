package i2f.springboot.exception.handler.ext;

import i2f.core.std.api.ApiResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.web.*;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @author ltb
 * @date 2022/7/3 20:22
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.exception.springmvc.enable:true}")
@Order(SpringMvcExceptionHandler.ORDER)
@ConditionalOnClass(DispatcherServlet.class)
@Slf4j
@RestControllerAdvice
public class SpringMvcExceptionHandler {
    public static final int ORDER = 5999;

    @ExceptionHandler(NoHandlerFoundException.class)
    public ApiResp<String> noHandlerFoundEx(NoHandlerFoundException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(404, "未找到资源");
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ApiResp<String> httpMediaTypeNotSupportedEx(HttpMediaTypeNotSupportedException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(415, "请求媒体类型不受支持");
    }


    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ApiResp<String> httpMediaTypeNotAcceptableEx(HttpMediaTypeNotAcceptableException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(415, "请求媒体类型不接受");
    }

    @ExceptionHandler(HttpMediaTypeException.class)
    public ApiResp<String> httpMediaTypeEx(HttpMediaTypeException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(415, "请求媒体类型异常");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResp<String> httpRequestMethodNotSupportedEx(HttpRequestMethodNotSupportedException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(405, "请求方法不支持");
    }

    @ExceptionHandler(HttpSessionRequiredException.class)
    public ApiResp<String> httpSessionRequiredEx(HttpSessionRequiredException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(400, "请求会话丢失");
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ApiResp<String> missingServletRequestPartEx(MissingServletRequestPartException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(400, "请求分块丢失");
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ApiResp<String> missingRequestHeaderEx(MissingRequestHeaderException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(400, "请求头丢失");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiResp<String> missingServletRequestParameterEx(MissingServletRequestParameterException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(400, "请求参数丢失");
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ApiResp<String> missingPathVariableEx(MissingPathVariableException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(400, "请求路径参数丢失");
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ApiResp<String> missingRequestCookieEx(MissingRequestCookieException e) {
        log.error(e.getClass().getName(), e);
        return ApiResp.error(400, "请求Cookie丢失");
    }
}
