package i2f.springboot.limit.aop;

import i2f.springboot.limit.annotations.Limit;
import i2f.springboot.limit.consts.LimitConsts;
import i2f.springboot.limit.consts.LimitType;
import i2f.springboot.limit.core.ILimitListener;
import i2f.springboot.limit.core.IUserProvider;
import i2f.springboot.limit.core.LimitContext;
import i2f.springboot.limit.data.LimitAnnDto;
import i2f.springboot.limit.data.LimitDto;
import i2f.springboot.limit.exception.LimitException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author Ice2Faith
 * @date 2023/8/1 17:53
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.limit.api.enable:true}")
@Slf4j
@Aspect
@Component
public class ApiLimitAop {


    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PatchMapping)" +
            "")
    public void annPointCut() {
    }

    @Autowired(required = false)
    private IUserProvider userProvider;

    @Autowired(required = false)
    private ILimitListener limitListener;

    @Autowired
    private LimitContext limitContext;

    @Before("annPointCut()")
    public void doAround(JoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Object _target = pjp.getTarget();
        Object _this = pjp.getThis();
        Method method = signature.getMethod();
        Object[] args = pjp.getArgs();
        Parameter[] params = method.getParameters();

        long ts=System.currentTimeMillis();
        String ip=null;
        String user=null;
        HttpServletRequest request=null;

        String resourceKey=method.toGenericString();
        if(!limitContext.getLimitResourcesMap().containsKey(resourceKey)){
            limitContext.getLimitResourcesMap().put(resourceKey,method);
        }

        Map<String, Map<LimitType, Map<String,LimitDto>>> limits = limitContext.getLimits();
        if(!limits.containsKey(resourceKey)){
            Map<LimitType,LimitAnnDto> anns=getLimitAnnotations(method);
            if(anns==null || anns.isEmpty()){
                anns=limitContext.getDefaultLimitMap();
            }
            if(anns==null){
                anns=new ConcurrentHashMap<>();
            }

            Map<LimitType,Map<String,LimitDto>> methodMap=new ConcurrentHashMap<>();
            for (Map.Entry<LimitType, LimitAnnDto> entry : anns.entrySet()) {
                LimitAnnDto ann = entry.getValue();
                LimitDto dto=new LimitDto();
                dto.setEnable(ann.isValue());
                dto.setWindow(ann.getWindow());
                dto.setUnit(ann.getUnit());
                dto.setCount(ann.getCount());
                dto.setCurrents(new ConcurrentSkipListSet<>());
                dto.getCurrents().add(ts);

                LimitDto tpl=new LimitDto();
                tpl.setEnable(ann.isValue());
                tpl.setWindow(ann.getWindow());
                tpl.setUnit(ann.getUnit());
                tpl.setCount(ann.getCount());

                LimitType type = entry.getKey();
                Map<String,LimitDto> keyMap=new ConcurrentHashMap<>();
                if(type==LimitType.GLOBAL){
                    keyMap.put(LimitConsts.DEFAULT_TPL_KEY,dto);
                }else if(type==LimitType.IP){
                    if(request==null){
                        request=getRequest();
                    }
                    if(ip==null){
                        ip=getIp(request);
                    }
                    keyMap.put(LimitConsts.DEFAULT_TPL_KEY,tpl);
                    keyMap.put(ip,dto);
                }else if(type==LimitType.USER){
                    if(request==null){
                        request=getRequest();
                    }
                    if(user==null){
                        user=getUser(request);
                    }
                    keyMap.put(LimitConsts.DEFAULT_TPL_KEY,tpl);
                    keyMap.put(user,dto);
                }
                methodMap.put(type,keyMap);
            }

            if(!methodMap.isEmpty()){
                limits.put(resourceKey,methodMap);
            }
        }

        if(!limits.containsKey(resourceKey)){
            return;
        }

        Map<LimitType, Map<String,LimitDto>> limitMap = limits.get(resourceKey);
        if(limitMap.isEmpty()){
            return;
        }

        LimitType exceedType=null;
        LimitDto dto=null;

        for (Map.Entry<LimitType, Map<String,LimitDto>> entry : limitMap.entrySet()) {
            LimitType type = entry.getKey();
            Map<String, LimitDto> keyMap = entry.getValue();

            if(type==LimitType.GLOBAL){
                dto = keyMap.get(LimitConsts.DEFAULT_TPL_KEY);
            }else if(type==LimitType.IP){
                if(request==null){
                    request=getRequest();
                }
                if(ip==null){
                    ip=getIp(request);
                }
                if(!keyMap.containsKey(ip)){
                    LimitDto tpl = keyMap.get(LimitConsts.DEFAULT_TPL_KEY);
                    LimitDto tmp=new LimitDto();
                    tmp.setEnable(tpl.isEnable());
                    tmp.setWindow(tpl.getWindow());
                    tmp.setUnit(tpl.getUnit());
                    tmp.setCount(tpl.getCount());
                    tmp.setCurrents(new ConcurrentSkipListSet<>());
                    tmp.getCurrents().add(ts);
                    keyMap.put(ip,tmp);
                }
               dto=keyMap.get(ip);
            }else if(type==LimitType.USER){
                if(request==null){
                    request=getRequest();
                }
                if(user==null){
                    user=getUser(request);
                }
                if(!keyMap.containsKey(user)){
                    LimitDto tpl = keyMap.get(LimitConsts.DEFAULT_TPL_KEY);
                    LimitDto tmp=new LimitDto();
                    tmp.setEnable(tpl.isEnable());
                    tmp.setWindow(tpl.getWindow());
                    tmp.setUnit(tpl.getUnit());
                    tmp.setCount(tpl.getCount());
                    tmp.setCurrents(new ConcurrentSkipListSet<>());
                    tmp.getCurrents().add(ts);
                    keyMap.put(user,tmp);
                }
                dto=keyMap.get(user);
            }

            Set<Long> rmSet=new LinkedHashSet<>();
            long ets=ts-dto.getUnit().toMillis(dto.getWindow());
            for (Long cts : dto.getCurrents()) {
                if(cts<=ets){
                    rmSet.add(cts);
                }
            }
            for (Long rts : rmSet) {
                dto.getCurrents().remove(rts);
            }

            if(dto.isEnable() && dto.getCount()>0){
                if(dto.getCurrents().size()>=dto.getCount()){
                    if(exceedType==null){
                        exceedType=type;
                    }
                }

                dto.getCurrents().add(ts);
            }


        }

        if(exceedType!=null){
            if(limitListener!=null){
                limitListener.accept(exceedType,method,dto);
            }else{
                String msg="current resource has limit request, please retry later.";
                if(exceedType==LimitType.IP){
                    msg="current resource has limit request for your ip, please retry later";
                }
                if(exceedType==LimitType.USER){
                    msg="current resource has limit request for your account, please retry later";
                }
                throw new LimitException(exceedType,msg);
            }
        }
    }


    public String getUser(HttpServletRequest request){
        if(userProvider==null){
            throw new IllegalStateException("limit require user provider, but not found.");
        }
        Object userKey = userProvider.getUserKey(request);
        return String.valueOf(userKey);
    }


    public HttpServletRequest getRequest(){
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return request;
    }


    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip=request.getHeader("X-Real-IP");
        }
        if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){
            ip=request.getRemoteAddr();
            if (ip.equals("127.0.0.1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                if (inet !=null) {
                    ip = inet.getHostAddress();
                }
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.contains(",")) {
            ip = ip.substring(0, ip.indexOf(","));
        }
        return ip;
    }

    public static Map<LimitType,LimitAnnDto> getLimitAnnotations(Method method) {
        Map<LimitType,LimitAnnDto> map=new HashMap<>();

        Set<Limit> manns = getRepeatableAnnotations(method, Limit.class);
        Set<Limit> canns = getRepeatableAnnotations(method.getDeclaringClass(), Limit.class);

        if(canns!=null){
            for (Limit item : canns) {
                LimitAnnDto dto=new LimitAnnDto();
                dto.setValue(item.value());
                dto.setWindow(item.window());
                dto.setUnit(item.unit());
                dto.setCount(item.count());
                dto.setType(item.type());
                map.put(item.type(),dto);
            }
        }
        if(manns!=null){
            for (Limit item : manns) {
                LimitAnnDto dto=null;
                if(map.containsKey(item.type())){
                    dto=map.get(item.type());
                }else{
                    dto=new LimitAnnDto();
                }
                dto.setValue(item.value());
                dto.setWindow(item.window());
                dto.setUnit(item.unit());
                dto.setCount(item.count());
                dto.setType(item.type());
                map.put(item.type(),dto);
            }
        }

        return map;
    }



    public static<T extends Annotation> Set<T> getRepeatableAnnotations(AnnotatedElement elem, Class<T> clazz){
        Set<T> ret=new LinkedHashSet<>();
        T[] dann = elem.getDeclaredAnnotationsByType(clazz);
        if(dann!=null && dann.length>0){
            for (T item : dann) {
                ret.add(item);
            }
        }
        T[] ann = elem.getDeclaredAnnotationsByType(clazz);
        if(ann!=null && ann.length>0){
            for (T item : ann) {
                ret.add(item);
            }
        }
        return ret;
    }

}
