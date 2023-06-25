package i2f.core.network.net.http.interfaces;

import i2f.core.network.net.http.data.HttpRequest;
import i2f.core.network.net.http.data.HttpResponse;
import i2f.core.serialize.json.IJsonProcessor;
import i2f.core.zplugin.log.annotations.Log;

import java.io.IOException;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/3/26 20:09
 * @desc
 */
@Log
public interface HttpProcessorProvider extends IHttpProcessor {
    <T> T postFormForObject(HttpRequest request,String charset,Class<T> clazz,IJsonProcessor processor) throws IOException;
    String postFormForString(HttpRequest request, String charset) throws IOException;
    HttpResponse postForm(HttpRequest request) throws IOException;

    <T> T postJsonForObject(HttpRequest request, String charset, Class<T> clazz, IJsonProcessor processor) throws IOException;
    String postJsonForString(HttpRequest request, String charset) throws IOException;
    HttpResponse postJson(HttpRequest request) throws IOException;

    HttpResponse postJson(String url, Map<String,Object> data) throws IOException;
    HttpResponse postJson(String url,Map<String,Object> data,Map<String,Object> params) throws IOException;
    HttpResponse postJson(String url,Map<String,Object> data,Map<String,Object> params,Map<String,Object> header) throws IOException;

    String postJsonForString(String url,Map<String,Object> data,String charset) throws IOException;
    String postJsonForString(String url,Map<String,Object> data,Map<String,Object> params,String charset) throws IOException;
    String postJsonForString(String url,Map<String,Object> data,Map<String,Object> params,Map<String,Object> header,String charset) throws IOException;

    <T> T postJsonForObject(String url,Map<String,Object> data,String charset,Class<T> clazz,IJsonProcessor processor) throws IOException;
    <T> T postJsonForObject(String url,Map<String,Object> data,Map<String,Object> params,String charset,Class<T> clazz,IJsonProcessor processor) throws IOException;
    <T> T postJsonForObject(String url,Map<String,Object> data,Map<String,Object> params,Map<String,Object> header,String charset,Class<T> clazz,IJsonProcessor processor) throws IOException;

    HttpResponse postForm(String url, Map<String,Object> data) throws IOException;
    HttpResponse postForm(String url,Map<String,Object> data,Map<String,Object> params) throws IOException;
    HttpResponse postForm(String url,Map<String,Object> data,Map<String,Object> params,Map<String,Object> header) throws IOException;

    String postFormForString(String url,Map<String,Object> data,String charset) throws IOException;
    String postFormForString(String url,Map<String,Object> data,Map<String,Object> params,String charset) throws IOException;
    String postFormForString(String url,Map<String,Object> data,Map<String,Object> params,Map<String,Object> header,String charset) throws IOException;

    <T> T postFormForObject(String url,Map<String,Object> data,String charset,Class<T> clazz,IJsonProcessor processor) throws IOException;
    <T> T postFormForObject(String url,Map<String,Object> data,Map<String,Object> params,String charset,Class<T> clazz,IJsonProcessor processor) throws IOException;
    <T> T postFormForObject(String url,Map<String,Object> data,Map<String,Object> params,Map<String,Object> header,String charset,Class<T> clazz,IJsonProcessor processor) throws IOException;

    HttpResponse get(String url) throws IOException;
    HttpResponse get(String url,Map<String,Object> params) throws IOException;
    HttpResponse get(String url,Map<String,Object> params,Map<String,Object> header) throws IOException;

    String getForString(String url,String charset) throws IOException;
    String getForString(String url,Map<String,Object> params,String charset) throws IOException;
    String getForString(String url,Map<String,Object> params,Map<String,Object> header,String charset) throws IOException;

    <T> T getForObject(String url,String charset,Class<T> clazz,IJsonProcessor processor) throws IOException;
    <T> T getForObject(String url,Map<String,Object> params,String charset,Class<T> clazz,IJsonProcessor processor) throws IOException;
    <T> T getForObject(String url,Map<String,Object> params,Map<String,Object> header,String charset,Class<T> clazz,IJsonProcessor processor) throws IOException;
}
