package i2f.core.sql;

/**
 * @author Ice2Faith
 * @date 2024/3/20 14:05
 * @desc
 */
public class $<T extends $<T>> extends i2f.core.str.$<T> {

    public static<T extends $<T>> $<T> sql(){
        return new $();
    }

    public T sql(int str){
        this.builder.append(str);
        return (T)this;
    }

}
