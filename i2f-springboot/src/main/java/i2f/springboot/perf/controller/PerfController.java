package i2f.springboot.perf.controller;

import i2f.springboot.perf.context.PerfContext;
import i2f.springboot.perf.context.PerfStorage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2023/6/8 11:15
 * @desc
 */
@RequestMapping("/perf")
@RestController
public class PerfController {

    @GetMapping("/all")
    public Object getAll() {
        return PerfStorage.STORE.indexesMap;
    }

    @GetMapping("/one")
    public Object getOne(@RequestParam("key") String key) {
        return PerfStorage.STORE.indexesMap.get(key);
    }

    @GetMapping("/find")
    public Object find(@RequestParam(value = "key", required = false) String key) {
        if (key == null || "".equals(key)) {
            return PerfStorage.STORE.indexesMap;
        }
        Map<String, PerfContext> ret = new HashMap<>();
        boolean isNeg = key.startsWith("!");
        if (isNeg) {
            key = key.substring(1);
        }
        for (String item : PerfStorage.STORE.indexesMap.keySet()) {
            if (item.contains(key)) {
                if (!isNeg) {
                    ret.put(item, PerfStorage.STORE.indexesMap.get(item));
                }
            } else {
                if (isNeg) {
                    ret.put(item, PerfStorage.STORE.indexesMap.get(item));
                }
            }
        }
        return ret;
    }

}
