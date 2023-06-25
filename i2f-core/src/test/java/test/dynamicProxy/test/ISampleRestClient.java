package test.dynamicProxy.test;

import i2f.core.network.net.http.rest.annotations.RestClient;
import i2f.core.network.net.http.rest.annotations.RestGetMapping;
import i2f.core.network.net.http.rest.annotations.RestHeader;
import i2f.core.network.net.http.rest.annotations.RestParam;

/**
 * @author ltb
 * @date 2022/5/18 9:36
 * @desc
 */
@RestClient(url="http://www.baidu.com")
public interface ISampleRestClient {

    @RestGetMapping("/s")
    String search(@RestHeader(name="token") String token, @RestParam("wd")String wd);

}
