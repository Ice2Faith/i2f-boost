package i2f.extension.spring.springboot.config.mvc;

import i2f.core.api.ApiResp;
import i2f.core.exception.BoostException;
import i2f.extension.json.jackson.JacksonJsonProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;

@ConditionalOnExpression("${i2f.springboot.config.mvc.global-exception-handler.enable:true}")
@Configuration
public class PublicExceptionResolver implements HandlerExceptionResolver {
    public JacksonJsonProcessor processor=new JacksonJsonProcessor();

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        System.out.println("---------ex:"+e.getMessage());
        System.out.println("---Global Exception:");
        e.printStackTrace();

        ModelAndView mv=new ModelAndView();
        if(e instanceof NoHandlerFoundException){ //404 页面异常
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
            mv.setViewName("redirect:http://www.baidu.com/s?wd="+e.getMessage());
            //mv.setViewName("redirect:https://so.csdn.net/so/search/s.do?q="+e.getMessage());
        }
        return mv;
    }

    private void responseException(HttpServletResponse response,ApiResp data,Throwable ex,ModelAndView mv){
        try {
            ex.printStackTrace();
            response.setStatus(200);
            response.getWriter().write(processor.toText(data));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

}
