package i2f.spring.secret.web;

import i2f.spring.secret.web.annotations.SecretParams;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author ltb
 * @date 2022/10/20 18:21
 * @desc
 */
@RestController
@RequestMapping("test")
public class TestWeb {

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
