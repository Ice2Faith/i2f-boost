package i2f.core.network.net.http.rest.annotations;

import i2f.core.annotations.remark.Author;
import i2f.core.network.net.http.impl.HttpUrlConnectProcessor;
import i2f.core.network.net.http.interfaces.IHttpProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ltb
 * @date 2022/5/18 9:14
 * @desc
 */
@Author("i2f")
@Target({
        ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface RestClient {
    String url() default "";
    String path() default "";
    Class<? extends IHttpProcessor> http() default HttpUrlConnectProcessor.class;
}
