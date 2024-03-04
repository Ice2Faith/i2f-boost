package i2f.check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.*;

/**
 * @author Ice2Faith
 * @date 2024/3/4 14:11
 * @desc
 */
public class Checker<T> {

    private List<String> errList = new ArrayList<>();
    private T curr;
    private boolean once = false;

    private Checker(T val, Checker<?> checker) {
        this.curr = val;
        if (checker != null) {
            this.once = checker.once;
            this.errList.addAll(checker.errList);
        }
    }

    public static Checker<Object> begin() {
        return new Checker<>(null, null);
    }

    public static <E> Checker<E> begin(E val) {
        return new Checker<>(val, null);
    }

    public static <E> Checker<E> begin(E val,Predicate<E> predicate,Function<E,String> errorMessageSupplier){
        return begin(val).test(predicate,errorMessageSupplier);
    }

    public static <E> Checker<E> begin(E val,Predicate<E> predicate,String errorMsg){
        return begin(val).test(predicate,errorMsg);
    }

    public boolean get() {
        return errList.isEmpty();
    }

    public void end(BiConsumer<List<String>, T> consumer) {
        consumer.accept(this.errList, this.curr);
    }

    public void error(Consumer<List<String>> errorsConsumer) {
        if (this.errList.isEmpty()) {
            return;
        }
        errorsConsumer.accept(this.errList);
    }

    public void except(Function<List<String>, ? extends RuntimeException> exceptionMapper) {
        if (this.errList.isEmpty()) {
            return;
        }
        RuntimeException ex = exceptionMapper.apply(this.errList);
        throw ex;
    }

    public String errorMessage() {
        return errorMessage("\n", null, null);
    }

    public String errorMessage(String separator) {
        return errorMessage(separator, null, null);
    }

    public String errorMessage(String separator, String prefix, String suffix) {
        if (errList.isEmpty()) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        if (prefix != null) {
            builder.append(prefix);
        }
        boolean isFirst = true;
        for (String item : this.errList) {
            if (!isFirst) {
                if (separator != null) {
                    builder.append(separator);
                }
            }
            builder.append(item);
            isFirst = false;
        }
        if (suffix != null) {
            builder.append(suffix);
        }
        return builder.toString();
    }

    public void exceptMessage(Function<String, ? extends RuntimeException> exceptionMapper) {
        exceptMessage(exceptionMapper, "\n", null, null);
    }

    public void exceptMessage(Function<String, ? extends RuntimeException> exceptionMapper, String separator) {
        exceptMessage(exceptionMapper, separator, null, null);
    }

    public void exceptMessage(Function<String, ? extends RuntimeException> exceptionMapper, String separator, String prefix, String suffix) {
        if (this.errList.isEmpty()) {
            return;
        }
        String msg = errorMessage(separator, prefix, suffix);
        RuntimeException ex = exceptionMapper.apply(msg);
        throw ex;
    }

    public String firstError() {
        return errList.isEmpty() ? null : errList.get(0);
    }

    public List<String> errors() {
        return this.errList;
    }

    public T value() {
        return this.curr;
    }

    public Checker<T> once() {
        this.once = true;
        return this;
    }

    public <E> Checker<E> next(E val) {
        return new Checker<>(val, this);
    }

    public <E> Checker<E> test(E val, Predicate<E> predicate, Function<E, String> errorMessageSupplier) {
        return new Checker<>(val, this).test(predicate, errorMessageSupplier);
    }

    public <E, U> Checker<E> testBi(E val, BiPredicate<E, U> predicate, U biValue, BiFunction<E, U, String> errorMessageSupplier) {
        return new Checker<>(val, this).testBi(predicate, biValue, errorMessageSupplier);
    }

    public <E> Checker<E> not(E val, Predicate<E> predicate, Function<E, String> errorMessageSupplier) {
        return new Checker<>(val, this).not(predicate, errorMessageSupplier);
    }

    public <E, U> Checker<E> notBi(E val, BiPredicate<E, U> predicate, U biValue, BiFunction<E, U, String> errorMessageSupplier) {
        return new Checker<>(val, this).notBi(predicate, biValue, errorMessageSupplier);
    }

