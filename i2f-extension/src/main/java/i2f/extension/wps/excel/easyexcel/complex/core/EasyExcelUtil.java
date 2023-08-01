package i2f.extension.wps.excel.easyexcel.complex.core;

import com.alibaba.excel.write.metadata.fill.FillWrapper;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2023/8/1 15:20
 * @desc
 */
public class EasyExcelUtil {

    public static List<Object> recursiveFoundFillData(Object data){
        return recursiveFoundFillData(data,"");
    }
    public static List<Object> recursiveFoundFillData(Object data,String route){
        List<Object> ret=new ArrayList<>();
        if(StringUtils.isEmpty(route)){
            ret.add(data);
        }
        if(data==null){
            return ret;
        }
        if(isBasicType(data)){
            return ret;
        }

        if(data instanceof Collection){
            // do nothing
            if(!StringUtils.isEmpty(route)){
                ret.add(new FillWrapper(route, (Collection) data));
            }
        }else if(data instanceof Map){
            // wrapper map every key
            Map<?,?> map=(Map<?,?>)data;
            for (Object key : map.keySet()) {
                Object val = map.get(key);
                extractKeyValue(route, ret, key, val);
            }
        }else{
            // wrapper obj every field
            Class<?> clazz = data.getClass();
            Set<Field> fields = foundAllFields(clazz,(field)->{
                int modifiers = field.getModifiers();
                return !Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers);
            });
            for (Field field : fields) {
                try{
                    field.setAccessible(true);
                    Object val=field.get(data);
                    Object key=field.getName();
                    extractKeyValue(route, ret, key, val);
                }catch(Exception e){

                }
            }
        }

        return ret;
    }

    private static void extractKeyValue(String route, List<Object> ret, Object key, Object val) {
        String nextRoute=addRoute(route,String.valueOf(key));
        if(val instanceof Collection){
            ret.add(new FillWrapper(nextRoute, (Collection) val));
        }else{
            if(!isBasicType(val)){
                ret.add(new FillWrapper(nextRoute, Arrays.asList(val)));
            }
            List<Object> nextes = recursiveFoundFillData(val, nextRoute);
            ret.addAll(nextes);
        }
    }

    public static Set<Field> foundAllFields(Class<?> clazz, Predicate<Field> predicate){
        Set<Field> ret=new LinkedHashSet<>();
        if(clazz==null || clazz.equals(Object.class)){
            return ret;
        }
        for (Field item : clazz.getDeclaredFields()) {
            if(predicate==null || predicate.test(item)){
                ret.add(item);
            }
        }
        for (Field item : clazz.getFields()) {
            if(predicate==null || predicate.test(item)){
                ret.add(item);
            }
        }
        Class<?> superClass = clazz.getSuperclass();
        Set<Field> nextes = foundAllFields(superClass, predicate);
        ret.addAll(nextes);
        return ret;
    }

    public static String addRoute(String prefix,String suffix){
        if(StringUtils.isEmpty(prefix)){
            return suffix;
        }
        if(StringUtils.isEmpty(suffix)){
            return prefix;
        }
        return prefix+"$"+suffix;
    }

    public static boolean isBasicType(Object data){
        if(data ==null){
            return true;
        }
        if(data instanceof Number){
            return true;
        }
        if(data instanceof Boolean){
            return true;
        }
        if(data instanceof String){
            return true;
        }
        if(data instanceof Character){
            return true;
        }
        if(data instanceof Date){
            return true;
        }
        if(data instanceof LocalTime){
            return true;
        }
        if(data instanceof LocalDate){
            return true;
        }
        if(data instanceof LocalDateTime){
            return true;
        }
        Class<?> clazz = data.getClass();
        if(typeOf(clazz,int.class)){
            return true;
        }
        if(typeOf(clazz,long.class)){
            return true;
        }
        if(typeOf(clazz,short.class)){
            return true;
        }
        if(typeOf(clazz,byte.class)){
            return true;
        }
        if(typeOf(clazz,char.class)){
            return true;
        }
        if(typeOf(clazz,float.class)){
            return true;
        }
        if(typeOf(clazz,double.class)){
            return true;
        }
        if(typeOf(clazz,boolean.class)){
            return true;
        }
        return false;
    }

    public static boolean typeOf(Class<?> clazz,Class<?> typeClazz){
        if(typeClazz.equals(clazz)){
            return true;
        }
        return typeClazz.isAssignableFrom(clazz);
    }
}
