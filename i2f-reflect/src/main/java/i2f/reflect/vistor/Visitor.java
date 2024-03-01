package i2f.reflect.vistor;

import i2f.reflect.vistor.impl.VisitorParser;

/**
 * @author Ice2Faith
 * @date 2024/3/1 19:00
 * @desc
 */
public interface Visitor {
    Object get();
    void set(Object value);

    default Object parent(){
        throw new IllegalStateException("not parent support");
    }

    static Visitor visit(String expression, Object rootObj){
        return VisitorParser.visit(expression,rootObj);
    }

    static Visitor visit(String expression,Object rootObj,Object paramObj) {
        return VisitorParser.visit(expression, rootObj,  paramObj);
    }
}
