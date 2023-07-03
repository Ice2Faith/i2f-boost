package i2f.springboot.secure.core;

import i2f.springboot.secure.annotation.SecureParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ltb
 * @date 2022/6/30 11:34
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.secure.api.enable:true}")
@RestController
@RequestMapping("secure")
public class SecureController {

    @Autowired
    private SecureTransfer secureTransfer;

    @SecureParams(in = false, out = false)
    @PostMapping("key")
    public String key() {
        String pubKey = secureTransfer.getWebAsymPublicKey();
        return pubKey;
    }

    @SecureParams(in = false, out = false)
    @PostMapping("clientKey")
    public String clientKey(HttpServletRequest request) {
        String priKey = secureTransfer.getWebClientAsymPrivateKey(request);
        return priKey;
    }
}
