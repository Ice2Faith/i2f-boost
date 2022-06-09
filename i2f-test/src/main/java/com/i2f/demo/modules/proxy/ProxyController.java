package com.i2f.demo.modules.proxy;

import i2f.spring.proxy.HttpProxyHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author ltb
 * @date 2022/6/9 17:08
 * @desc
 */
@Controller
@RequestMapping("proxy")
public class ProxyController {

    @RequestMapping("/**")
    public void proxyBaidu(HttpServletRequest request, HttpServletResponse response) throws IOException, URISyntaxException {
        boolean ok = HttpProxyHandler.build()
                .mapping("/proxy/baidu", "http://www.baidu.com")
                .mapping("/proxy/exam", "http://localhost:8101")
                .handle(request, response);
        if(!ok){
            response.setStatus(404);
        }
    }

}
