package i2f.spring.mvc;

import i2f.core.j2ee.web.ServletContextUtil;
import i2f.core.j2ee.web.ServletResponseUtil;
import i2f.spring.jackson.JacksonUtil;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author ltb
 * @date 2022/3/26 15:47
 * @desc
 */
public class SpringMvcUtil {
    public static ServletRequestAttributes getRequestAttributes()
    {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    public static HttpServletRequest getRequest(ServletRequestAttributes attributes){
        return attributes.getRequest();
    }
    public static HttpServletResponse getResponse(ServletRequestAttributes attributes){
        return attributes.getResponse();
    }

    /**
     * 获取request
     */
    public static HttpServletRequest getRequest()
    {
        return getRequestAttributes().getRequest();
    }

    /**
     * 获取response
     */
    public static HttpServletResponse getResponse()
    {
        return getRequestAttributes().getResponse();
    }



    /**
     * 获取session
     */
    public static HttpSession getSession()
    {
        return ServletContextUtil.getSession(getRequest());
    }



    public static Cookie[] getCookie(){
        return ServletContextUtil.getCookie(getRequest());
    }

    public static ArrayList<String> getHeaders(String key){
        return ServletContextUtil.getHeaders(getRequest(),key);
    }

    public static ServletContext getServletContext(){
        return ServletContextUtil.getServletContext(getSession());
    }

    public static File getContextPath(String relativePath){
        return ServletContextUtil.getContextPath(getServletContext(),relativePath);
    }

    public static RequestDispatcher getDispatcher(String target){
        return ServletContextUtil.getDispatcher(getRequest(),target);
    }

    public static void forward(String target) throws ServletException, IOException {
        ServletContextUtil.forward(getRequest(),getResponse(),target);
    }

    public static void include(String target) throws ServletException, IOException {
        ServletContextUtil.forward(getRequest(),getResponse(),target);
    }

    public static void redirect(String target) throws IOException {
        ServletContextUtil.redirect(getResponse(),target);
    }

    public static void sessionSet(String key,Object val){
        ServletContextUtil.sessionSet(getSession(),key,val);
    }

    public static Object sessionGet(String key){
        return ServletContextUtil.sessionGet(getSession(),key);
    }

    public static void requestSet(String key,Object val){
        ServletContextUtil.requestSet(getRequest(),key,val);
    }

    public static Object requestGet(String key){
        return ServletContextUtil.requestGet(getRequest(),key);
    }

    public static void respJson(HttpServletResponse response, String string){
        respJson(response, string,200);
    }
    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param string 待渲染的字符串
     * @return null
     */
    public static void respJson(HttpServletResponse response, String string,int status)
    {
        try
        {
            ServletResponseUtil.respJsonString(response,string,status);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void respJson(String json){
        respJson(json,200);
    }
    public static void respJson(String json,int status){
        respJson(getResponse(),json,status);
    }

    public static void respJsonObj(Object obj) throws Exception {
        respJson(JacksonUtil.toJson(obj),200);
    }

    /**
     * 获取完整的请求路径，包括：域名，端口，上下文访问路径
     *
     * 举例：http://127.0.0.1:8080
     * 如果是部署在Tomcat中，指定了上下文：test
     * 那么：
     * http://127.0.0.1:8080/test
     * @return 服务地址
     */
    public static String getContextBaseUrl()
    {
        return ServletContextUtil.getContextBaseUrl(getRequest());
    }

}
