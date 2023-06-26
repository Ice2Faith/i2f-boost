package i2f.core.safe;

import i2f.core.container.array.ArrayUtil;
import i2f.core.container.collection.CollectionUtil;
import i2f.core.container.map.MapUtil;
import i2f.core.lang.functional.jvf.BiSupplier;
import i2f.core.reflection.reflect.Reflects;
import i2f.core.type.str.Strings;

import java.util.Collection;
import java.util.Map;

public class Asserts {
    private boolean thr = false;
    private BiSupplier<? extends RuntimeException, String> thrProvider = (msg) -> new AssertException(msg);
    private boolean ok = true;
    private String msg = null;

    private Asserts(boolean thr) {
        this.thr = thr;
    }

    public static Asserts thr() {
        return new Asserts(true);
    }

    public static Asserts chk() {
        return new Asserts(false);
    }

    public boolean ok() {
        return ok;
    }

    public String msg() {
        return msg;
    }

    public Asserts except(BiSupplier<? extends RuntimeException, String> thrProvider) {
        this.thrProvider = thrProvider;
        return this;
    }

    private void thrEx(String msg) {
        if (!ok) {
            this.msg = msg;
            if (thr) {
                throw thrProvider.get(msg);
            }
        }
    }

    public Asserts isTrue(boolean condition, String msg) {
        if (!ok) {
            return this;
        }
        ok = !condition;
        thrEx(msg);
        return this;
    }

    public Asserts isFalse(boolean condition, String msg) {
        if (!ok) {
            return this;
        }
        ok = condition;
        thrEx(msg);
        return this;
    }

    public Asserts isNull(Object obj) {
        return isNull(obj, "cloud not be null obj.");
    }

    public Asserts isNull(Object obj, String msg) {
        return isTrue(Nulls.isNull(obj), msg);
    }

    public Asserts notNull(Object obj) {
        return notNull(obj, "cloud not be null obj.");
    }

    public Asserts notNull(Object obj, String msg) {
        return isTrue(!Nulls.isNull(obj), msg);
    }

    public <T> Asserts isEmpty(T[] arr) {
        return isEmpty(arr, "cloud not be empty str.");
    }

    public <T> Asserts isEmpty(T[] arr, String msg) {
        return isTrue(ArrayUtil.isEmpty(arr), msg);
    }

    public <T> Asserts notIsEmpty(T[] arr) {
        return notIsEmpty(arr, "str require is empty.");
    }

    public <T> Asserts notIsEmpty(T[] arr, String msg) {
        return isFalse(ArrayUtil.isEmpty(arr), msg);
    }


    public Asserts isEmpty(String str) {
        return isEmpty(str, "cloud not be empty str.");
    }

    public Asserts isEmpty(String str, String msg) {
        return isTrue(Strings.isEmpty(str), msg);
    }

    public Asserts notIsEmpty(String str) {
        return notIsEmpty(str, "str require is empty.");
    }

    public Asserts notIsEmpty(String str, String msg) {
        return isFalse(Strings.isEmpty(str), msg);
    }

    public Asserts isEmpty(Collection<?> col) {
        return isEmpty(col, "cloud not be empty collection.");
    }

    public Asserts isEmpty(Collection<?> col, String msg) {
        return isTrue(CollectionUtil.isEmpty(col), msg);
    }

    public Asserts notIsEmpty(Collection<?> col) {
        return notIsEmpty(col, "collection require is empty.");
    }

    public Asserts notIsEmpty(Collection<?> col, String msg) {
        return isFalse(CollectionUtil.isEmpty(col), msg);
    }

    public Asserts isEmpty(Map<?, ?> map) {
        return isEmpty(map, "cloud not be empty map.");
    }

    public Asserts isEmpty(Map<?, ?> map, String msg) {
        return isTrue(MapUtil.isEmpty(map), msg);
    }

    public Asserts notIsEmpty(Map<?, ?> map) {
        return notIsEmpty(map, "map require is empty.");
    }

    public Asserts notIsEmpty(Map<?, ?> map, String msg) {
        return isFalse(MapUtil.isEmpty(map), msg);
    }

