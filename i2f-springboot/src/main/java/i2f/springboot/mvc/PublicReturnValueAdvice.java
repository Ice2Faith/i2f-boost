package i2f.springboot.mvc;

import i2f.core.api.ApiResp;
import i2f.spring.jackson.JacksonJsonProcessor;
import i2f.springboot.mvc.annotations.StdApiResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.MethodParameter;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.LinkedHashMap;

@ConditionalOnExpression("${i2f.springboot.config.mvc.pubic-return-advice.enable:true}")
@Slf4j
@ControllerAdvice
public class PublicReturnValueAdvice implements ResponseBodyAdvice<Object> {
    @Autowired
    public JacksonJsonProcessor processor;

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        if(methodParameter.getParameterType().equals(ApiResp.class)){
            return true;
        }
        return methodParameter.getContainingClass().getAnnotationsByType(ResponseBody.class)!=null
                || methodParameter.hasMethodAnnotation(ResponseBody.class);
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        StdApiResp ann=methodParameter.getMethodAnnotation(StdApiResp.class);
        if(ann==null){
            ann=methodParameter.getContainingClass().getAnnotation(StdApiResp.class);
        }
        if(ann==null || !ann.value()){
            return o;
        }
        Object obj=getTrulyReturnObj(o, serverHttpRequest);
        log.info("---- "+serverHttpRequest.getURI().getPath());
       try{
           log.info("\traw resp data:\n\t\t"+ processor.toText(o));
           log.info("\ttrue resp data:\n\t\t"+ processor.toText(obj));
       }catch(Exception e){
           e.printStackTrace();
           System.out.println(e.getMessage());
       }
        return obj;
    }

    private Object getTrulyReturnObj(Object o, ServerHttpRequest serverHttpRequest) {
        //排除actuator进行性能监视，不进行封装
        String path=serverHttpRequest.getURI().getPath();
        if(path.startsWith("/actuator") ){
            System.out.println("ignore matched");
            return o;
        }
        if(o instanceof ApiResp) {
            return o;
        }
        if(o instanceof String){
            try{
                return processor.toText(ApiResp.success(o));
            }catch(Exception e){
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
        //如果返回值是LinkedHashMap，那么有可能是发生了错误，类型为LinkedHashMap<String,Object>
        //其中最常见的就是404页面找不到，在这里是能够截获的
        if(o instanceof LinkedHashMap){
            LinkedHashMap map=(LinkedHashMap) o;
            Object status=map.get("status");
            if(status!=null && status instanceof Integer && ((Integer) status)==404){
                System.out.println(status+":"+status.getClass());
                return ApiResp.error(404,"404 bad request found").data(o);
            }
        }
        return ApiResp.success(o,"success");
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
