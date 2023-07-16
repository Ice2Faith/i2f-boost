package i2f.springboot.perf.controller;

import i2f.core.std.api.ApiResp;
import i2f.springboot.perf.PerfConfig;
import i2f.springboot.perf.context.PerfContext;
import i2f.springboot.perf.context.PerfStorage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2023/6/8 11:15
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.perf.api.enable:true}")
@RequestMapping("/perf")
@RestController
public class PerfController {

    @GetMapping("/all")
    public ApiResp<?> getAll() {
        Object ret = PerfConfig.STORE.indexesMap;
        return ApiResp.success(ret);
    }

    @GetMapping("/one")
    public ApiResp<?> getOne(@RequestParam("key") String key) {
        Object ret=PerfConfig.STORE.indexesMap.get(key);
        return ApiResp.success(ret);
    }

    @GetMapping("/find")
    public ApiResp<?> find(@RequestParam(value = "key", required = false) String key) {
        if (key == null || "".equals(key)) {
            Object ret=PerfConfig.STORE.indexesMap;
            return ApiResp.success(ret);
        }
        Map<String, PerfContext> ret = new HashMap<>();
        boolean isNeg = key.startsWith("!");
        if (isNeg) {
            key = key.substring(1);
        }
        key=key.toLowerCase();
        for (String iter : PerfConfig.STORE.indexesMap.keySet()) {
            String item=iter.toLowerCase();
            if (item.contains(key)) {
                if (!isNeg) {
                    ret.put(item, PerfConfig.STORE.indexesMap.get(item));
                }
            } else {
                if (isNeg) {
                    ret.put(item, PerfConfig.STORE.indexesMap.get(item));
                }
            }
        }
        return ApiResp.success(ret);
    }

}
