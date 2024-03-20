package i2f.core.str;


import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/3/20 14:03
 * @desc
 */
public class $<T extends $<T>> {
    protected StringBuilder builder=new StringBuilder();

    public $(){

    }

    public static $ str(){
        return new $();
    }

    public T str(int str){
        this.builder.append(str);
        return (T)this;
    }

    public String $$(){
        return builder.toString();
    }
}