    public Checker<T> test(Predicate<T> predicate, Function<T, String> errorMessageSupplier) {
        if (once && !this.errList.isEmpty()) {
            return this;
        }
        if (!predicate.test(this.curr)) {
            errList.add(errorMessageSupplier.apply(this.curr));
        }
        return this;
    }

    public Checker<T> testOr(Function<T, String> errorMessageSupplier, Predicate<T>... predicates) {
        if (once && !this.errList.isEmpty()) {
            return this;
        }
        boolean ok = false;
        for (Predicate<T> predicate : predicates) {
            if (predicate.test(this.curr)) {
                ok = true;
                break;
            }
        }
        if (!ok) {
            errList.add(errorMessageSupplier.apply(this.curr));
        }

        return this;
    }

    public Checker<T> testOr(String errorMsg, Predicate<T>... predicates) {
        return testOr(e -> errorMsg, predicates);
    }

    public Checker<T> test(Predicate<T> predicate, String errorMsg) {
        return test(predicate, e -> errorMsg);
    }

    public <E> Checker<T> testBi(BiPredicate<T, E> predicate, E biValue, BiFunction<T, E, String> errorMessageSupplier) {
        if (once && !this.errList.isEmpty()) {
            return this;
        }
        if (!predicate.test(this.curr, biValue)) {
            errList.add(errorMessageSupplier.apply(this.curr, biValue));
        }
        return this;
    }

    public <E> Checker<T> testBi(BiPredicate<T, E> predicate, E biValue, String errorMsg) {
        return testBi(predicate, biValue, (e, v) -> errorMsg);
    }

    public Checker<T> not(Predicate<T> predicate, Function<T, String> errorMessageSupplier) {
        if (once && !this.errList.isEmpty()) {
            return this;
        }
        if (predicate.test(this.curr)) {
            errList.add(errorMessageSupplier.apply(this.curr));
        }
        return this;
    }

    public Checker<T> notOr(Function<T, String> errorMessageSupplier, Predicate<T>... predicates) {
        if (once && !this.errList.isEmpty()) {
            return this;
        }
        boolean ok = true;
        for (Predicate<T> predicate : predicates) {
            if (predicate.test(this.curr)) {
                ok = false;
                break;
            }
        }

        if (!ok) {
            errList.add(errorMessageSupplier.apply(this.curr));
        }

        return this;
    }

    public Checker<T> notOr(String errorMsg, Predicate<T>... predicates) {
        return notOr((e) -> errorMsg, predicates);
    }

    public Checker<T> not(Predicate<T> predicate, String errorMsg) {
        return not(predicate, e -> errorMsg);
    }

    public <E> Checker<T> notBi(BiPredicate<T, E> predicate, E biValue, BiFunction<T, E, String> errorMessageSupplier) {
        if (once && !this.errList.isEmpty()) {
            return this;
        }
        if (predicate.test(this.curr, biValue)) {
            errList.add(errorMessageSupplier.apply(this.curr, biValue));
        }
        return this;
    }

    public <E> Checker<T> notBi(BiPredicate<T, E> predicate, E biValue, String errorMsg) {
        return notBi(predicate, biValue, (e, v) -> errorMsg);
    }

    public <E> Checker<E> map(Function<T, E> mapper) {
        E elem = mapper.apply(this.curr);
        return next(elem);
    }

    public <E, R> Checker<R> mapBi(BiFunction<T, E, R> mapper, E biValue) {
        R elem = mapper.apply(this.curr, biValue);
        return next(elem);
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static boolean isBlank(String str){
        return str==null || "".equals(str.trim());
    }

    public static boolean isEmpty(Collection<?> col) {
        return col == null || col.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isArray(Object obj) {
        if (obj == null) {
            return false;
        }
        return obj.getClass().isArray();
    }

    public static boolean isType(Object obj, Class<?> type) {
        if (obj == null) {
            return false;
        }
        Class<?> clazz = obj.getClass();
        return clazz.equals(type) || type.isAssignableFrom(clazz);
    }
}
