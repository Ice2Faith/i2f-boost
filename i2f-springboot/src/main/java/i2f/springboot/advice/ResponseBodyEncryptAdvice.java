package i2f.springboot.advice;


import i2f.core.annotations.remark.Remark;
import i2f.core.api.ApiResp;
import i2f.spring.jackson.JacksonJsonProcessor;
import i2f.springboot.advice.annotation.SecureParams;
import i2f.springboot.advice.annotation.StandardApiResp;
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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.*;

@ConditionalOnExpression("${i2f.springboot.config.advice.response-advice.enable:true}")
@Slf4j
@ControllerAdvice
@Remark({
        "provide response body encrypt and standard as ApiResp",
        "when encrypt,response header has [secure-data] header,value is true",
        "return type is ApiResp,and ApiResp.data,ApiResp.kvs.values will be encrypt."
})
public class ResponseBodyEncryptAdvice implements ResponseBodyAdvice<Object>, InitializingBean {
    public static final String SECURE_DATA_HEADER="secure-data";

    @Autowired
    private JacksonJsonProcessor processor;

    @Autowired
    public  IObjectEncryptor encryptor;

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
        if(o instanceof ApiResp){
            ApiResp resp=(ApiResp)o;
            if(methodParameter.getMethod().isAnnotationPresent(SecureParams.class)){
                SecureParams secureParams=methodParameter.getMethodAnnotation(SecureParams.class);
                if(secureParams.out()){
                    List<String> headers=new ArrayList<>();
                    headers.add(SECURE_DATA_HEADER);
                    serverHttpResponse.getHeaders().setAccessControlExposeHeaders(headers);
                    serverHttpResponse.getHeaders().set(SECURE_DATA_HEADER,"true");
                    Object data=resp.getData();
                    if(data!=null){
                        String enData= encryptor.encrypt(data);
                        resp.setData(enData);
                    }
                    Map<String,Object> kvs=resp.getKvs();
                    if(kvs!=null){
                        Map<String,Object> enKvs=new HashMap<>();
                        for(Map.Entry<String,Object> item : kvs.entrySet()){
                            Object idata=item.getValue();
                            String ienData=encryptor.encrypt(idata);
                            enKvs.put(item.getKey(),ienData);
                        }
                        resp.setKvs(enKvs);
                    }
                }
            }
            return returnObj(o,isStringReturn);
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
