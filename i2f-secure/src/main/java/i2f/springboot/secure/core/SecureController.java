package i2f.springboot.secure.core;

import i2f.springboot.secure.annotation.SecureParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ltb
 * @date 2022/6/30 11:34
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.secure.api.enable:true}")
@RestController
@RequestMapping("secure")
public class SecureController  {

    @Autowired
    private SecureTransfer secureTransfer;

    @SecureParams(in = false,out = false)
    @PostMapping("key")
    public String rsa(){
        String pubKey= secureTransfer.getWebRsaPublicKey();
        return pubKey;
    }
}
