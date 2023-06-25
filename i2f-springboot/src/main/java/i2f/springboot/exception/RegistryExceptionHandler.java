package i2f.springboot.exception;

import i2f.core.std.api.ApiResp;
import i2f.spring.environment.EnvironmentUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author ltb
 * @date 2022/7/3 21:27
 * @desc
 */
@Slf4j
@RestControllerAdvice
public class RegistryExceptionHandler implements InitializingBean, EnvironmentAware {

    // 注册项规范：
    // REGISTRY_EXCEPTION_PREFIX.{order}.{full-exception-class-name}={code},{msg}
    // 举例
    // i2f.springboot.exception-handler.1000.java.lang.RuntimeException=500,系统运行异常
    public static final String REGISTRY_EXCEPTION_PREFIX="i2f.springboot.exception-handler.";

    protected Environment env;
    protected volatile CopyOnWriteArrayList<ExceptionMsg> exceptionMap=new CopyOnWriteArrayList<>();

    public static class ExceptionMsg{
        public int order;
        public Class<? extends Throwable> exception;

        public int code;
        public String msg;

        public ExceptionMsg(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public ExceptionMsg(int order, Class<? extends Throwable> exception, int code, String msg) {
            this.order = order;
            this.exception = exception;
            this.code = code;
            this.msg = msg;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initExceptionMap();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env=environment;
    }

    public void initExceptionMap(){
        Map<String, Object> map = EnvironmentUtil.of(env).getPropertiesWithPrefix(false, REGISTRY_EXCEPTION_PREFIX);
        for(Map.Entry<String,Object> item : map.entrySet()){
            int order=1000;
            Class<? extends Throwable> clazz=null;
            int code=500;
            String msg="系统异常";

            String orderException=item.getKey();
            String codeMsg=String.valueOf(item.getValue()).trim();
            int orderIdx=orderException.indexOf(".");

            if(orderIdx>=0){
                String[] arr=orderException.split("\\.",2);
                try{
                    order=Integer.parseInt(arr[0]);
                    clazz=(Class)Class.forName(arr[1]);
                }catch(Exception e){
                    log.warn("bad exception registry order.exception-class-name:"+orderException+" by "+e.getMessage());
                    try{
                        clazz=(Class)Class.forName(arr[1]);
                    }catch(Exception ex){
                        log.warn("bad exception registry exception-class-name:"+orderException+" by "+ex.getMessage());
                        try{
                            clazz=(Class)Class.forName("java.lang."+arr[1]);
                        }catch(Exception exc){
                            log.warn("bad exception registry exception-class-name:"+orderException+" by "+exc.getMessage());
                            try{
                                clazz=(Class)Class.forName("java.util."+arr[1]);
                            }catch(Exception exce){
                                log.warn("bad exception registry exception-class-name:"+orderException+" by "+exce.getMessage());
                                try{
                                    clazz=(Class)Class.forName("java.io."+arr[1]);
                                }catch(Exception excep){
                                    log.warn("bad exception registry exception-class-name:"+orderException+" by "+excep.getMessage());
                                }
                            }
                        }
                    }
                }
            }

            int codeIdx=codeMsg.indexOf(",");
            if(codeIdx>=0){
                String[] arr=codeMsg.split(",",2);
                try{
                    code=Integer.parseInt(arr[0]);
                    msg=arr[1];
                }catch(Exception e){
                    log.warn("bad exception registry code.msg:"+codeMsg);
                    msg=codeMsg;
                }
            }

            if(clazz!=null){
                ExceptionMsg sitem=new ExceptionMsg(order,clazz,code,msg);
                exceptionMap.add(sitem);
                log.info("exception has registry of code["+sitem.code+"] of exception["+sitem.exception.getName()+"]");
            }
        }

        exceptionMap.sort(new Comparator<ExceptionMsg>() {
            @Override
            public int compare(ExceptionMsg o1, ExceptionMsg o2) {
                return o1.order-o2.order;
            }
        });
    }

    @ExceptionHandler(Throwable.class)
    public ApiResp<String> thr(Throwable e){
        Class<? extends Throwable> clazz=e.getClass();
        log.error(clazz.getName(),e);
        ExceptionMsg msg=null;
        // 优先直接类获取
        for(ExceptionMsg item : exceptionMap){
            if(item.exception.equals(clazz)){
                msg=item;
                break;
            }
        }
        // 找不到再根据继承关系获取
        if(msg==null){
            for(ExceptionMsg item : exceptionMap){
                if(item.exception.isAssignableFrom(clazz)){
                    msg=item;
                    break;
                }
            }
        }

        if(msg==null){
            msg=new ExceptionMsg(500,"系统异常");
        }

        return ApiResp.error(msg.code,msg.msg);
    }

}
