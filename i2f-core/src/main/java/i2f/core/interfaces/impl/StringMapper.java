package i2f.core.interfaces.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.interfaces.IMap;

/**
 * @author ltb
 * @date 2022/3/25 14:12
 * @desc
 */
@Author("i2f")
public class StringMapper<T> implements IMap<T,String> {
    private boolean null2Empty;
    public StringMapper(){
        this.null2Empty=false;
    }
    public StringMapper(boolean null2Empty){
        this.null2Empty=null2Empty;
    }
    @Override
    public String map(T val) {
        if(val==null && null2Empty){
            return "";
        }
        return String.valueOf(val);
    }
}
