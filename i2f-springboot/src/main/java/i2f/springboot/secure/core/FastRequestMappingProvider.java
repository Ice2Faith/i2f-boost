package i2f.springboot.secure.core;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ltb
 * @date 2022/7/1 8:32
 * @desc
 */
@Component
public class FastRequestMappingProvider implements InitializingBean {
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandler;

    private Map<String,Map<RequestMappingInfo,HandlerMethod>> fastMapping=new ConcurrentHashMap<>();

    public void initFastMapping() {
        Map<String,Map<RequestMappingInfo,HandlerMethod>> ret=new ConcurrentHashMap<>();
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandler.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> item : handlerMethods.entrySet()) {
            RequestMappingInfo key = item.getKey();
            HandlerMethod value = item.getValue();
            Set<String> patterns = key.getPatternsCondition().getPatterns();
            for(String patten : patterns) {
                if (!ret.containsKey(patten)) {
                    ret.put(patten, new ConcurrentHashMap<>());
                }
                ret.get(patten).put(key, value);
            }
        }

        fastMapping=new ConcurrentHashMap<>(ret.size());
        for(Map.Entry<String,Map<RequestMappingInfo,HandlerMethod>> item : ret.entrySet()){
            String key = item.getKey();
            Map<RequestMappingInfo, HandlerMethod> value = item.getValue();
            Map<RequestMappingInfo, HandlerMethod> umap=Collections.unmodifiableMap(value);
            fastMapping.put(key, umap);
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initFastMapping();
    }

    public Method getMethodFormMappingMap(HttpServletRequest request, Map<RequestMappingInfo, HandlerMethod> handlerMethods) {
        for(Map.Entry<RequestMappingInfo, HandlerMethod> item : handlerMethods.entrySet()){
            RequestMappingInfo key = item.getKey();
            HandlerMethod value = item.getValue();
            RequestMappingInfo cond = key.getMatchingCondition(request);
            if(cond!=null){
                HandlerMethod handler= handlerMethods.get(cond);
                if(handler!=null){
                    return handler.getMethod();
                }
            }
        }
        return null;
    }

    public Method getMatchRequestMethod(HttpServletRequest request){
        String url=request.getRequestURI();
        Map<RequestMappingInfo, HandlerMethod> fastMethods=fastMapping.get(url);
        if(fastMethods!=null){
            Method method=getMethodFormMappingMap(request,fastMethods);
            if(method!=null){
                return method;
            }
        }
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandler.getHandlerMethods();
        return getMethodFormMappingMap(request, handlerMethods);
    }
}
