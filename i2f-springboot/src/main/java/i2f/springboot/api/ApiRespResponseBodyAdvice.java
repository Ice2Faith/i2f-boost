package i2f.springboot.api;

import i2f.core.reflection.reflect.Reflects;
import i2f.core.std.api.ApiResp;
import i2f.spring.serialize.jackson.JacksonJsonSerializer;
import i2f.springboot.api.annotations.ApiRespCtl;
import i2f.springboot.secure.consts.SecureConsts;
import i2f.springboot.secure.core.SecureTransfer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.MethodParameter;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;


@ConditionalOnExpression("${i2f.springboot.config.api-resp.response-advice.enable:true}")
@Slf4j
@ControllerAdvice
public class ApiRespResponseBodyAdvice implements ResponseBodyAdvice<Object>, InitializingBean {

    String FLAG_ENABLE = "true";

    String FLAG_DISABLE = "false";

    // 是否是String返回值类型标记
    String STRING_RETURN_HEADER = "SECURE_RETURN_STRING";

    @Autowired
    private JacksonJsonSerializer serializer;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("ApiRespResponseBodyAdvice config done.");
    }

    @Autowired
    private ApiRespConfig apiRespConfig;

    public static ApiRespCtl getApiRespAnnotation(Member member) {
        return Reflects.annotations(member, ApiRespCtl.class, true, false);
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        Method method = methodParameter.getMethod();
        ApiRespCtl ann = getApiRespAnnotation(method);
        if (ann != null) {
            return ann.value();
        }
        return apiRespConfig.isGlobal();
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (o == null) {
            return o;
        }

        boolean isStringReturn = (o instanceof String);
        //如果返回值是LinkedHashMap，那么有可能是发生了错误，类型为LinkedHashMap<String,Object>
        //其中最常见的就是404页面找不到，在这里是能够截获的
        if (o instanceof LinkedHashMap) {
            LinkedHashMap<?,?> map = (LinkedHashMap<?,?>) o;
            Object status = map.get("status");
            if (status != null && status instanceof Integer && ((Integer) status) == 404) {
                log.info(status + ":" + status.getClass());
                o = ApiResp.error(404, "404 bad request found").data(o);
            }
        }

        // 对不是标准返回类型的进行包装
        if (!(o instanceof ApiResp)) {
            o = ApiResp.success(o);
        }


        // 如果封装之后，不是String类型，则表示不再需要对String类型特殊处理
        // 后续如果使用过滤器，则不再需要针对String类型做特殊处理
        if (isStringReturn) {
            ServletServerHttpResponse sresp = (ServletServerHttpResponse) serverHttpResponse;
            HttpServletResponse resp = sresp.getServletResponse();
            resp.setHeader(STRING_RETURN_HEADER, FLAG_DISABLE);
        }
        return returnObj(o, isStringReturn);
    }

    protected Object returnObj(Object obj, boolean isReturnString) {
        // 如果原始controller的返回值类型为string，则在这里处理之后也需要返回string，否则将会出错
        if (isReturnString) {
            return serializer.serialize(obj);
        }
        return obj;
    }
}