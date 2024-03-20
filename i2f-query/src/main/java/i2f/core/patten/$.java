package i2f.core.patten;

/**
 * @author Ice2Faith
 * @date 2024/3/20 14:05
 * @desc
 */
public class $<T extends $<T>> extends i2f.core.regex.$<T> {



    public static<T extends $<T>> $<T> patten(){
        return new $();
    }

    public T patten(int str){
        this.builder.append(str);
        return (T)this;
    }

}
