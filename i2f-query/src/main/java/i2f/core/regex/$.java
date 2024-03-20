package i2f.core.regex;

/**
 * @author Ice2Faith
 * @date 2024/3/20 14:05
 * @desc
 */
public class $<T extends $<T>> extends i2f.core.sql.$<T> {

    public static<T extends $<T>> $<T> regex(){
        return new $();
    }

    public T regex(int str){
        this.builder.append(str);
        return (T)this;
    }

}
