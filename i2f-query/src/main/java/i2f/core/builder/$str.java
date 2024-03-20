package i2f.core.builder;


import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2024/3/20 16:07
 * @desc
 */
public class $str<T extends $<Object,StringBuilder,String,T>> extends $<Object,StringBuilder,String,T> {

    public $str() {
        super(Object.class,
                e->new StringBuilder(),
                StringBuilder::append,
                (h,c)->c.toString());
    }

    public static<T extends $str<T>> $str<T> $_(){
        return new $str();
    }


    public T $sep(Object separator){
        if(separator!=null) {
            this.separator = () -> separator;
        }
        return (T)this;
    }

    public T $format(String format,Object ... args){
        return $(String.format(format,args));
    }

    public <V> T $trim(V val, Predicate<V> condition,
                       Function<V, String> prefix,
                       Function<V, String> suffix,
                       Function<V, List<String>> trimPrefix,
                       Function<V, List<String>> trimSuffix,
                       Function<V, String> innerMapper) {
        if (condition != null && !condition.test(val)) {
            return (T)this;
        }
        String text = innerMapper.apply(val);
        if (this.ignoreNull && text == null) {
            return (T)this;
        }
        if (text!=null && trimPrefix != null) {
            List<String> list = trimPrefix.apply(val);
            if (list != null) {
                String str = text;
                for (String item : list) {
                    str = str.trim();
                    if (str.startsWith(item)) {
                        str = str.substring(item.length());
                        break;
                    }
                }
                text=str;
            }
        }
        if (text!=null && trimSuffix != null) {
            List<String> list = trimSuffix.apply(val);
            if (list != null) {
                String str = text;
                for (String item : list) {
                    str = str.trim();
                    if (str.endsWith(item)) {
                        str = str.substring(0, str.length() - item.length());
                        break;
                    }
                }
                text=str;
            }
        }
        if(text!=null){
            text=text.trim();
        }
        if (this.ignoreNull && "".equals(text)) {
            return (T)this;
        }
        if (prefix != null) {
            String pre = prefix.apply(val);
            if (pre != null) {
                this.$(pre);
            }
        }
        this.$(text);
        if (suffix != null) {
            String suf = suffix.apply(val);
            if (suf != null) {
                this.$(suf);
            }
        }
        return (T)this;
    }

}
