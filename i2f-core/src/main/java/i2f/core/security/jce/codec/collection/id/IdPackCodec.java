package i2f.core.security.jce.codec.collection.id;

import i2f.core.security.jce.codec.collection.IStringCollectionCodec;
import i2f.core.type.str.Appender;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2023/6/27 16:33
 * @desc
 */
public class IdPackCodec<T, C extends Collection<T>> implements IStringCollectionCodec<T, C> {
    public static IdPackCodec<Object, Set<Object>> INSTANCE_OBJ = new IdPackCodec<>(LinkedHashSet::new, String::valueOf);
    public static IdPackCodec<Long, Set<Long>> INSTANCE_LONG = new IdPackCodec<>(LinkedHashSet::new, Long::parseLong);
    public static IdPackCodec<Integer, Set<Integer>> INSTANCE_INT = new IdPackCodec<>(LinkedHashSet::new, Integer::parseInt);
    public static IdPackCodec<String, Set<String>> INSTANCE_STRING = new IdPackCodec<>(LinkedHashSet::new, String::valueOf);

    protected Supplier<C> supplier;
    protected Function<String, T> converter;
    protected String separator = ",";

    public IdPackCodec(Supplier<C> supplier, Function<String, T> converter) {
        this.supplier = supplier;
        this.converter = converter;
    }

    public IdPackCodec(Supplier<C> supplier, Function<String, T> converter, String separator) {
        this.supplier = supplier;
        this.converter = converter;
        this.separator = separator;
    }

    @Override
    public String encode(C data) {
        if (data == null) {
            return null;
        }
        return Appender.builder().addCollection(data, separator).get();
    }

    @Override
    public C decode(String enc) {
        if (enc == null) {
            return null;
        }
        C ret = supplier.get();
        String[] arr = enc.split(separator);
        for (String item : arr) {
            if ("".equals(item)) {
                continue;
            }
            try {
                T val = converter.apply(item);
                ret.add(val);
            } catch (Exception e) {

            }
        }
        return ret;
    }
}
