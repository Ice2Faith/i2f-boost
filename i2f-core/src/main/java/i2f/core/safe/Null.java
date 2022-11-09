package i2f.core.safe;


import i2f.core.interfaces.IExecutor;
import i2f.core.lambda.functional.preset.IBeanGetter;
import i2f.core.lambda.functional.preset.IBeanSetter;

/**
 * @author ltb
 * @date 2022/10/27 10:27
 * @desc
 */
public class Null {
    public static <E, R> R get(E elem, IBeanGetter<R, E> getter) {
        return get(elem, getter, null);
    }

    public static <E, R> R get(E elem, IBeanGetter<R, E> getter, R defVal) {
        if (elem == null) {
            return defVal;
        }
        return getter.pick(elem);
    }

    public static <E, V> void set(E elem, V val, IBeanSetter<E, V> setter) {
        if (elem == null) {
            return;
        }
        setter.accept(elem, val);
    }

    public static <E> E ifnull(E elem, E defVal) {
        if (elem == null) {
            return defVal;
        }
        return elem;
    }

    public static <E, V> V ifnull(E elem, V val, IBeanGetter<V, E> getter, IBeanSetter<E, V> setter) {
        V ret = getter.pick(elem);
        if (ret == null) {
            setter.accept(elem, val);
            ret = val;
        }
        return ret;
    }

    public static <E> void doNull(E elem, IExecutor<E, ?> exec) {
        doNull(elem, null, exec);
    }

    public static <E, R> R doNull(E elem, R defVal, IExecutor<E, R> exec) {
        return doNull(elem, elem, defVal, exec);
    }

    public static <E, R, P> R doNull(E elem, P param, R defVal, IExecutor<P, R> exec) {
        if (elem == null) {
            return exec.exec(param);
        }
        return defVal;
    }

    public static <E> void doNotNull(E elem, IExecutor<E, ?> exec) {
        doNotNull(elem, null, exec);
    }

    public static <E, R> R doNotNull(E elem, R defVal, IExecutor<E, R> exec) {
        return doNotNull(elem, elem, defVal, exec);
    }

    public static <E, R, P> R doNotNull(E elem, P param, R defVal, IExecutor<P, R> exec) {
        if (elem != null) {
            return exec.exec(param);
        }
        return defVal;
    }
}
