package i2f.trace.test;

import i2f.trace.ThreadTrace;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * @author Ice2Faith
 * @date 2024/3/29 11:12
 * @desc
 */
public class TestFunctional {

    public static void main(String[] args) throws Exception {

        trace(1);
        trace((Integer)1);
        trace("str");
        trace(true,null,"1",2,null);
        predicate(1);
        supplier();
        consumer(null);
        consumerInt(1);
        consumerException(null);
        consumerIoException(null);
        consumerMulException(null);
        System.out.println("------------------------------------");

    }


    public static Boolean predicate(Integer val) {
        Method method = ThreadTrace.currentMethod(val);
        System.out.println(method);
        return true;
    }

    public static byte[] supplier() {
        Method method = ThreadTrace.currentMethod();
        System.out.println(method);
        return null;
    }

    public static void consumer(Integer val) {
        Method method = ThreadTrace.currentMethod(val);
        System.out.println(method);
    }

    public static void trace(int val) {
        Method method = ThreadTrace.currentMethod(val);
        System.out.println(method);
    }

    public static void trace(Integer val) {
        Method method = ThreadTrace.currentMethod(val);
        System.out.println(method);
    }

    public static void trace(String val) {
        Method method = ThreadTrace.currentMethod(val);
        System.out.println(method);
    }

    public static void trace(boolean ok,String val,String str,Integer ite,Boolean bl) {
        Method method = ThreadTrace.currentMethod(ok,val,str,ite,bl);
        System.out.println(method);
    }

    public static void consumerInt(int val) {
        Method method = ThreadTrace.currentMethod(val);
        System.out.println(method);
    }

    public static void consumerException(Integer val) throws Exception {
        Method method = ThreadTrace.currentMethod(val);
        System.out.println(method);
    }

    public static void consumerIoException(Integer val) throws IOException {
        Method method = ThreadTrace.currentMethod(val);
        System.out.println(method);
    }

    public static void consumerMulException(Integer val) throws IOException, SQLException {
        Method method = ThreadTrace.currentMethod(val);
        System.out.println(method);
    }
}
