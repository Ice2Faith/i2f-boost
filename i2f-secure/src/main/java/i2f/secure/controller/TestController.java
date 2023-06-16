package i2f.secure.controller;

import i2f.secure.StackTraceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2023/6/13 11:23
 * @desc
 */
@Slf4j
@RestController
@RequestMapping("test")
public class TestController {
    private SecureRandom random = new SecureRandom();
    private DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS");

    @RequestMapping("param")
    public Object param(@RequestParam("name")String name,
                        @RequestParam("age")Integer age,
                        @RequestBody Map<String,Object> body,
                        HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String,Object> ret=new HashMap<>();
        ret.put("map",parameterMap);
        ret.put("body",body);
        ret.put("name",name);
        ret.put("age",age);
        return ret;
    }

    @RequestMapping("echo")
    public Integer echo(@RequestBody Integer val) {
        return val;
    }

    @RequestMapping("int")
    public int getInt() {
        return random.nextInt(100);
    }

    @RequestMapping("str")
    public String getStr() {
        return fmt.format(LocalDateTime.now());
    }

    @RequestMapping("map")
    public Object getMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("inum",random.nextInt(100));
        map.put("dnum",random.nextDouble());
        map.put("bool",random.nextBoolean());
        map.put("date",LocalDateTime.now());
        map.put("dateStr",fmt.format(LocalDateTime.now()));
        return map;
    }

    @RequestMapping("obj")
    public Object getObject(@RequestBody Map<String,Object> obj) {
        obj.put("dateStr",fmt.format(LocalDateTime.now()));
        log.info("trace:\n"+ StackTraceUtils.getCurrentStackTrace());
        return obj;
    }
}
