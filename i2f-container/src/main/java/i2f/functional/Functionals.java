package i2f.functional;


import java.util.Comparator;
import java.util.function.*;

/**
 * @author Ice2Faith
 * @date 2024/4/19 11:21
 * @desc 主要职责是将方法引用或者lambda转换为具体的接口类
 * Functionals.supplier(()->new ArrayList<>())
 */
public class Functionals {
    public static <T> Supplier<T> supplier(Supplier<T> supplier) {
        return supplier;
    }

    public static <T, R> Function<T, R> function(Function<T, R> function) {
        return function;
    }

    public static <T, V, R> BiFunction<T, V, R> biFunction(BiFunction<T, V, R> biFunction) {
        return biFunction;
    }

    public static <T, V, R> Function<T, R> function(BiFunction<T, V, R> biFunction, V val) {
        return t -> biFunction.apply(t, val);
    }

    public static <T> Predicate<T> predicate(Predicate<T> predicate) {
        return predicate;
    }

    public static <T, V> BiPredicate<T, V> biPredicate(BiPredicate<T, V> biPredicate) {
        return biPredicate;
    }

    public static <T, V> Predicate<T> predicate(BiPredicate<T, V> biPredicate, V val) {
        return t -> biPredicate.test(t, val);
    }

    public static <T> Consumer<T> consumer(Consumer<T> consumer) {
        return consumer;
    }

    public static <T, V> BiConsumer<T, V> biConsumer(BiConsumer<T, V> biConsumer) {
        return biConsumer;
    }

    public static <T, V> Consumer<T> consumer(BiConsumer<T, V> biConsumer, V val) {
        return t -> biConsumer.accept(t, val);
    }

    public static <T> Comparator<T> comparator(Comparator<T> comparator) {
        return comparator;
    }

    public static <T> BinaryOperator<T> binaryOperator(BinaryOperator<T> binaryOperator) {
        return binaryOperator;
    }

    public static BooleanSupplier booleanSupplier(BooleanSupplier booleanSupplier) {
        return booleanSupplier;
    }

    public static DoubleBinaryOperator doubleBinaryOperator(DoubleBinaryOperator doubleBinaryOperator) {
        return doubleBinaryOperator;
    }

    public static DoubleConsumer doubleConsumer(DoubleConsumer doubleConsumer) {
        return doubleConsumer;
    }

    public static <R> DoubleFunction<R> doubleFunction(DoubleFunction<R> doubleFunction) {
        return doubleFunction;
    }

    public static DoublePredicate doublePredicate(DoublePredicate doublePredicate) {
        return doublePredicate;
    }

    public static DoubleSupplier doubleSupplier(DoubleSupplier doubleSupplier) {
        return doubleSupplier;
    }

    public static DoubleToIntFunction doubleToIntFunction(DoubleToIntFunction doubleToIntFunction) {
        return doubleToIntFunction;
    }

    public static DoubleToLongFunction doubleToLongFunction(DoubleToLongFunction doubleToLongFunction) {
        return doubleToLongFunction;
    }

    public static DoubleUnaryOperator doubleUnaryOperator(DoubleUnaryOperator doubleUnaryOperator) {
        return doubleUnaryOperator;
    }

    public static IntBinaryOperator intBinaryOperator(IntBinaryOperator intBinaryOperator) {
        return intBinaryOperator;
    }

    public static IntConsumer intConsumer(IntConsumer intConsumer) {
        return intConsumer;
    }

    public static <R> IntFunction<R> intFunction(IntFunction<R> intFunction) {
        return intFunction;
    }

    public static IntPredicate intPredicate(IntPredicate intPredicate) {
        return intPredicate;
    }

    public static IntSupplier intSupplier(IntSupplier intSupplier) {
        return intSupplier;
    }

    public static IntToDoubleFunction intToDoubleFunction(IntToDoubleFunction intToDoubleFunction) {
        return intToDoubleFunction;
    }

    public static IntToLongFunction intToLongFunction(IntToLongFunction intToLongFunction) {
        return intToLongFunction;
    }

    public static IntUnaryOperator intUnaryOperator(IntUnaryOperator intUnaryOperator) {
        return intUnaryOperator;
    }

    public static LongBinaryOperator longBinaryOperator(LongBinaryOperator longBinaryOperator) {
        return longBinaryOperator;
    }

    public static LongConsumer longConsumer(LongConsumer longConsumer) {
        return longConsumer;
    }

    public static <R> LongFunction<R> longFunction(LongFunction<R> longFunction) {
        return longFunction;
    }

    public static LongPredicate longPredicate(LongPredicate longPredicate) {
        return longPredicate;
    }

    public static LongSupplier longSupplier(LongSupplier longSupplier) {
        return longSupplier;
    }

    public static LongToDoubleFunction longToDoubleFunction(LongToDoubleFunction longToDoubleFunction) {
        return longToDoubleFunction;
    }

    public static LongToIntFunction longToIntFunction(LongToIntFunction longToIntFunction) {
        return longToIntFunction;
    }

    public static LongUnaryOperator longUnaryOperator(LongUnaryOperator longUnaryOperator) {
        return longUnaryOperator;
    }

    public static <T> ObjDoubleConsumer<T> objDoubleConsumer(ObjDoubleConsumer<T> objDoubleConsumer) {
        return objDoubleConsumer;
    }

    public static <T> ObjIntConsumer<T> objIntConsumer(ObjIntConsumer<T> objIntConsumer) {
        return objIntConsumer;
    }

    public static <T> ObjLongConsumer<T> objLongConsumer(ObjLongConsumer<T> objLongConsumer) {
        return objLongConsumer;
    }

    public static <T, V> ToDoubleBiFunction<T, V> toDoubleBiFunction(ToDoubleBiFunction<T, V> toDoubleBiFunction) {
        return toDoubleBiFunction;
    }

    public static <T> ToDoubleFunction<T> toDoubleFunction(ToDoubleFunction<T> toDoubleFunction) {
        return toDoubleFunction;
    }

    public static <T, V> ToIntBiFunction<T, V> toIntBiFunction(ToIntBiFunction<T, V> toIntBiFunction) {
        return toIntBiFunction;
    }

    public static <T> ToIntFunction<T> toIntFunction(ToIntFunction<T> toIntFunction) {
        return toIntFunction;
    }

    public static <T, V> ToLongBiFunction<T, V> toLongBiFunction(ToLongBiFunction<T, V> toLongBiFunction) {
        return toLongBiFunction;
    }

    public static <T> ToLongFunction<T> toLongFunction(ToLongFunction<T> toLongFunction) {
        return toLongFunction;
    }

    public static <T> UnaryOperator<T> unaryOperator(UnaryOperator<T> unaryOperator) {
        return unaryOperator;
    }
}
