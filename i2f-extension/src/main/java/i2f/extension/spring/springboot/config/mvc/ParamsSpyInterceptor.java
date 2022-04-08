package i2f.extension.spring.springboot.config.mvc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
public class ParamsSpyInterceptor implements HandlerInterceptor {

    //before come into controller to handle,you can check parameters
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String reqUrl=request.getRequestURI();
        String method=request.getMethod();
        log.info("---- request:"+method+" : "+reqUrl+" ----");
        Map<String,String[]> reqParams=request.getParameterMap();
        if(reqParams!=null && reqParams.size()>0){
            log.info("+parameters:");
            for(String key : reqParams.keySet()){
                log.info("+\t"+key+":");
                String[] vals=reqParams.get(key);
                for(String val : vals){
                    log.info("+\t\t"+val);
                }
            }
            log.info("--------");
        }
        return true;
    }

    //before response,you can modify response content
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    //all done ,cannot modify response
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //here print request log info
        String reqUrl=request.getRequestURI();
        if(ex!=null){
            log.info("--Exception-- request:"+reqUrl+" ----");
            ex.printStackTrace();
        }

    }
}
