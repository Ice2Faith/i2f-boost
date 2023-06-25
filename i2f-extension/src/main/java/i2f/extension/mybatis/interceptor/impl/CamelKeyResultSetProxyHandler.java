package i2f.extension.mybatis.interceptor.impl;

import i2f.core.type.str.Strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/4/4 15:59
 * @desc
 */
public class CamelKeyResultSetProxyHandler extends AbstractResultSetPreProcessProxyHandler {

    private List<Map<String, Object>> camelKey(List<Map<String, Object>> list) {
        List<Map<String, Object>> ret=new ArrayList<>();
        for(Map item : list){
            Map<String, Object> map=new HashMap<>();
            for(Object key : item.keySet()){
                String skey = String.valueOf(key);
                String camelKey = Strings.toCamel(skey);
                map.put(camelKey,item.get(key));
            }
            ret.add(map);
        }
        return ret;
    }

    @Override
    protected List<Map<String, Object>> preProcess(List<Map<String, Object>> list) {
        return camelKey(list);
    }
}
