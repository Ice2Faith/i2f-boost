package i2f.generator.api;

import i2f.core.annotations.remark.Remark;
import i2f.core.db.data.TableMeta;
import i2f.core.reflect.core.ReflectResolver;
import i2f.core.str.Appender;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author ltb
 * @date 2022/6/16 13:57
 * @desc
 */
@Data
public class ApiContext {
    private String url;
    private String method;
    private String comment;
    private String contentType;
    private List<ApiJoinLine> lines;

    public static ApiContext parse(TableMeta meta){
        ApiContext ret=new ApiContext();
        ret.lines=ApiJoinLine.parse(meta,null);
        return ret;
    }

    public static List<ApiContext> parse(List<TableMeta> metas){
        List<ApiContext> ret=new ArrayList<>();
        for(TableMeta item : metas){
            ApiContext ctx=parse(item);
            ret.add(ctx);
        }
        return ret;
    }

    public static ApiContext parse(Class voClass){
        ApiContext ret=new ApiContext();
        ret.lines=ApiJoinLine.parseVo(voClass,null);
        return ret;
    }

    public static ApiContext parse(Method method){
        ApiContext ret=new ApiContext();
        ret.lines=ApiJoinLine.parseMethod(method);
        return ret;
    }


    public static String keepSep(String sep,String str1,String str2){
        if(str1.endsWith(sep)){
            if(str2.startsWith(sep)){
                return str1+ str2.substring(sep.length());
            }else{
                return str1+str2;
            }
        }else{
            if(str2.startsWith(sep)){
                return str1+ str2;
            }else{
                return str1+sep+str2;
            }
        }
    }
    public static String urlMerge(String baseUrl,String ... subs){
        List<String> urls=new ArrayList<>();
        for(String item : subs){
            urls.add(keepSep("/",baseUrl,item));
        }
        return Appender.builder().addCollection(urls," | ").get();
    }
    public static List<ApiContext> parseMvc(Class controllerClass){
        Set<Method> methods=ReflectResolver.getMethodsWithAnnotations(controllerClass,false,RequestMapping.class,GetMapping.class,PostMapping.class,PutMapping.class,DeleteMapping.class);
        List<ApiContext> ret=new ArrayList<>();
        for(Method item : methods){
            ret.add(parseMvc(item));
        }
        return ret;
    }
    public static ApiContext parseMvc(Method method){
        ApiContext ret=new ApiContext();
        Class clazz=method.getDeclaringClass();
        RequestMapping baseMapping= ReflectResolver.findAnnotation(clazz,RequestMapping.class,false);
        String baseUrl="";
        if(baseMapping!=null){
            baseUrl=baseMapping.value()[0];
        }

        RequestMapping elemMapping=ReflectResolver.findAnnotation(method,RequestMapping.class,false);
        if(elemMapping!=null){
            ret.url=urlMerge(baseUrl,elemMapping.value());
            ret.method=elemMapping.method().length>0?elemMapping.method()[0]+"":"ALL";
            ret.contentType=elemMapping.consumes().length>0?elemMapping.consumes()[0]:"";
        }

        GetMapping getMapping=ReflectResolver.findAnnotation(method,GetMapping.class,false);
        if(getMapping!=null){
            ret.url=urlMerge(baseUrl,getMapping.value());
            ret.method=RequestMethod.GET+"";
            ret.contentType=getMapping.consumes().length>0?getMapping.consumes()[0]:"";
        }

        PostMapping postMapping=ReflectResolver.findAnnotation(method,PostMapping.class,false);
        if(postMapping!=null){
            ret.url=urlMerge(baseUrl,postMapping.value());
            ret.method=RequestMethod.POST+"";
            ret.contentType=postMapping.consumes().length>0?postMapping.consumes()[0]:"";
        }
        PutMapping putMapping=ReflectResolver.findAnnotation(method,PutMapping.class,false);
        if(putMapping!=null){
            ret.url=urlMerge(baseUrl,putMapping.value());
            ret.method=RequestMethod.PUT+"";
            ret.contentType=putMapping.consumes().length>0?putMapping.consumes()[0]:"";
        }
        DeleteMapping deleteMapping=ReflectResolver.findAnnotation(method,DeleteMapping.class,false);
        if(deleteMapping!=null){
            ret.url=urlMerge(baseUrl,deleteMapping.value());
            ret.method=RequestMethod.DELETE+"";
            ret.contentType=deleteMapping.consumes().length>0?deleteMapping.consumes()[0]:"";
        }

        Remark ann= ReflectResolver.findAnnotation(method,Remark.class,false);
        if(ann!=null){
            String comment= Appender.sepStr(" \n",ann.value());
            ret.comment=comment;
        }

        if(swaggerSupport()){
            Api apiann=ReflectResolver.findAnnotation(clazz,Api.class,false);
            ApiOperation opeann=ReflectResolver.findAnnotation(method,ApiOperation.class,false);
            String comment=Appender.builder()
                    .addWhen(apiann!=null,apiann.value())
                    .keepEnd(" ")
                    .addWhen(opeann!=null,opeann.value())
                    .keepEnd(" ")
                    .addsWhen(apiann!=null,apiann.tags())
                    .keepEnd(" ")
                    .addsWhen(opeann!=null,opeann.tags())
                    .get();
            ret.comment=comment;
        }

        ret.lines=ApiJoinLine.parseMethod(method,true);
        return ret;
    }

    public static boolean swaggerSupport(){
        boolean swaggerFind=false;
        try{
            String swaggerAnnName="io.swagger.annotations.ApiModelProperty";
            Class<?> aClass = Class.forName(swaggerAnnName);
            if(aClass!=null){
                swaggerFind=true;
            }
        }catch(Exception e){

        }
        return swaggerFind;
    }
}
