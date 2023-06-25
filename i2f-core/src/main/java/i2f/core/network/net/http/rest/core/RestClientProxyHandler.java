package i2f.core.network.net.http.rest.core;

import i2f.core.annotations.notice.Name;
import i2f.core.exception.BoostException;
import i2f.core.generate.core.ObjectFinder;
import i2f.core.lang.proxy.impl.BasicDynamicProxyHandler;
import i2f.core.network.net.http.data.HttpRequest;
import i2f.core.network.net.http.data.HttpResponse;
import i2f.core.network.net.http.data.MultipartFile;
import i2f.core.network.net.http.interfaces.IHttpProcessor;
import i2f.core.network.net.http.rest.annotations.*;
import i2f.core.reflection.reflect.convert.ConvertResolver;
import i2f.core.reflection.reflect.core.ReflectResolver;
import i2f.core.serialize.std.IStringSerializer;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/5/18 9:52
 * @desc
 */
public class RestClientProxyHandler extends BasicDynamicProxyHandler {
    protected IStringSerializer processor;

    public RestClientProxyHandler(IStringSerializer processor) {
        this.processor = processor;
    }

    @Override
    public Object resolve(Object context, Object ivkObj, Method method, Object... args) {
        HttpRequest request = new HttpRequest();
        Class clazz = method.getDeclaringClass();
        Parameter[] parameters = method.getParameters();
        RestClient client = ReflectResolver.findAnnotation(clazz, RestClient.class, false);
        if (client == null) {
            throw new BoostException("interface not an RestClient interface");
        }

        IHttpProcessor http=ReflectResolver.instance(client.http());

        String url= joinUrlPath(client.url(),client.path());

        String path="";
        String urlMethod=HttpRequest.GET;
        RestMapping mapping=ReflectResolver.findAnnotation(method,RestMapping.class,false);
        if(mapping!=null){
            path=mapping.value();
            urlMethod=mapping.method();
        }
        RestGetMapping getMapping=ReflectResolver.findAnnotation(method,RestGetMapping.class,false);
        if(getMapping!=null){
            path=getMapping.value();
            urlMethod=HttpRequest.GET;
        }
        RestPostMapping postMapping=ReflectResolver.findAnnotation(method,RestPostMapping.class,false);
        if(postMapping!=null){
            path=postMapping.value();
            urlMethod=HttpRequest.POST;
        }
        RestPutMapping putMapping=ReflectResolver.findAnnotation(method,RestPutMapping.class,false);
        if(putMapping!=null){
            path=putMapping.value();
            urlMethod=HttpRequest.PUT;
        }
        RestDeleteMapping deleteMapping=ReflectResolver.findAnnotation(method,RestDeleteMapping.class,false);
        if(deleteMapping!=null){
            path=deleteMapping.value();
            urlMethod=HttpRequest.DELETE;
        }

        url=joinUrlPath(url,path);

        if("".equals(urlMethod)){
            urlMethod=HttpRequest.GET;
        }
        request.setUrl(url);
        request.setMethod(urlMethod);

        Map<String,Object> headers=new HashMap<>();

        Map<String,Object> params=new HashMap<>();

        int datasCount=0;
        Map<String,Object> datas=new HashMap<>();

        RestHeaders annHeaders=ReflectResolver.findAnnotation(method,RestHeaders.class,false);
        if(annHeaders!=null){
            for(RestHeader item : annHeaders.value()){
                String name=item.name();
                if("".equals(name)){
                    continue;
                }
                if("".equals(item.param())){
                    String val=item.value();
                    headers.put(name,val);
                }else{
                    Object paramObj=null;
                    String param=item.param();
                    if(param.matches("^\\d+$")){
                        int paramIdx=Integer.parseInt(param);
                        if(paramIdx<args.length){
                            paramObj=args[paramIdx];
                        }
                    }else{
                        String paramName=param;
                        for(int i=0;i<parameters.length;i+=1){
                            Parameter pm=parameters[i];
                            if(pm.getName().equals(paramName)){
                                paramObj=args[i];
                                break;
                            }
                            Name aname=ReflectResolver.findAnnotation(pm,Name.class,false);
                            if(aname!=null){
                                if(aname.value().equals(paramName)){
                                    paramObj=args[i];
                                    break;
                                }
                            }
                        }
                    }
                    if("".equals(item.attr())){
                        headers.put(name,paramObj);
                    }else{
                        Object val= ObjectFinder.getObjectByDotKeyWithReference(paramObj,item.attr());
                        headers.put(name,val);
                    }
                }
            }
        }

        for(int i=0;i<parameters.length;i++){
            Parameter item = parameters[i];
            String name= item.getName();
            Object val=args[i];
            if(val instanceof MultipartFile){
                request.addFile((MultipartFile)val);
                continue;
            }
            if(val instanceof File){
                try{
                    request.addFile((File)val);
                }catch(Exception e){
                    throw new BoostException(e.getMessage(),e);
                }
                continue;
            }
            RestHeader annHeader=ReflectResolver.findAnnotation(item,RestHeader.class,false);
            if(annHeader!=null){
                if(!"".equals(annHeader.name())) {
                    name = annHeader.name();
                }
                headers.put(name,val);
                continue;
            }
            RestBody annBody=ReflectResolver.findAnnotation(item,RestBody.class,false);
            if(annBody!=null){
                if(!"".equals(annBody.value())) {
                    name = annBody.value();
                }
                datas.put(name,val);
                datasCount++;
                continue;
            }
            RestParam annParam=ReflectResolver.findAnnotation(item,RestParam.class,false);
            if(annParam!=null){
                if(!"".equals(annParam.value())){
                    name=annParam.value();
                }
            }
            params.put(name,val);
        }


        request.setHeader(headers);

        request.setParams(params);

        request.setData(datas);

        Class<?> retType=method.getReturnType();
        Object ret=null;
        try{

            HttpResponse response = http.doHttp(request);

            String content = response.getContentAsString("UTF-8");

            if(retType.equals(String.class)){
                ret=content;
            }else if(ConvertResolver.isValueConvertible(content,retType)){
                ret=ConvertResolver.tryConvertible(content,retType);
            }else{
                ret = processor.deserialize(content, retType);
            }
        }catch(Exception e){

            throw new BoostException(e.getMessage(),e);
        }

        return ret;
    }

    public static String joinUrlPath(String basePath,String subPath){
        if(!"".equals(subPath)){
            if(basePath.endsWith("/")){
                if(subPath.startsWith("/")){
                    basePath=basePath+subPath.substring(1);
                }else{
                    basePath=basePath+subPath;
                }
            }else{
                if(subPath.startsWith("/")){
                    basePath=basePath+subPath;
                }else{
                    basePath=basePath+"/"+subPath;
                }
            }
        }

        return basePath;
    }
}
