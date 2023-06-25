package i2f.core.safe;

import i2f.core.lang.functional.common.IGetter;
import i2f.core.lang.functional.common.ISetter;
import i2f.core.lang.functional.jvf.BiSupplier;


public class Nulls {
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static <T> T ifNull(T obj, T def) {
        if (obj == null) {
            return def;
        }
        return obj;
    }

    public static <T> T ifNull(Object obj, T nval, T eval) {
        if (obj == null) {
            return nval;
        }
        return eval;
    }

    public static <R, E> R get(E elem, IGetter<R, E> getter) {
        return get(elem, getter, null);
    }

    public static <R, E> R get(E elem, IGetter<R, E> getter, R defVal) {
        if (elem == null) {
            return defVal;
        }
        return getter.get(elem);
    }

    public static <E, V> void set(E elem, V val, ISetter<E, V> setter) {
        if (elem == null) {
            return;
        }
        setter.accept(elem, val);
    }


    public static <E, V> V ifNull(E elem, V val, IGetter<V, E> getter, ISetter<E, V> setter) {
        V ret = getter.get(elem);
        if (ret == null) {
            setter.accept(elem, val);
            ret = val;
        }
        return ret;
    }

    public static <E> void doNull(E elem, BiSupplier<?, E> exec) {
        doNull(elem, null, exec);
    }

    public static <R, E> R doNull(E elem, R defVal, BiSupplier<R, E> exec) {
        return doNull(elem, elem, defVal, exec);
    }

    public static <R, E, P> R doNull(E elem, P param, R defVal, BiSupplier<R, P> exec) {
        if (elem == null) {
            return exec.get(param);
        }
        return defVal;
    }

    public static <E> void doNotNull(E elem, BiSupplier<?, E> exec) {
        doNotNull(elem, null, exec);
    }

    public static <R, E> R doNotNull(E elem, R defVal, BiSupplier<R, E> exec) {
        return doNotNull(elem, elem, defVal, exec);
    }

    public static <R, E, P> R doNotNull(E elem, P param, R defVal, BiSupplier<R, P> exec) {
        if (elem != null) {
            return exec.get(param);
        }
        return defVal;
    }

}
