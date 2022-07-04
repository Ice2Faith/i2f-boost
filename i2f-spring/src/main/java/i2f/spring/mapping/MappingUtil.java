package i2f.spring.mapping;

import i2f.core.data.Pair;
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
 * @date 2022/7/4 9:13
 * @desc
 */
@Component
public class MappingUtil implements InitializingBean {

    @Autowired
    protected RequestMappingHandlerMapping requestMappingHandler;

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
            Map<RequestMappingInfo, HandlerMethod> umap= Collections.unmodifiableMap(value);
            fastMapping.put(key, umap);
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initFastMapping();
    }

    public Pair<RequestMappingInfo,HandlerMethod> getRequestMapping(HttpServletRequest request) {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods=requestMappingHandler.getHandlerMethods();
        for(Map.Entry<RequestMappingInfo, HandlerMethod> item : handlerMethods.entrySet()){
            RequestMappingInfo key = item.getKey();
            HandlerMethod value = item.getValue();
            RequestMappingInfo cond = key.getMatchingCondition(request);
            if(cond!=null){
                HandlerMethod handler= handlerMethods.get(cond);
                if(handler!=null){
                    return new Pair<>(cond,handler);
                }
            }
        }
        return null;
    }

    public HandlerMethod getRequestMappingHandlerMethod(HttpServletRequest request){
        Pair<RequestMappingInfo,HandlerMethod> pair=getRequestMapping(request);
        if(pair!=null){
            return pair.val;
        }
        return null;
    }

    public Method getRequestMappingMethod(HttpServletRequest request){
        HandlerMethod handlerMethod=getRequestMappingHandlerMethod(request);
        if(handlerMethod!=null){
            return handlerMethod.getMethod();
        }
        return null;
    }

}