    public Asserts isBlank(String str) {
        return isBlank(str, "cloud not be blank str.");
    }

    public Asserts isBlank(String str, String msg) {
        return isTrue(Strings.isBlank(str), msg);
    }

    public Asserts notIsBlank(String str) {
        return notIsBlank(str, "str require is blank.");
    }

    public Asserts notIsBlank(String str, String msg) {
        return isFalse(Strings.isBlank(str), msg);
    }

    public Asserts isArray(Object obj) {
        return isArray(obj, "cloud not be array type obj.");
    }

    public Asserts isArray(Object obj, String msg) {
        return isTrue(ArrayUtil.isArray(obj), msg);
    }

    public Asserts notIsArray(Object obj) {
        return notIsArray(obj, "obj require is array type.");
    }

    public Asserts notIsArray(Object obj, String msg) {
        return isFalse(ArrayUtil.isArray(obj), msg);
    }

    public Asserts isTypeOf(Class clazz, Class type) {
        return isTypeOf(clazz, type, clazz + " cloud not be type of " + type);
    }

    public Asserts isTypeOf(Class clazz, Class type, String msg) {
        return isTrue(Reflects.isTypeOf(clazz, type), msg);
    }

    public Asserts notIsTypeOf(Class clazz, Class type) {
        return notIsTypeOf(clazz, type, clazz + " require is type of " + type);
    }

    public Asserts notIsTypeOf(Class clazz, Class type, String msg) {
        return isFalse(Reflects.isTypeOf(clazz, type), msg);
    }

    public Asserts instanceOf(Object obj, Class type) {
        return instanceOf(obj, type, "obj cloud not be type of " + type);
    }

    public Asserts instanceOf(Object obj, Class type, String msg) {
        return isTrue(Reflects.instanceOf(obj, type), msg);
    }

    public Asserts notInstanceOf(Object obj, Class type) {
        return notInstanceOf(obj, type, "obj require is type of " + type);
    }

    public Asserts notInstanceOf(Object obj, Class type, String msg) {
        return isFalse(Reflects.instanceOf(obj, type), msg);
    }

    public <T extends Comparable<T>> Asserts isLt(T val, T cmp) {
        return isLt(val, cmp, "obj cloud not be lower than " + cmp);
    }

    public <T extends Comparable<T>> Asserts isLt(T val, T cmp, String msg) {
        return isTrue(Safes.compare(val, cmp) < 0, msg);
    }

    public <T extends Comparable<T>> Asserts isLte(T val, T cmp) {
        return isLte(val, cmp, "obj cloud not be lower than equal " + cmp);
    }

    public <T extends Comparable<T>> Asserts isLte(T val, T cmp, String msg) {
        return isTrue(Safes.compare(val, cmp) <= 0, msg);
    }

    public <T extends Comparable<T>> Asserts isGt(T val, T cmp) {
        return isGt(val, cmp, "obj cloud not be gather than " + cmp);
    }

    public <T extends Comparable<T>> Asserts isGt(T val, T cmp, String msg) {
        return isTrue(Safes.compare(val, cmp) > 0, msg);
    }

    public <T extends Comparable<T>> Asserts isGte(T val, T cmp) {
        return isGte(val, cmp, "obj cloud not be gather than equal " + cmp);
    }

    public <T extends Comparable<T>> Asserts isGte(T val, T cmp, String msg) {
        return isTrue(Safes.compare(val, cmp) >= 0, msg);
    }

    public <T extends Comparable<T>> Asserts isEq(T val, T cmp) {
        return isEq(val, cmp, "obj cloud not be  equal " + cmp);
    }

    public <T extends Comparable<T>> Asserts isEq(T val, T cmp, String msg) {
        return isTrue(Safes.compare(val, cmp) == 0, msg);
    }

    public <T extends Comparable<T>> Asserts isNeq(T val, T cmp) {
        return isNeq(val, cmp, "obj cloud not be not equal " + cmp);
    }

    public <T extends Comparable<T>> Asserts isNeq(T val, T cmp, String msg) {
        return isTrue(Safes.compare(val, cmp) != 0, msg);
    }

}

