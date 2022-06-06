package i2f.core.zplugin.databind;

import i2f.core.annotations.notice.Name;
import i2f.core.exception.BoostException;
import i2f.core.generate.core.ObjectFinder;
import i2f.core.reflect.core.ReflectResolver;
import i2f.core.zplugin.databind.annotations.DataBind;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author ltb
 * @date 2022/6/6 9:10
 * @desc
 */
public class DatabindResolver {
    public static<T> T getMethodBindValue(Method method,Object... args){
        DataBind ann= ReflectResolver.findAnnotation(method,DataBind.class,false);
        if(ann==null){
            throw new BoostException("not @DataBind annotation found on method.");
        }
        return getMethodBindValue(ann, method, args);
    }

    public static <T> T getMethodBindValue(DataBind ann, Method method, Object... args) {
        String bind= ann.bind();
        if(bind==null || "".equals(bind)){
            throw new BoostException("not @DataBind.bind defined.");
        }
        Object tar=null;
        if(bind.matches("^[+|-]?\\d+$")){
            Integer idx=Integer.parseInt(bind);
            if(idx<0){
                throw new BoostException("cannot process return value @DataBind.");
            }
            if(idx>= args.length){
                throw new BoostException("bad @DataBind.bind index that out of arguments length.");
            }
            tar= args[idx];
        }else{
            Parameter[] parameters= method.getParameters();
            for(int i=0;i<parameters.length;i++){
                Parameter item=parameters[i];
                String name=item.getName();
                Name nann=ReflectResolver.findAnnotation(item,Name.class,false);
                if(nann!=null){
                    if(!"".equals(nann.value())){
                        name=nann.value();
                    }
                }
                if(name.equals(bind)){
                    tar= args[i];
                }
            }
        }
        Object ret = ObjectFinder.getObjectByDotKeyWithReference(tar, ann.value());
        return (T)ret;
    }

}
