package i2f.spring.secret.web;

import i2f.core.security.secret.impl.ram.api.Md5Hasher;
import i2f.core.security.secret.util.SecretUtil;
import i2f.spring.secret.web.annotations.SecretParams;
import i2f.spring.secret.web.core.SecretWebCore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/10/20 18:21
 * @desc
 */
@RestController
@RequestMapping("test")
public class TestWeb {

    @Autowired
    private SecretWebCore secretWebCore;

    @RequestMapping("handle")
    public Map<String, Object> handle(@RequestBody Map<String, Object> req) {
        Map<String, Object> ret = new HashMap<>();
        byte[] pk = secretWebCore.secret().mineKey.publicKey();
        String data = SecretUtil.escapeBase64(SecretUtil.toBase64(pk));
        ret.put("data", data);
        return ret;
    }

    public static void main(String[] args) {
        Md5Hasher hasher = new Md5Hasher();
        byte[] hash = hasher.hash(SecretUtil.str2utf8("123456"));
        String str = SecretUtil.utf82str(hash);
        System.out.println(str);
    }

    @SecretParams(out = false)
    @RequestMapping("in")
    public Map<String, Object> in(@RequestBody Map<String, Object> beanMap) {
        System.out.println(beanMap);
        beanMap.put("test", "ok");
        return beanMap;
    }

    @SecretParams(in = false)
    @RequestMapping("out")
    public Map<String, Object> out(@RequestBody Map<String, Object> beanMap) {
        System.out.println(beanMap);
        beanMap.put("test", "ok");
        return beanMap;
    }

    @SecretParams
    @RequestMapping("both")
    public Map<String, Object> both(@RequestBody Map<String, Object> beanMap) {
        System.out.println(beanMap);
        beanMap.put("test", "ok");
        return beanMap;
    }
}
