package i2f.reflect.vistor.impl;

import i2f.reflect.ReflectResolver;
import i2f.reflect.vistor.Visitor;

/**
 * @author Ice2Faith
 * @date 2024/3/1 19:03
 * @desc
 */
public class FieldVisitor implements Visitor {
    private Object ivkObj;
    private String fieldName;

    public FieldVisitor(Object ivkObj, String fieldName) {
        this.ivkObj = ivkObj;
        this.fieldName = fieldName;
    }

    @Override
    public Object get() {
        try{
            return ReflectResolver.valueGet(ivkObj,fieldName);
        }catch(Exception e){
            throw new IllegalStateException("get field ["+this.fieldName+"] value error in class ["+ivkObj.getClass().getName()+"]",e);
        }
    }

    @Override
    public void set(Object value) {
        try{
            ReflectResolver.valueSet(ivkObj,fieldName,value);
        }catch(Exception e){
            throw new IllegalStateException("set field ["+this.fieldName+"] value error in class ["+ivkObj.getClass().getName()+"]",e);
        }
    }

    @Override
    public Object parent() {
        return this.ivkObj;
    }
}
