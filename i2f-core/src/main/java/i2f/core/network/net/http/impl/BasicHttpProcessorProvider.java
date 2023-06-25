package i2f.core.network.net.http.impl;

import i2f.core.network.net.http.data.HttpRequest;
import i2f.core.network.net.http.data.HttpResponse;
import i2f.core.network.net.http.interfaces.HttpProcessorProvider;
import i2f.core.network.net.http.interfaces.IHttpProcessor;
import i2f.core.network.net.http.interfaces.IHttpRequestBodyHandler;
import i2f.core.serialize.json.IJsonProcessor;

import java.io.IOException;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/3/26 20:12
 * @desc
 */
public class BasicHttpProcessorProvider implements HttpProcessorProvider {
    protected IHttpProcessor httpProcessor;
    protected IHttpRequestBodyHandler formRequestBodyHandler=new HttpFormUrlEncodedRequestBodyHandler();
    protected IHttpRequestBodyHandler jsonRequestBodyHandler=new HttpJsonRequestBodyHandler();
    protected IHttpRequestBodyHandler multipartFormDataRequestBodyHandler=new HttpMultipartFormDataRequestBodyHandler();

    public BasicHttpProcessorProvider(IHttpProcessor httpProcessor){
        this.httpProcessor = httpProcessor;
    }

    @Override
    public <T> T postFormForObject(HttpRequest request, String charset, Class<T> clazz, IJsonProcessor processor) throws IOException {
        return request.setMethod(HttpRequest.POST)
                .addHeader(HttpRequest.CONTENT_TYPE,HttpRequest.CONTENT_FORM_URLENCODE)
                .setRequestBodyHandler(formRequestBodyHandler)
                .send(httpProcessor)
                .getContentAsObject(processor,clazz,charset);
    }

    @Override
    public String postFormForString(HttpRequest request, String charset) throws IOException {
        return request.setMethod(HttpRequest.POST)
                .addHeader(HttpRequest.CONTENT_TYPE,HttpRequest.CONTENT_FORM_URLENCODE)
                .setRequestBodyHandler(formRequestBodyHandler)
                .send(httpProcessor)
                .getContentAsString(charset);
    }

    @Override
    public HttpResponse postForm(HttpRequest request) throws IOException {
        return request.setMethod(HttpRequest.POST)
                .addHeader(HttpRequest.CONTENT_TYPE,HttpRequest.CONTENT_FORM_URLENCODE)
                .setRequestBodyHandler(formRequestBodyHandler)
                .send(httpProcessor);
    }

    @Override
    public <T> T postJsonForObject(HttpRequest request, String charset, Class<T> clazz, IJsonProcessor jsonProcessor) throws IOException {
        return request.setMethod(HttpRequest.POST)
                .addHeader(HttpRequest.CONTENT_TYPE,HttpRequest.CONTENT_JSON)
                .setRequestBodyHandler(jsonRequestBodyHandler)
                .send(httpProcessor)
                .getContentAsObject(jsonProcessor,clazz,charset);
    }

    @Override
    public String postJsonForString(HttpRequest request, String charset) throws IOException {
        return request.setMethod(HttpRequest.POST)
                .addHeader(HttpRequest.CONTENT_TYPE,HttpRequest.CONTENT_JSON)
                .setRequestBodyHandler(jsonRequestBodyHandler)
                .send(httpProcessor)
                .getContentAsString(charset);
    }

    @Override
    public HttpResponse postJson(HttpRequest request) throws IOException {
        return request.setMethod(HttpRequest.POST)
                .addHeader(HttpRequest.CONTENT_TYPE,HttpRequest.CONTENT_JSON)
                .setRequestBodyHandler(jsonRequestBodyHandler)
                .send(httpProcessor);
    }

    @Override
    public HttpResponse postJson(String url, Map<String, Object> data) throws IOException {
        return HttpRequest.doPost()
                .setUrl(url)
                .setData(data)
                .addHeader(HttpRequest.CONTENT_TYPE,HttpRequest.CONTENT_JSON)
                .setRequestBodyHandler(jsonRequestBodyHandler)
                .send(httpProcessor);
    }

    @Override
    public HttpResponse postJson(String url, Map<String, Object> data, Map<String, Object> params) throws IOException {
        return HttpRequest.doPost()
                .setUrl(url)
                .setData(data)
                .setParams(params)
                .addHeader(HttpRequest.CONTENT_TYPE,HttpRequest.CONTENT_JSON)
                .setRequestBodyHandler(jsonRequestBodyHandler)
                .send(httpProcessor);
    }

    @Override
    public HttpResponse postJson(String url, Map<String, Object> data, Map<String, Object> params, Map<String, Object> header) throws IOException {
        return HttpRequest.doPost()
                .setUrl(url)
                .setData(data)
                .setParams(params)
                .setHeader(header)
                .addHeader(HttpRequest.CONTENT_TYPE,HttpRequest.CONTENT_JSON)
                .setRequestBodyHandler(jsonRequestBodyHandler)
                .send(httpProcessor);
    }

