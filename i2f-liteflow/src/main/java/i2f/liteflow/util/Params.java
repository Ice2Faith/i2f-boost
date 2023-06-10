package i2f.liteflow.util;

import i2f.liteflow.data.vo.LiteFlowParamsVo;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2023/6/10 14:26
 * @desc
 */
public class Params {
    public static Map<String,Object> mapOf(List<LiteFlowParamsVo> params){
        Map<String,Object> ret=new LinkedHashMap<>();
        if(params==null){
            return ret;
        }
        for (LiteFlowParamsVo item : params) {
            String key = item.getEntryKey();
            if(key==null){
                continue;
            }
            Object val=null;
            String type = item.getEntryType();
            if("null".equals(type)){
                val=null;
            }else{
                String json = item.getEntryValue();
                Class<?> clazz = null;
                try{
                    clazz=Class.forName(type);
                }catch(Exception e){

                }
                if(clazz==null){
                    try{
                        clazz=Thread.currentThread().getContextClassLoader().loadClass(type);
                    }catch(Exception e){

                    }
                }
                if(clazz!=null){
                    val = Json.parseJson(json, clazz);
                }else{
                    val=json;
                }
            }
            ret.put(key,val);
        }
        return ret;
    }

    public static List<LiteFlowParamsVo> listOf(Map<String,Object> params){
        List<LiteFlowParamsVo> ret=new LinkedList<>();
        if(params==null){
            return ret;
        }
        for (String key : params.keySet()) {
            LiteFlowParamsVo item=new LiteFlowParamsVo();
            item.setId(Uid.getId());
            item.setEntryKey(key);
            Object val = params.get(key);
            if(val==null){
                item.setEntryType("null");
                item.setEntryValue("null");
            }else{
                item.setEntryType(val.getClass().getName());
                item.setEntryValue(Json.toJson(val,true));
            }
            ret.add(item);
        }
        return ret;
    }

    public static Map<String,Object> mergeMap(Map<String,Object> ... maps){
        Map<String,Object> ret=new LinkedHashMap<>();
        for (Map<String, Object> map : maps) {
            if(map==null){
                continue;
            }
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                ret.put(entry.getKey(), entry.getValue());
            }
        }
        return ret;
    }
}
