package test;

import i2f.core.lang.proxy.impl.JdkProxyProvider;
import i2f.core.network.net.http.HttpUtil;
import i2f.core.network.net.http.impl.BasicHttpProcessorProvider;
import i2f.core.network.net.http.impl.HttpUrlConnectProcessor;
import i2f.core.network.net.http.interfaces.HttpProcessorProvider;
import i2f.core.zplugin.log.LoggerFactory;
import i2f.core.zplugin.log.context.LogLevelMappingHolder;
import i2f.core.zplugin.log.context.LogWriterHolder;
import i2f.core.zplugin.log.enums.LogLevel;

import java.io.IOException;

/**
 * @author ltb
 * @date 2022/3/26 11:04
 * @desc
 */
public class TestHttpUtil {
    public static void main(String[] args) throws IOException {
//        LogLevelMappingHolder.addMapping("test.*", LogLevel.WARN);
//        LogLevelMappingHolder.addMapping("i2f.core.*",LogLevel.ERROR);
//        LogLevelMappingHolder.addMapping("i2f.core.*.doHttp",LogLevel.DEBUG);
        LogLevelMappingHolder.addMappingsByClasspathProperties("logging.prop");
        LogWriterHolder.registerLogWritersByClasspathProperties("logging.prop");
//        HttpRequest request=new HttpRequest()
//                .setConnectTimeout(120000)
//                .setReadTimeout(120000)
//                .setUrl("http://localhost:9202/agri/proxy/EcController/qryEcDistribution")
//                .setMethod(HttpRequest.POST)
//                .addData("content","477")
//                .addHeader("Accept","*/*");
//        String resp= HttpUtil.http().postJsonForString(request,"UTF-8");
//        System.out.println(resp);

        try{
            thrable();
        }catch(Exception ep){
//            System.out.println(e.getMessage());
        }
    }

    public static void thrable() throws IOException {
        LogLevelMappingHolder.getMapping().clear();
        LogLevelMappingHolder.addMapping("*", LogLevel.ALL);
        String baidu= HttpUtil
                .http()
                .getForString("www.baidu.com", "UTF-8");
        HttpProcessorProvider provider= new JdkProxyProvider().proxy(new BasicHttpProcessorProvider(new HttpUrlConnectProcessor()), LoggerFactory.getHandler());
        baidu=provider.getForString("www.baidu.com", "UTF-8");
        System.out.println(baidu);
    }
}
