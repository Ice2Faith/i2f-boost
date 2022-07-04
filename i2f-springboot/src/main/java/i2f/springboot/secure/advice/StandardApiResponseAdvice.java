package i2f.springboot.secure.advice;


import i2f.core.api.ApiResp;
import i2f.spring.jackson.JacksonJsonProcessor;
import i2f.springboot.secure.annotation.StandardApiResp;
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
import java.util.LinkedHashMap;

@ConditionalOnExpression("${i2f.springboot.config.secure.response-advice.enable:true}")
@Slf4j
@ControllerAdvice
public class StandardApiResponseAdvice implements ResponseBodyAdvice<Object>, InitializingBean {

    @Autowired
    private JacksonJsonProcessor processor;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("ResponseBodyEncryptAdvice config done.");
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        boolean isStringReturn=(o instanceof String);
        if(methodParameter.getMethod().isAnnotationPresent(StandardApiResp.class)){
            //如果返回值是LinkedHashMap，那么有可能是发生了错误，类型为LinkedHashMap<String,Object>
            //其中最常见的就是404页面找不到，在这里是能够截获的
            if(o instanceof LinkedHashMap){
                LinkedHashMap map=(LinkedHashMap) o;
                Object status=map.get("status");
                if(status!=null && status instanceof Integer && ((Integer) status)==404){
                    System.out.println(status+":"+status.getClass());
                    o= ApiResp.error(404,"404 bad request found").data(o);
                }
            }
            // 对不是标准返回类型的进行包装
            if(!(o instanceof ApiResp)) {
                o = ApiResp.success(o);
            }
        }
        if(o==null){
            return o;
        }
        // 如果封装之后，不是String类型，则表示不再需要对String类型特殊处理
        // 后续如果使用过滤器，则不再需要针对String类型做特殊处理
        if(isStringReturn && !(o instanceof String)){
            ServletServerHttpResponse sresp=(ServletServerHttpResponse)serverHttpResponse;
            HttpServletResponse resp = sresp.getServletResponse();
            resp.setHeader(SecureTransfer.STRING_RETURN_HEADER,SecureTransfer.FLAG_DISABLE);
        }
        return returnObj(o,isStringReturn);
    }

    protected Object returnObj(Object obj,boolean isReturnString){
        // 如果原始controller的返回值类型为string，则在这里处理之后也需要返回string，否则将会出错
        if(isReturnString){
            return processor.toText(obj);
        }
        return obj;
    }

    //全局异常处理的替代方式，但是只会针对Controller请求
    //这里只处理运行时异常，其他普通异常交由全局异常处理器进行处理
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Object exceptCatch(Exception e){
        e.printStackTrace();
        return ApiResp.error(500,e.getMessage());
    }

    @InitBinder
    public void globalInitBinder(WebDataBinder binder) {
        binder.addCustomFormatter(new DateFormatter("yyyy-MM-dd HH:mm:ss"));
    }

}
