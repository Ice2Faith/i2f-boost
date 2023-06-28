package i2f.core.network.net.http.rest;

import i2f.core.lang.proxy.JdkProxyUtil;
import i2f.core.network.net.http.rest.core.RestClientProxyHandler;
import i2f.core.serialize.str.IStringObjectSerializer;

/**
 * @author ltb
 * @date 2022/5/18 9:51
 * @desc
 */
public class RestClientProvider {
    public <T> T getClient(Class<T> interfaces, IStringObjectSerializer processor) {
        return JdkProxyUtil.proxy(interfaces, new RestClientProxyHandler(processor));
    }
}
