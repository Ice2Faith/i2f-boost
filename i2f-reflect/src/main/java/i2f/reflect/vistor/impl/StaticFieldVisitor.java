package i2f.reflect.vistor.impl;

import i2f.reflect.ReflectResolver;
import i2f.reflect.vistor.Visitor;

/**
 * @author Ice2Faith
 * @date 2024/3/1 19:04
 * @desc
 */
public class StaticFieldVisitor implements Visitor {
    private Class<?> clazz;
    private String fieldName;

    public StaticFieldVisitor(Class<?> clazz, String fieldName) {
        this.clazz = clazz;
        this.fieldName = fieldName;
    }

    @Override
    public Object get() {
        try{
            return ReflectResolver.valueGetStatic(clazz,fieldName);
        }catch(Exception e){
            throw new IllegalStateException("get static field ["+this.fieldName+"] value error in class ["+clazz.getName()+"]",e);
        }
    }

    @Override
    public void set(Object value) {
        try{
            ReflectResolver.valueSetStatic(clazz,fieldName,value);
        }catch(Exception e){
            throw new IllegalStateException("set static field ["+this.fieldName+"] value error in class ["+clazz.getName()+"]",e);
        }
    }

    @Override
    public Object parent() {
        return this.clazz;
    }
}
