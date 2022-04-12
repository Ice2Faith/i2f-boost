package i2f.springboot.security.impl;

import i2f.core.api.ApiResp;
import i2f.springboot.security.SecurityForwardUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ltb
 * @date 2022/4/7 17:07
 * @desc
 */
@RestController
@RequestMapping("security")
public class SecurityForwardController {

    @RequestMapping("response")
    public ApiResp response(HttpServletRequest request){
        return SecurityForwardUtil.getResp(request);
    }
}
