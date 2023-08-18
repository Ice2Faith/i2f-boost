package test.http;

import i2f.core.lang.functional.thr.supplier.IThrSupplier1;
import i2f.core.lang.lambda.impl.LambdaFunctions;
import i2f.core.network.net.http.HttpUtil;
import i2f.core.network.net.http.data.HttpRequest;
import i2f.core.network.net.http.data.HttpResponse;

import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2023/6/26 11:32
 * @desc
 */
public class TestHttp {
    public static void main(String[] args) throws IOException {

        String mname = LambdaFunctions.getFunctionSerializedLambda((IThrSupplier1<HttpResponse, HttpRequest>) HttpUtil.http()::doHttp).getImplMethodName();
        System.out.println(mname);
    }
}
