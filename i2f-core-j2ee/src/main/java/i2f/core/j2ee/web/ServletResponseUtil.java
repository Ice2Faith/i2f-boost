package i2f.core.j2ee.web;

import i2f.core.json.IJsonProcessor;
import i2f.core.json.Json2Processor;
import i2f.core.xml.IXmlProcessor;
import i2f.core.xml.Xml2Processor;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * @author ltb
 * @date 2022/3/26 17:32
 * @desc
 */
public class ServletResponseUtil {
    public static void respJson(HttpServletResponse response,Object obj) throws Exception {
        IJsonProcessor processor=new Json2Processor();
        respJson(response, obj,processor);
    }

    public static void respJson(HttpServletResponse response, Object obj, IJsonProcessor processor) throws Exception{
        String json= processor.toText(obj);
        respJsonString(response,json);
    }
    public static void respJsonString(HttpServletResponse response, String content) throws Exception{
        respJsonString(response, content,200);
    }
    public static void respJsonString(HttpServletResponse response,String content,int status) throws Exception{
        respString(response,content,status,"application/json","UTF-8");
    }

    public static void respXml(HttpServletResponse response,Object obj) throws Exception {
        IXmlProcessor processor=new Xml2Processor();
        respXml(response, obj,processor);
    }

    public static void respXml(HttpServletResponse response, Object obj, IXmlProcessor processor) throws Exception{
        String json= processor.toText(obj);
        respXmlString(response,json);
    }
    public static void respXmlString(HttpServletResponse response,String content) throws Exception{
        respXmlString(response, content,200);
    }
    public static void respXmlString(HttpServletResponse response,String content,int status) throws Exception{
        respString(response,content,status,"text/xml","UTF-8");
    }

    public static void respString(HttpServletResponse response,String content,int status,String contentType,String charset) throws Exception{
        response.setStatus(status);
        response.setContentType(contentType);
        response.setCharacterEncoding(charset);
        response.getWriter().print(content);
    }

    public static void supportCors(HttpServletResponse resp){
        Collection<String> headers=resp.getHeaders("Access-Control-Allow-Origin");
        if(headers.size()<=0) {
            resp.setHeader("Access-Control-Allow-Origin", "*");
        }
        resp.setHeader("Access-Control-Allow-Methods", "*");
        resp.setHeader("Access-Control-Max-Age", "3600");
        resp.setHeader("Access-Control-Allow-Headers",
                "X-Requested-With, Content-Type, Authorization, Accept, Origin, User-Agent, Content-Range, Content-Disposition, Content-Description");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
    }
}
