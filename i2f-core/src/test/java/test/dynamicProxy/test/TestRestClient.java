package test.dynamicProxy.test;

import i2f.core.json.Json2Processor;
import i2f.core.net.http.rest.RestClientProvider;

/**
 * @author ltb
 * @date 2022/5/18 10:45
 * @desc
 */
public class TestRestClient {
    public static void main(String[] args){
        ISampleRestClient client=new RestClientProvider().getClient(ISampleRestClient.class, new Json2Processor());

        String ret=client.search("xxx","hello");

        System.out.println(ret);
    }
}