    @Override
    public String postJsonForString(String url, Map<String, Object> data, String charset) throws IOException {
        return HttpRequest.doPost()
                .setUrl(url)
                .setData(data)
                .addHeader(HttpRequest.CONTENT_TYPE,HttpRequest.CONTENT_JSON)
                .setRequestBodyHandler(jsonRequestBodyHandler)
                .send(httpProcessor)
                .getContentAsString(charset);
    }

    @Override
    public String postJsonForString(String url, Map<String, Object> data, Map<String, Object> params, String charset) throws IOException {
        return HttpRequest.doPost()
                .setUrl(url)
                .setData(data)
                .setParams(params)
                .addHeader(HttpRequest.CONTENT_TYPE,HttpRequest.CONTENT_JSON)
                .setRequestBodyHandler(jsonRequestBodyHandler)
                .send(httpProcessor)
                .getContentAsString(charset);
    }

    @Override
    public String postJsonForString(String url, Map<String, Object> data, Map<String, Object> params, Map<String, Object> header, String charset) throws IOException {
        return HttpRequest.doPost()
                .setUrl(url)
                .setData(data)
                .setParams(params)
                .setHeader(header)
                .addHeader(HttpRequest.CONTENT_TYPE,HttpRequest.CONTENT_JSON)
                .setRequestBodyHandler(jsonRequestBodyHandler)
                .send(httpProcessor)
                .getContentAsString(charset);
    }

    @Override
    public <T> T postJsonForObject(String url, Map<String, Object> data, String charset, Class<T> clazz, IJsonProcessor processor) throws IOException {
        return HttpRequest.doPost()
                .setUrl(url)
                .setData(data)
                .addHeader(HttpRequest.CONTENT_TYPE,HttpRequest.CONTENT_JSON)
                .setRequestBodyHandler(jsonRequestBodyHandler)
                .send(httpProcessor)
                .getContentAsObject(processor,clazz,charset);
    }

    @Override
    public <T> T postJsonForObject(String url, Map<String, Object> data, Map<String, Object> params, String charset, Class<T> clazz, IJsonProcessor processor) throws IOException {
        return HttpRequest.doPost()
                .setUrl(url)
                .setData(data)
                .setParams(params)
                .addHeader(HttpRequest.CONTENT_TYPE,HttpRequest.CONTENT_JSON)
                .setRequestBodyHandler(jsonRequestBodyHandler)
                .send(httpProcessor)
                .getContentAsObject(processor,clazz,charset);
    }

    @Override
    public <T> T postJsonForObject(String url, Map<String, Object> data, Map<String, Object> params, Map<String, Object> header, String charset, Class<T> clazz, IJsonProcessor processor) throws IOException {
        return HttpRequest.doPost()
                .setUrl(url)
                .setData(data)
                .setParams(params)
                .setHeader(header)
                .addHeader(HttpRequest.CONTENT_TYPE,HttpRequest.CONTENT_JSON)
                .setRequestBodyHandler(jsonRequestBodyHandler)
                .send(httpProcessor)
                .getContentAsObject(processor,clazz,charset);
    }

    @Override
    public HttpResponse postForm(String url, Map<String, Object> data) throws IOException {
        return HttpRequest.doPost()
                .setUrl(url)
                .setData(data)
                .addHeader(HttpRequest.CONTENT_TYPE,HttpRequest.CONTENT_FORM_URLENCODE)
                .setRequestBodyHandler(formRequestBodyHandler)
                .send(httpProcessor);
    }

    @Override
    public HttpResponse postForm(String url, Map<String, Object> data, Map<String, Object> params) throws IOException {
        return HttpRequest.doPost()
                .setUrl(url)
                .setData(data)
                .setParams(params)
                .addHeader(HttpRequest.CONTENT_TYPE,HttpRequest.CONTENT_FORM_URLENCODE)
                .setRequestBodyHandler(formRequestBodyHandler)
                .send(httpProcessor);
    }

    @Override
    public HttpResponse postForm(String url, Map<String, Object> data, Map<String, Object> params, Map<String, Object> header) throws IOException {
        return HttpRequest.doPost()
                .setUrl(url)
                .setData(data)
                .setParams(params)
                .setHeader(header)
                .addHeader(HttpRequest.CONTENT_TYPE,HttpRequest.CONTENT_FORM_URLENCODE)
                .setRequestBodyHandler(formRequestBodyHandler)
                .send(httpProcessor);
    }

    @Override
    public String postFormForString(String url, Map<String, Object> data, String charset) throws IOException {
        return HttpRequest.doPost()
                .setUrl(url)
                .setData(data)
                .addHeader(HttpRequest.CONTENT_TYPE,HttpRequest.CONTENT_FORM_URLENCODE)
                .setRequestBodyHandler(formRequestBodyHandler)
                .send(httpProcessor)
                .getContentAsString(charset);
    }

