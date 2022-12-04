package i2f.core.reflect.bean;

import i2f.core.annotations.remark.Author;
import i2f.core.functional.common.ICompare;
import i2f.core.functional.common.IFilter;
import i2f.core.functional.impl.AlwaysTrueFilter;
import i2f.core.functional.impl.EmptyFilter;
import i2f.core.functional.impl.EqualsCompare;
import i2f.core.functional.impl.NullFilter;
import i2f.core.reflect.bean.annotations.BeanTag;
import i2f.core.reflect.core.ReflectResolver;
import i2f.core.reflect.interfaces.PropertyAccessor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/3/25 10:37
 * @desc
 */
@Author("i2f")
public class BeanTagResolver {
    /**
     * 获取包含指定TAG的字段的值
     * @param obj
     * @param tags
     * @return
     */
    public static Map<String,Object> getValuesByTag(Object obj,String ... tags){
        Map<String, Object> ret=new HashMap<>();
        if(obj==null || tags==null || tags.length==0){
            return ret;
        }
        Class clazz=obj.getClass();
        List<PropertyAccessor> list=getLogicalReadableFieldsWithTags(clazz,tags);
        for(PropertyAccessor item : list){
            String name=item.getName();
            item.setInvokeObject(obj);
            Object val=item.get();
            ret.put(name,val);
        }
        return ret;
    }

    /**
     * 将源对象中包含指定TAG的属性复制到目标对象中
     * @param srcObj
     * @param dstObj
     * @param tags
     * @param <T>
     * @return
     */
    public static<T> T copyByTag(Object srcObj,T dstObj,String ... tags){
        Map<String,Object> map=getValuesByTag(srcObj,tags);
        BeanResolver.copy(map,dstObj,true);
        return dstObj;
    }

    public static<T> T copyBothWithTag(Object srcObj,T dstObj,String ... tags){
        return copyBothWithTag(srcObj, dstObj,true,null, tags);
    }

    /**
     * 将源对象和目标对象同时包含指定TAG的字段进行复制
     * 同时可以指定是否null值也要复制
     * 可以使用自定义的字段名映射比较器keyCmp
     * @param srcObj
     * @param dstObj
     * @param includeNull
     * @param keyCmp
     * @param tags
     * @param <T>
     * @return
     */
    public static<T> T copyBothWithTag(Object srcObj, T dstObj, boolean includeNull, ICompare<String> keyCmp, String ... tags){
        if(srcObj==null || dstObj==null){
            return dstObj;
        }
        if (keyCmp == null) {
            keyCmp = new EqualsCompare<String>();
        }
        Class srcCls=srcObj.getClass();
        Class dstCls=dstObj.getClass();
        List<PropertyAccessor> src=getLogicalReadableFieldsWithTags(srcCls,tags);
        List<PropertyAccessor> dst=getLogicalWritableFieldsWithTags(dstCls,tags);
        for(PropertyAccessor sitem : src){
            sitem.setInvokeObject(srcObj);
            Object sval=sitem.get();
            if(!includeNull && sval==null){
                continue;
            }
            for(PropertyAccessor ditem : dst){
                if (keyCmp.get(sitem.getName(), ditem.getName()) == 0) {
                    ditem.setInvokeObject(dstObj);
                    ditem.set(sval);
                    break;
                }
            }
        }
        return dstObj;
    }

    /**
     * 给给定对象设置包含指定TAG的字段设置同样的值value
     * @param obj
     * @param value
     * @param tags
     * @param <T>
     * @return
     */
    public static<T> T setValueByTags(T obj,Object value,String ... tags){
        if(obj==null || tags==null || tags.length==0){
             return obj;
        }
        Class clazz=obj.getClass();
        List<PropertyAccessor> list=getLogicalWritableFieldsWithTags(clazz,tags);
        for(PropertyAccessor item : list){
            item.setInvokeObject(obj);
            item.set(value);
        }
        return obj;
    }

    public static<T> T setValueByTagsWhenValueIsEmpty(T obj,Object value,String ... tags){
        return setValueByTagsWhenValueCondition(obj,value,new EmptyFilter<Object>(),tags);
    }

    public static<T> T setValueByTagsWhenValueIsNull(T obj,Object value,String ... tags){
        return setValueByTagsWhenValueCondition(obj,value,new NullFilter<Object>(),tags);
    }

    public static<T> T setValueByTagsWhenValueCondition(T obj, Object value, IFilter<Object> filter, String ... tags){
        if(obj==null || tags==null || tags.length==0){
            return obj;
        }
        if(filter==null){
            filter=new AlwaysTrueFilter<>();
        }
        Class clazz=obj.getClass();
        List<PropertyAccessor> list=getLogicalReadWriteFieldsWithTags(clazz,tags);
        for(PropertyAccessor item : list){
            item.setInvokeObject(obj);
            Object val=item.get();
            if (filter.test(val)) {
                item.set(value);
            }
        }
        return obj;
    }

    public static List<PropertyAccessor> getLogicalReadableFieldsWithTags(Class clazz,String ... tags){
        List<PropertyAccessor> list=ReflectResolver.getLogicalReadableFieldsWithAnnotations(clazz,false,BeanTag.class);
        return getLogicalFieldsWithTagsProxy(list,tags);
    }
    public static List<PropertyAccessor> getLogicalWritableFieldsWithTags(Class clazz,String ... tags){
        List<PropertyAccessor> list=ReflectResolver.getLogicalReadWriteFieldsWithAnnotations(clazz,false,BeanTag.class);
        return getLogicalFieldsWithTagsProxy(list,tags);
    }
    public static List<PropertyAccessor> getLogicalReadWriteFieldsWithTags(Class clazz,String ... tags){
        List<PropertyAccessor> list=ReflectResolver.getLogicalReadWriteFieldsWithAnnotations(clazz,false,BeanTag.class);
        return getLogicalFieldsWithTagsProxy(list,tags);
    }
    public static List<PropertyAccessor> getLogicalFieldsWithTagsProxy(List<PropertyAccessor> list,String ... tags){
        List<PropertyAccessor> ret=new ArrayList<>();
        if(tags==null || tags.length==0){
            return ret;
        }
        for(PropertyAccessor item : list){
            Field field=item.getField();
            if(field==null){
                continue;
            }
            BeanTag tag=ReflectResolver.findAnnotation(field, BeanTag.class,false);
            if(tag==null){
                continue;
            }
            String[] hasTags=tag.value();
            for(String htag : hasTags){
                boolean find=false;
                for(String rtag : tags){
                    if(htag.equals(rtag)){
                        ret.add(item);
                        find=true;
                        break;
                    }
                }
                if(find){
                    break;
                }
            }
        }
        return ret;
    }
}
