package i2f.springboot.mvc;

import i2f.core.exception.BoostException;
import i2f.core.std.api.ApiResp;
import i2f.spring.serialize.jackson.JacksonJsonSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.URLEncoder;

@ConditionalOnExpression("${i2f.springboot.config.mvc.global-exception-handler.enable:true}")
@Configuration
@Order(20000)
public class PublicExceptionResolver implements HandlerExceptionResolver {

    @Autowired
    private JacksonJsonSerializer serializer;

    // 返回值不为null，表示已经处理，不会再进入其他处理器处理
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        System.out.println("---------ex:"+e.getMessage());
        System.out.println("---Global Exception:");
        e.printStackTrace();
        String exceptionClassName=e.getClass().getName();

        ModelAndView mv=new ModelAndView();
        if("org.springframework.security.access.AccessDeniedException".equals(exceptionClassName)){
            responseException(httpServletResponse,
                    ApiResp.error(401,"401,访问被拒了，你没有相应的权限."),e,
                    mv);
        }
        else if(e instanceof NoHandlerFoundException){ //404 页面异常
            responseException(httpServletResponse,
                    ApiResp.error(404,"404,连接走丢了，请检查URL是否正确或联系管理员."),e,
                    mv);
        }else if(e instanceof UndeclaredThrowableException){ //AOP 中的异常
            responseException(httpServletResponse,
                    ApiResp.error(500,"系统内部AOP异常，请联系管理员."),
                            (Exception)((UndeclaredThrowableException)e).getUndeclaredThrowable(),
                    mv);
        } else if(e instanceof BoostException){ // 自定义的业务异常
            responseException(httpServletResponse,
                    ApiResp.error("业务异常:"+e.getMessage()),e,
                    mv);
        }else{  //其他异常
            mv.setViewName("redirect:http://www.baidu.com/s?wd="+ URLEncoder.encode(e.getMessage()));
            //mv.setViewName("redirect:https://so.csdn.net/so/search/s.do?q="+e.getMessage());
        }
        return mv;
    }

    private void responseException(HttpServletResponse response,ApiResp data,Throwable ex,ModelAndView mv){
        try {
            ex.printStackTrace();
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(200);
            response.getWriter().write(serializer.serialize(data));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

}