    @Override
    public String postFormForString(String url, Map<String, Object> data, Map<String, Object> params, String charset) throws IOException {
        return HttpRequest.doPost()
                .setUrl(url)
                .setData(data)
                .setParams(params)
                .addHeader(HttpRequest.CONTENT_TYPE,HttpRequest.CONTENT_FORM_URLENCODE)
                .setRequestBodyHandler(formRequestBodyHandler)
                .send(httpProcessor)
                .getContentAsString(charset);
    }

    @Override
    public String postFormForString(String url, Map<String, Object> data, Map<String, Object> params, Map<String, Object> header, String charset) throws IOException {
        return HttpRequest.doPost()
                .setUrl(url)
                .setData(data)
                .setParams(params)
                .setHeader(header)
                .addHeader(HttpRequest.CONTENT_TYPE,HttpRequest.CONTENT_FORM_URLENCODE)
                .setRequestBodyHandler(formRequestBodyHandler)
                .send(httpProcessor)
                .getContentAsString(charset);
    }

    @Override
    public <T> T postFormForObject(String url, Map<String, Object> data, String charset, Class<T> clazz, IJsonProcessor processor) throws IOException {
        return HttpRequest.doPost()
                .setUrl(url)
                .setData(data)
                .addHeader(HttpRequest.CONTENT_TYPE,HttpRequest.CONTENT_FORM_URLENCODE)
                .setRequestBodyHandler(formRequestBodyHandler)
                .send(httpProcessor)
                .getContentAsObject(processor,clazz,charset);
    }

    @Override
    public <T> T postFormForObject(String url, Map<String, Object> data, Map<String, Object> params, String charset, Class<T> clazz, IJsonProcessor processor) throws IOException {
        return HttpRequest.doPost()
                .setUrl(url)
                .setData(data)
                .setParams(params)
                .addHeader(HttpRequest.CONTENT_TYPE,HttpRequest.CONTENT_FORM_URLENCODE)
                .setRequestBodyHandler(formRequestBodyHandler)
                .send(httpProcessor)
                .getContentAsObject(processor,clazz,charset);
    }

    @Override
    public <T> T postFormForObject(String url, Map<String, Object> data, Map<String, Object> params, Map<String, Object> header, String charset, Class<T> clazz, IJsonProcessor processor) throws IOException {
        return HttpRequest.doPost()
                .setUrl(url)
                .setData(data)
                .setParams(params)
                .setHeader(header)
                .addHeader(HttpRequest.CONTENT_TYPE,HttpRequest.CONTENT_FORM_URLENCODE)
                .setRequestBodyHandler(formRequestBodyHandler)
                .send(httpProcessor)
                .getContentAsObject(processor,clazz,charset);
    }

    @Override
    public HttpResponse get(String url) throws IOException {
        return HttpRequest.doGet()
                .setUrl(url)
                .send(httpProcessor);
    }

    @Override
    public HttpResponse get(String url, Map<String, Object> params) throws IOException {
        return HttpRequest.doGet()
                .setUrl(url)
                .setParams(params)
                .send(httpProcessor);
    }

    @Override
    public HttpResponse get(String url, Map<String, Object> params, Map<String, Object> header) throws IOException {
        return HttpRequest.doGet()
                .setUrl(url)
                .setParams(params)
                .setHeader(header)
                .send(httpProcessor);
    }

    @Override
    public String getForString(String url, String charset) throws IOException {
        return HttpRequest.doGet()
                .setUrl(url)
                .send(httpProcessor)
                .getContentAsString(charset);
    }

    @Override
    public String getForString(String url, Map<String, Object> params, String charset) throws IOException {
        return HttpRequest.doGet()
                .setUrl(url)
                .setParams(params)
                .send(httpProcessor)
                .getContentAsString(charset);
    }

    @Override
    public String getForString(String url, Map<String, Object> params, Map<String, Object> header, String charset) throws IOException {
        return HttpRequest.doGet()
                .setUrl(url)
                .setParams(params)
                .setHeader(header)
                .send(httpProcessor)
                .getContentAsString(charset);
    }

    @Override
    public <T> T getForObject(String url, String charset, Class<T> clazz, IJsonProcessor processor) throws IOException {
        return HttpRequest.doGet()
                .setUrl(url)
                .send(httpProcessor)
                .getContentAsObject(processor,clazz,charset);
    }

    @Override
    public <T> T getForObject(String url, Map<String, Object> params, String charset, Class<T> clazz, IJsonProcessor processor) throws IOException {
        return HttpRequest.doGet()
                .setUrl(url)
                .setParams(params)
                .send(httpProcessor)
                .getContentAsObject(processor,clazz,charset);
    }

    @Override
    public <T> T getForObject(String url, Map<String, Object> params, Map<String, Object> header, String charset, Class<T> clazz, IJsonProcessor processor) throws IOException {
        return HttpRequest.doGet()
                .setUrl(url)
                .setParams(params)
                .setHeader(header)
                .send(httpProcessor)
                .getContentAsObject(processor,clazz,charset);
    }

    @Override
    public HttpResponse doHttp(HttpRequest request) throws IOException {
        return httpProcessor.doHttp(request);
    }
}
