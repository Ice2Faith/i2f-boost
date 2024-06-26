package i2f.core.reflection.pkg.filter;


import i2f.core.annotations.remark.Author;
import i2f.core.reflection.pkg.data.ClassMetaData;
import i2f.core.reflection.reflect.core.ReflectResolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author ltb
 * @date 2021/9/23
 */
@Author("i2f")
public abstract class AbstractClassFilter implements IClassFilter{
    public enum FilterType{
        CLASS(1),
        SUPERCLASS(2),
        INTERFACES(4),
        CLASS_ANNOTATIONS(8),
        FIELDS(16),
        METHODS(32),
        CONSTRUCTORS(64),
        FIELD_ANNOTATIONS(128),
        METHOD_ANNOTATIONS(256);

        private int code;
        private FilterType(int code){
            this.code=code;
        }
        public int getCode(){
            return code;
        }
    }
    public static int maskCode(FilterType ... types){
        int code=0;
        for(FilterType item : types){
            code|= item.getCode();
        }
        return code;
    }
    public static boolean isMask(int code,FilterType type){
        return (type.getCode()&code)!=0;
    }
    public static boolean hasIncludeAnnotations(Set<Annotation> annotations,Class<? extends Annotation> ... requireAnnotations){
        if(annotations==null || annotations.size()==0){
            return false;
        }
        if(requireAnnotations==null || requireAnnotations.length==0){
            return false;
        }
        for(Class item : requireAnnotations){
            for(Annotation ait : annotations){
                if(item.equals(ait.annotationType())){
                    return true;
                }
                Set<Annotation> anns=ReflectResolver.getAnnotatedAnnotationsNext(ait.annotationType());
                for(Annotation pait : anns){
                    if(item.equals(pait.annotationType())){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    @Override
    public final boolean save(ClassMetaData classMetaData) {
        Class clazz= classMetaData.getClazz();
        int code=onMaskCode();
        if(code==0){
            return true;
        }
        if(isMask(code, FilterType.CLASS)){
            if(onClass(clazz)){
                return true;
            }
        }
        if(isMask(code, FilterType.SUPERCLASS)){
            try{
                Class suClazz=clazz.getSuperclass();
                if(suClazz==null){
                    return false;
                }
                if(onSuperClass(suClazz)){
                    return true;
                }
            }catch(Throwable e){

            }
        }
        if(isMask(code, FilterType.INTERFACES)){
            try{
                Class[] interfaces = clazz.getInterfaces();
                if(onInterfaces(interfaces)){
                    return true;
                }
            }catch(Throwable e){

            }
        }
        if(isMask(code, FilterType.CLASS_ANNOTATIONS)){
            try{
                Set<Annotation> anns= ReflectResolver.getAllAnnotations(clazz);
                if(onClassAnnotation(anns)){
                    return true;
                }
            }catch(Throwable e){

            }
        }
        if(isMask(code, FilterType.FIELDS)){
            try{
                Set<Field> fields=ReflectResolver.getAllFields(clazz);
                if(onFields(fields)){
                    return true;
                }
            }catch(Throwable e){

            }
        }
        if(isMask(code, FilterType.FIELD_ANNOTATIONS)){
            try{
                Set<Field> fields=ReflectResolver.getAllFields(clazz);
                for(Field item : fields){
                    try{
                        Set<Annotation> anns=ReflectResolver.getAllAnnotations(item);
                        if(onFieldAnnotations(item,anns)){
                            return true;
                        }
                    }catch(Throwable e){

                    }
                }
            }catch(Throwable e){

            }
        }
        if(isMask(code, FilterType.METHODS)){
            try{
                Set<Method> methods=ReflectResolver.getAllMethods(clazz);
                if(onMethods(methods)){
                    return true;
                }
            }catch(Throwable e){

            }
        }
        if(isMask(code, FilterType.METHOD_ANNOTATIONS)){
            try{
                Set<Method> methods=ReflectResolver.getAllMethods(clazz);
                for(Method item : methods){
                    try{
                        Set<Annotation> anns=ReflectResolver.getAllAnnotations(item);
                        if(onMethodAnnotations(item,anns)){
                            return true;
                        }
                    }catch(Throwable e){

                    }
                }
            }catch(Throwable e){

            }
        }
        if(isMask(code, FilterType.CONSTRUCTORS)){
            try{
                Set<Constructor> constructors=ReflectResolver.getAllConstructors(clazz);
                if(onConstructors(constructors)){
                    return true;
                }
            }catch(Throwable e){

            }
        }

        return false;
    }

    public boolean onMethodAnnotations(Method method, Set<Annotation> annotations){
        return true;
    }

    public boolean onFieldAnnotations(Field field, Set<Annotation> annotations){
        return true;
    }

    public int onMaskCode(){
        return 0;
    }

    public boolean onClass(Class clazz){
        return false;
    }

    public boolean onSuperClass(Class suClazz){
        return false;
    }

    public boolean onInterfaces(Class[] interfaces){
        return false;
    }

    public boolean onClassAnnotation(Set<Annotation> annotations){
        return false;
    }

    public boolean onFields(Set<Field> fields){
        return false;
    }

    public boolean onMethods(Set<Method> methods){
        return false;
    }

    public boolean onConstructors(Set<Constructor> constructors){
        return false;
    }
}
