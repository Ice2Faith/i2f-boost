package i2f.core.net.http.rest;

import i2f.core.net.http.rest.core.RestClientProxyHandler;
import i2f.core.proxy.JdkProxyUtil;
import i2f.core.text.IFormatTextProcessor;

/**
 * @author ltb
 * @date 2022/5/18 9:51
 * @desc
 */
public class RestClientProvider {
    public<T> T getClient(Class<T> interfaces, IFormatTextProcessor processor){
        return JdkProxyUtil.proxy(interfaces,new RestClientProxyHandler(processor));
    }
}
