package i2f.springboot.secure.core;


import i2f.springboot.secure.util.Base64Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ltb
 * @date 2021/10/8
 */
@ConditionalOnExpression("${i2f.springboot.config.secure.enc-url-forward-controller.enable:true}")
@Slf4j
@Controller
public class EncodeRequestForwardController implements InitializingBean {
    @RequestMapping("/enc/{encodeUrl}")
    public void encodeUrlForward(@PathVariable("encodeUrl")String encodeUrl, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        byte[] data= Base64Util.fromUrl(encodeUrl);
        String trueUrl= new String(data,"UTF-8");
        log.info("forward:\n\tsrc:"+encodeUrl+"\n\t"+"dst:"+trueUrl);
        request.getRequestDispatcher(trueUrl).forward(request,response);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("EncodeRequestForwardController config done.");
    }
}
