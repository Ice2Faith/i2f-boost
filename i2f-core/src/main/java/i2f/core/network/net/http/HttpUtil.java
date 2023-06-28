package i2f.core.network.net.http;

import i2f.core.annotations.remark.Author;
import i2f.core.network.net.http.data.HttpRequest;
import i2f.core.network.net.http.impl.BasicHttpProcessorProvider;
import i2f.core.network.net.http.impl.HttpUrlConnectProcessor;
import i2f.core.serialize.str.json.IJsonSerializer;
import i2f.core.serialize.str.json.impl.Json2Serializer;

import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/3/24 8:30
 * @desc
 */
@Author("i2f")
public class HttpUtil {
    public static volatile BasicHttpProcessorProvider httpProvider = new BasicHttpProcessorProvider(new HttpUrlConnectProcessor());
    public static volatile IJsonSerializer jsonProcessor = new Json2Serializer();

    public static BasicHttpProcessorProvider http(){
        return httpProvider;
    }


    public static void setHttpHeaders(HttpURLConnection conn, HttpRequest request) {
        Map<String, Object> header = request.getHeader();
        if (header == null || header.size() == 0) {
            return;
        }
        for (Map.Entry<String, Object> item : header.entrySet()) {
            String name = item.getKey();
            Object value = item.getValue();
            if (value == null) {
                value = "";
            }
            String str = String.valueOf(value);
            conn.setRequestProperty(name, str);
        }
    }

    public static String generateUrl(HttpRequest request) {
        Map<String, Object> params = request.getParams();
        String url = request.getUrl();
        if(!url.startsWith("http://") && !url.startsWith("https://")){
            url="http://"+url;
        }
        if (params == null || params.size() == 0) {
            return url;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(url);
        if(url.indexOf("?")<0) {
            builder.append("?");
        }
        if((url.contains("&") || url.contains("=")) && !url.endsWith("&") && !url.endsWith("?")){
            builder.append("&");
        }
        generateUrlEncodeString(params, builder);

        return builder.toString();
    }

    public static StringBuilder generateUrlEncodeString(Map<String, Object> params, StringBuilder builder) {
        boolean isFirst = true;
        for (Map.Entry<String, Object> item : params.entrySet()) {
            String name = item.getKey();
            Object value = item.getValue();
            if (value == null) {
                value = "";
            }
            String str = URLEncoder.encode(String.valueOf(value));
            if (!isFirst) {
                builder.append("&");
            }
            builder.append(name);
            builder.append("=");
            builder.append(str);
            isFirst = false;
        }
        return builder;
    }
}
