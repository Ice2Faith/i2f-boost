package i2f.lambda.test;

import i2f.lambda.Lambdas;

import java.io.IOException;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/3/29 11:12
 * @desc
 */
public class TestFunctional {

    public static void main(String[] args) throws Exception {

        System.out.println(Lambdas.METHOD.get(TestFunctional::consumer));
        System.out.println(Lambdas.METHOD.get(TestFunctional::consumerInt));
        System.out.println(Lambdas.METHOD.get(TestFunctional::consumerException));
        System.out.println(Lambdas.METHOD.get(TestFunctional::consumerIoException));
        System.out.println(Lambdas.METHOD.get(TestFunctional::consumerMulException));
        System.out.println(Lambdas.METHOD.get(TestFunctional::predicate));
        System.out.println(Lambdas.METHOD.get(TestFunctional::supplier));

        testConsumer(TestFunctional::consumer);

        // 无异常的接口，不接收有异常的实现
//        testConsumer(TestFunctional::consumerException); // error
//        testConsumer(TestFunctional::consumerIoException); // error

        // 包装类型可兼容基本类型
        testConsumer(TestFunctional::consumerInt);
        testIntConsumer(TestFunctional::consumer);
        testIntConsumer(TestFunctional::consumerInt);
        testIntConsumer(TestFunctional::consumerException);

        // 带异常的接口，可以接受无异常的和子异常的
        testExceptionConsumer(TestFunctional::consumer);
        testExceptionConsumer(TestFunctional::consumerException);
        testExceptionConsumer(TestFunctional::consumerIoException);
        testExceptionConsumer(TestFunctional::consumerMulException);
        testExceptionConsumer(TestFunctional::consumerInt);

        testIoExceptionConsumer(TestFunctional::consumer);
//        testIoExceptionConsumer(TestFunctional::consumerException); // error
        testIoExceptionConsumer(TestFunctional::consumerIoException);
//        testIoExceptionConsumer(TestFunctional::consumerMulException); // error
        testIoExceptionConsumer(TestFunctional::consumerInt);

        testPredicate(TestFunctional::predicate);

        // array类型，实际上是object，可以进行泛型
        testSupplier(TestFunctional::supplier);
        testByteArraySupplier(TestFunctional::supplier);

        // 总结
        // 方法引用的检查点：返回值，参数类型，异常类型
        // 返回值类型，参数类型需要兼容，包含基本类型与包装类型的兼容，个数也要相等
        // 异常类型只需要兼容，个数可以少，但是需要时公共异常父类
        // array类型，实际上是object，可以进行泛型
    }

    @FunctionalInterface
    public interface IntConsumer {
        void accept(int val) throws Exception;
    }

    @FunctionalInterface
    public interface ExceptionConsumer<T> {
        void accept(T val) throws Exception;
    }

    @FunctionalInterface
    public interface IoExceptionConsumer<T> {
        void accept(T val) throws IOException;
    }

    @FunctionalInterface
    public interface ByteArraySupplier {
        byte[] accept() throws IOException;
    }

    public static void testByteArraySupplier(ByteArraySupplier supplier) {

    }

    public static <T> void testSupplier(Supplier<T> supplier) {

    }

    public static <T> void testConsumer(Consumer<T> consumer) {

    }

    public static <T> void testExceptionConsumer(ExceptionConsumer<T> consumer) {

    }

    public static <T> void testIoExceptionConsumer(IoExceptionConsumer<T> consumer) {

    }

    public static <T> void testIntConsumer(IntConsumer consumer) {

    }

    public static <T> void testPredicate(Predicate<T> predicate) {

    }

    public static Boolean predicate(Integer val) {

        return true;
    }

    public static byte[] supplier() {

        return null;
    }

    public static void consumer(Integer val) {

    }


    public static void consumerInt(int val) {

    }

    public static void consumerException(Integer val) throws Exception {

    }

    public static void consumerIoException(Integer val) throws IOException {

    }

    public static void consumerMulException(Integer val) throws IOException, SQLException {

    }
}
