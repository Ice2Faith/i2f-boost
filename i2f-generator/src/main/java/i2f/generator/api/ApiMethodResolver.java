package i2f.generator.api;

import i2f.core.reflect.core.ReflectResolver;
import io.swagger.annotations.*;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * @author ltb
 * @date 2022/7/6 8:51
 * @desc
 */
public class ApiMethodResolver {

    protected Method method;
    protected ApiMethod api;

    public static ApiMethod parseMethod(Method method){
        return new ApiMethodResolver(method).parse();
    }

    public ApiMethodResolver(Method method){
        init(method);
    }

    protected void init(Method method){
        this.method=method;
    }

    public ApiMethod parse(){
        api=new ApiMethod();
        parseBasicMethod();
        parseMvcMethod();
        parseSwaggerMethod();
        parseReturn();
        parseArguments();

        return api;
    }

    protected void parseBasicMethod(){
        Class clazz=method.getDeclaringClass();
        String methodName=clazz.getName()+"."+method.getName();
        api.setJavaMethod(methodName);
        api.setJavaGenericMethod(method.toGenericString());
    }

    protected void parseMvcMethod(){
        if(!springMvcSupport()){
            return;
        }
        Class clazz=method.getDeclaringClass();
        RequestMapping classRequestMapping= ReflectResolver.findAnnotation(clazz,RequestMapping.class,false);
        ResponseBody classResponseBody= ReflectResolver.findAnnotation(clazz,ResponseBody.class,false);
        RestController classRestController= ReflectResolver.findAnnotation(clazz,RestController.class,false);

        RequestMapping requestMapping= ReflectResolver.findAnnotation(method,RequestMapping.class,false);
        PostMapping postMapping= ReflectResolver.findAnnotation(method,PostMapping.class,false);
        GetMapping getMapping= ReflectResolver.findAnnotation(method,GetMapping.class,false);
        PutMapping putMapping= ReflectResolver.findAnnotation(method,PutMapping.class,false);
        DeleteMapping deleteMapping= ReflectResolver.findAnnotation(method,DeleteMapping.class,false);
        PatchMapping patchMapping= ReflectResolver.findAnnotation(method,PatchMapping.class,false);
        ResponseBody responseBody= ReflectResolver.findAnnotation(method,ResponseBody.class,false);

        String[] classUrl=new String[0];
        String[] classConsumers=new String[0];
        String[] classProducts=new String[0];
        RequestMethod[] classMethods=new RequestMethod[0];
        if(classRequestMapping!=null){
            if(classRequestMapping.path().length>0){
                classUrl=classRequestMapping.path();
            }else{
                classUrl=classRequestMapping.value();
            }
            classConsumers=classRequestMapping.consumes();
            classProducts=classRequestMapping.produces();
            classMethods=classRequestMapping.method();
        }

        List<String> methods=new ArrayList<>();
        List<String> urls=new ArrayList<>();
        List<String> consumers=new ArrayList<>();
        List<String> products=new ArrayList<>();
        if(requestMapping!=null){
            ////////////////////////////////////////////
            RequestMethod[] annMethods = requestMapping.method();
            if(annMethods.length==0 && classMethods.length==0){
                methods.add("ALL");
            }else if(annMethods.length>0){
                for(RequestMethod item : annMethods){
                    methods.add(item.name());
                }
            }else if(classMethods.length>0){
                for(RequestMethod item : classMethods){
                    methods.add(item.name());
                }
            }
            ////////////////////////////////////////////
            urls.addAll(genUrls(classUrl,requestMapping.path(),requestMapping.value()));
            ////////////////////////////////////////////
            consumers.addAll(genMerge("ALL", requestMapping.consumes(), classConsumers));
            ////////////////////////////////////////////
            products.addAll(genMerge("ALL",requestMapping.produces(),classProducts));
        }

        if(getMapping!=null){
            methods.add(RequestMethod.GET.name());
            ////////////////////////////////////////////
            urls.addAll(genUrls(classUrl,getMapping.path(),getMapping.value()));
            ////////////////////////////////////////////
            consumers.addAll(genMerge("ALL", getMapping.consumes(), classConsumers));
            ////////////////////////////////////////////
            products.addAll(genMerge("ALL",getMapping.produces(),classProducts));
        }

        if(postMapping!=null){
            methods.add(RequestMethod.POST.name());
            ////////////////////////////////////////////
            urls.addAll(genUrls(classUrl,postMapping.path(),postMapping.value()));
            ////////////////////////////////////////////
            consumers.addAll(genMerge("ALL", postMapping.consumes(), classConsumers));
            ////////////////////////////////////////////
            products.addAll(genMerge("ALL",postMapping.produces(),classProducts));
        }

        if(putMapping!=null){
            methods.add(RequestMethod.PUT.name());
            ////////////////////////////////////////////
            urls.addAll(genUrls(classUrl,putMapping.path(),putMapping.value()));
            ////////////////////////////////////////////
            consumers.addAll(genMerge("ALL", putMapping.consumes(), classConsumers));
            ////////////////////////////////////////////
            products.addAll(genMerge("ALL",putMapping.produces(),classProducts));
        }

        if(deleteMapping!=null){
            methods.add(RequestMethod.DELETE.name());
            ////////////////////////////////////////////
            urls.addAll(genUrls(classUrl,deleteMapping.path(),deleteMapping.value()));
            ////////////////////////////////////////////
            consumers.addAll(genMerge("ALL", deleteMapping.consumes(), classConsumers));
            ////////////////////////////////////////////
            products.addAll(genMerge("ALL",deleteMapping.produces(),classProducts));
        }

        if(patchMapping!=null){
            methods.add(RequestMethod.PATCH.name());
            ////////////////////////////////////////////
            urls.addAll(genUrls(classUrl,patchMapping.path(),patchMapping.value()));
            ////////////////////////////////////////////
            consumers.addAll(genMerge("ALL", patchMapping.consumes(), classConsumers));
            ////////////////////////////////////////////
            products.addAll(genMerge("ALL",patchMapping.produces(),classProducts));
        }

        String remark="";
        if(methods.size()>0 && (responseBody!=null || classResponseBody!=null || classRestController!=null)){
            remark=RequestBody.class.getSimpleName();
        }


        api.setMethods(methods);
        api.setUrls(urls);
        api.setConsumers(consumers);
        api.setProducts(products);
        api.setRemark(remark);

    }

    protected void parseSwaggerMethod(){
        if(!swaggerSupport()){
            return;
        }
        Class clazz=method.getDeclaringClass();
        Api annApi= ReflectResolver.findAnnotation(clazz,Api.class,false);
        ApiOperation annOpe= ReflectResolver.findAnnotation(method,ApiOperation.class,false);
        String comment="";
        if(annApi!=null){
            comment+=annApi.value();
            String[] annTags=annApi.tags();
            if(annTags.length>0 && !(annTags.length==1 && "".equals(annTags[0]))){
                comment+="[";
                for (int i = 0; i < annTags.length; i++) {
                    if(i!=0){
                        comment+=", ";
                    }
                    comment+=annTags[i];
                }
                comment+="]";
            }
        }

        if(annOpe!=null){
            if(!"".equals(comment)){
                comment+=" >> ";
            }

            comment+=annOpe.value();
            if(!"".equals(annOpe.notes())) {
                comment+=" (";
                comment += annOpe.notes();
                comment+=") ";
            }
            String[] annTags=annOpe.tags();
            if(annTags.length>0 && !(annTags.length==1 && "".equals(annTags[0]))){
                comment+="[";
                for (int i = 0; i < annTags.length; i++) {
                    if(i!=0){
                        comment+=", ";
                    }
                    comment+=annTags[i];
                }
                comment+="]";
            }
        }

        api.setComment(comment);
    }

    protected void parseReturn(){
        Class<?> type = method.getReturnType();
        Type genericType = method.getGenericReturnType();
        List<ApiLine> lines=new ArrayList<>();

        ApiLine line=new ApiLine();
        line.setName("_");
        line.setParent("");
        line.setType(type);
        line.setOrder("1");
        if(!type.equals(genericType)){
            line.setType(genericType);
        }

        if(swaggerSupport()){
            ApiModel annModel= ReflectResolver.findAnnotation(type,ApiModel.class,false);
            if(annModel!=null){
                line.setComment(annModel.value()+" "+annModel.description());
            }
        }

        line.setRoute(line.getName());
        lines.add(line);
        if(!type.getName().startsWith("java.lang.")) {
            lines.addAll(genLines(type, genericType, line.getName(),line.getOrder(),line.getRoute()));
        }
        api.setReturns(lines);
    }

    public static String[] getParameterNames(Method method){
        if(springCoreSupport()){
            return new LocalVariableTableParameterNameDiscoverer().getParameterNames(method);
        }
        Parameter[] parameters = method.getParameters();
        String[] names=new String[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            names[i]=parameters[i].getName();
        }
        return names;
    }


    protected void parseArguments(){
        Parameter[] parameters=method.getParameters();
        String[] names= getParameterNames(method);
        List<ApiLine> lines=new ArrayList<>();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter=parameters[i];
            Class<?> type = parameter.getType();
            Type genericType = parameter.getParameterizedType();
            String name=names[i];

            ApiLine line=new ApiLine();
            line.setName(name);
            line.setParent("");
            line.setType(type);
            line.setOrder("1");
            if(!type.equals(genericType)){
                line.setType(genericType);
            }

            if(swaggerSupport()){
                ApiParam annParam= ReflectResolver.findAnnotation(parameter,ApiParam.class,false);
                if(annParam!=null){
                    if(!"".equals(annParam.value())){
                        line.setName(annParam.value());
                    }
                    if(!"".equals(annParam.name())){
                        line.setName(annParam.name());
                    }
                    if(annParam.required()){
                        line.setRestrict("require");
                    }
                    line.setRemark(annParam.example());
                }
                ApiModel annModel= ReflectResolver.findAnnotation(type,ApiModel.class,false);
                if(annModel!=null){
                    line.setComment(annModel.value()+" "+annModel.description());
                }
            }

            if(springMvcSupport()){
                RequestParam annReqParam= ReflectResolver.findAnnotation(parameter,RequestParam.class,false);
                if(annReqParam!=null){
                    if(!"".equals(annReqParam.value())){
                        line.setName(annReqParam.value());
                    }
                    if(!"".equals(annReqParam.name())){
                        line.setName(annReqParam.name());
                    }
                    if(annReqParam.required()){
                        line.setRestrict("require");
                    }
                    line.setRemark(RequestParam.class.getSimpleName());
                }
                RequestBody annReqBody= ReflectResolver.findAnnotation(parameter,RequestBody.class,false);
                if(annReqBody!=null){
                    if(annReqBody.required()){
                        line.setRestrict("require");
                    }
                    line.setRemark(RequestBody.class.getSimpleName());
                }
                PathVariable annPathVar= ReflectResolver.findAnnotation(parameter,PathVariable.class,false);
                if(annPathVar!=null){
                    if(!"".equals(annPathVar.value())){
                        line.setName(annPathVar.value());
                    }
                    if(!"".equals(annPathVar.name())){
                        line.setName(annPathVar.name());
                    }
                    if(annPathVar.required()){
                        line.setRestrict("require");
                    }
                    line.setRemark(PathVariable.class.getSimpleName());
                }
            }

            line.setRoute(line.getName());
            lines.add(line);
            if(!type.getName().startsWith("java.lang.")) {
                lines.addAll(genLines(type, genericType, line.getName(),line.getOrder(),line.getRoute()));
            }
        }

        api.setArgs(lines);
    }

    public static List<ApiLine> genLines(Class type, Type genericType, String parent, String order, String route){
        List<ApiLine> ret=new ArrayList<>();
        Map<Field,Class> fieldsMap=forceAllFields(type);
        for(Map.Entry<Field,Class> entry : fieldsMap.entrySet()){
            Field field = entry.getKey();
            if(Modifier.isStatic(field.getModifiers())){
                continue;
            }
            Class clazz = entry.getValue();
            Class fieldType=field.getType();
            Type fieldGenericType=field.getGenericType();
            ApiLine line=new ApiLine();
            line.setName(field.getName());
            line.setParent(parent);
            line.setType(fieldType);
            line.setOrder(order+"1");
            if(!fieldType.equals(fieldGenericType)){
                line.setType(fieldGenericType);
            }
            if(!fieldType.equals(fieldGenericType)){
                if(genericType instanceof ParameterizedType){
                    ParameterizedType ptype=(ParameterizedType)genericType;
                    Type[] typeArgs = ptype.getActualTypeArguments();
                    if(typeArgs.length==1){
                        line.setType(typeArgs[0]);
                    }
                }
            }
            if(swaggerSupport()){
                ApiModelProperty annProp= ReflectResolver.findAnnotation(field,ApiModelProperty.class,false);
                if(annProp!=null){
                    line.setComment(annProp.value());
                    line.setRemark(annProp.example());
                    line.setRestrict(annProp.required()?"require":"");
                }
            }
            line.setRoute(route+"."+line.getName());
            ret.add(line);
            if(!fieldType.getName().startsWith("java.lang.")) {
                if(!type.equals(fieldType)){
                    List<ApiLine> nexts = genLines(fieldType, fieldGenericType, line.getName(),line.getOrder(),line.getRoute());
                    ret.addAll(nexts);
                }
            }
            if(!fieldType.equals(fieldGenericType)){
                if(fieldGenericType instanceof TypeVariable){
                    TypeVariable typeVariable=(TypeVariable)fieldGenericType;
                    GenericDeclaration genericDeclaration = typeVariable.getGenericDeclaration();
                    TypeVariable<?>[] typeParameters = genericDeclaration.getTypeParameters();
                    if(genericType instanceof ParameterizedType){
                        ParameterizedType ptype=(ParameterizedType)genericType;
                        Type rawType = ptype.getRawType();
                        Type[] typeArgs = ptype.getActualTypeArguments();
                        if(typeArgs.length==1){
                            try{
                                Type typeArg = typeArgs[0];
                                if(typeArg instanceof Class) {
                                    Class nextClazz = (Class)typeArg;
                                    Type nextGenericType = nextClazz.getGenericSuperclass();
                                    if(!type.equals(nextClazz)){
                                        List<ApiLine> nexts = genLines(nextClazz, nextGenericType, line.getName(), line.getOrder(),line.getRoute());
                                        ret.addAll(nexts);
                                    }
                                }else if(typeArg instanceof ParameterizedType){
                                    ParameterizedType nextPtype = (ParameterizedType)typeArg;
                                    if(!existsRecursiveParameterizedType(type,nextPtype)) {
                                        List<ApiLine> nexts = resolveParameterizedType(nextPtype, line.getName(), line.getOrder(), line.getRoute());
                                        ret.addAll(nexts);
                                    }
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }else if(fieldGenericType instanceof ParameterizedType){
                    ParameterizedType nextPramType = (ParameterizedType)fieldGenericType;
                    if(!existsRecursiveParameterizedType(type,nextPramType)){
                        List<ApiLine> nexts=resolveParameterizedType(nextPramType,line.getName(),line.getOrder(),line.getRoute());
                        ret.addAll(nexts);
                    }

                }
            }
        }

        return ret;
    }

    public static boolean existsRecursiveParameterizedType(Type classType,ParameterizedType nextPramType){
        boolean isRecursive=false;
        Type[] nextArgs = nextPramType.getActualTypeArguments();
        for (int i = 0; i < nextArgs.length; i++) {
            if(nextArgs[i].equals(classType)){
                isRecursive=true;
                break;
            }
        }
        return isRecursive;
    }

    public static List<ApiLine> resolveParameterizedType(ParameterizedType nextPramType, String parent, String order, String route) {
        List<ApiLine> ret = new ArrayList<>();
        Class nextParamRawType = (Class) nextPramType.getRawType();
        if(Iterable.class.isAssignableFrom(nextParamRawType) || Enumeration.class.isAssignableFrom(nextParamRawType)){ // 处理collection中的泛型
            String nextName=parent+"$elem";
            Type[] nextArgs = nextPramType.getActualTypeArguments();
            Type typeArg=nextArgs[0];
            ApiLine nextLine=new ApiLine();
            nextLine.setName(nextName);
            nextLine.setParent(parent);
            nextLine.setType(typeArg);
            nextLine.setOrder(order+"1");
            nextLine.setRoute(route+"."+nextLine.getName());
            ret.add(nextLine);
            if(!typeArg.getTypeName().startsWith("java.lang.")){
                if(typeArg instanceof Class) {
                    Class nextClazz = (Class)typeArg;
                    Type nextGenericType = nextClazz.getGenericSuperclass();
                    List<ApiLine> nexts = genLines(nextClazz, nextGenericType, nextName, nextLine.getOrder(),nextLine.getRoute());
                    ret.addAll(nexts);
                }else if(typeArg instanceof ParameterizedType){
                    ParameterizedType nextPtype = (ParameterizedType)typeArg;
                    Class nextRawType = (Class)nextPtype.getRawType();
                    List<ApiLine> nexts = genLines(nextRawType, nextPtype, nextName, nextLine.getOrder(),nextLine.getRoute());
                    ret.addAll(nexts);
                }
            }

        }else if(Map.class.isAssignableFrom(nextParamRawType)){ // 处理map中的泛型
            String nextKeyName=parent+"$key";
            Type[] nextArgs = nextPramType.getActualTypeArguments();
            Type typeArg=nextArgs[0];
            ApiLine nextLine=new ApiLine();
            nextLine.setName(nextKeyName);
            nextLine.setParent(parent);
            nextLine.setType(typeArg);
            nextLine.setOrder(order+"1");
            nextLine.setRoute(route+"."+nextLine.getName());
            ret.add(nextLine);
            if(!typeArg.getTypeName().startsWith("java.lang.")){
                if(typeArg instanceof Class) {
                    Class nextClazz = (Class)typeArg;
                    Type nextGenericType = nextClazz.getGenericSuperclass();
                    List<ApiLine> nexts = genLines(nextClazz, nextGenericType, nextKeyName, nextLine.getOrder(),nextLine.getRoute());
                    ret.addAll(nexts);
                }else if(typeArg instanceof ParameterizedType){
                    ParameterizedType nextPtype = (ParameterizedType)typeArg;
                    Class nextRawType = (Class)nextPtype.getRawType();
                    List<ApiLine> nexts = genLines(nextRawType, nextPtype, nextKeyName, nextLine.getOrder(),nextLine.getRoute());
                    ret.addAll(nexts);
                }
            }
            String nextValueName=parent+"$value";
            typeArg=nextArgs[1];
            nextLine=new ApiLine();
            nextLine.setName(nextValueName);
            nextLine.setParent(parent);
            nextLine.setType(typeArg);
            nextLine.setOrder(order+"1");
            nextLine.setRoute(route+"."+nextLine.getName());
            ret.add(nextLine);
            if(!typeArg.getTypeName().startsWith("java.lang.")){
                if(typeArg instanceof Class) {
                    Class nextClazz = (Class)typeArg;
                    Type nextGenericType = nextClazz.getGenericSuperclass();
                    List<ApiLine> nexts = genLines(nextClazz, nextGenericType, nextValueName, nextLine.getOrder(),nextLine.getRoute());
                    ret.addAll(nexts);
                }else if(typeArg instanceof ParameterizedType){
                    ParameterizedType nextPtype = (ParameterizedType)typeArg;
                    Class nextRawType = (Class)nextPtype.getRawType();
                    List<ApiLine> nexts = genLines(nextRawType, nextPtype, nextValueName, nextLine.getOrder(),nextLine.getRoute());
                    ret.addAll(nexts);
                }
            }
        }else {
            List<ApiLine> nexts = genLines(nextParamRawType, nextPramType, parent, order,route);
            ret.addAll(nexts);
        }

        return ret;
    }

    public static String getTypeName(Type type,boolean keepJavaLang,boolean keepAll){
        if(type instanceof Class){
            Class clazz=(Class)type;
            return getTypeName(clazz.getName(),keepJavaLang,keepAll);
        }else if(type instanceof ParameterizedType){
            ParameterizedType ptype=(ParameterizedType)type;
            Type rawType = ptype.getRawType();
            Type[] typeArgs = ptype.getActualTypeArguments();
            String name=getTypeName(rawType,keepJavaLang,keepAll);
            if(typeArgs.length>0){
                name+="<";
                for (int i = 0; i < typeArgs.length; i++) {
                    if(i!=0){
                        name+=", ";
                    }
                    name+=getTypeName(typeArgs[i],keepJavaLang,keepAll);
                }
                name+=">";
            }
            return name;
        }else if(type instanceof TypeVariable){

        }
        return getTypeName(type.getTypeName(),keepJavaLang,keepAll);
    }

    public static String getTypeName(String name,boolean keepJavaLang,boolean keepAll){
        if(keepAll){
            return name;
        }
        if(keepJavaLang && name.startsWith("java.lang.")){
            return name;
        }
        return getShortTypeName(name);
    }

    public static String getShortTypeName(String name){
        int idx=name.lastIndexOf(".");
        if(idx>=0){
            return name.substring(idx+1);
        }
        return name;
    }

    public static Map<Field,Class> forceAllFields(Class clazz){
        Map<Field,Class> ret=new HashMap<>();
        Set<Field> fs=getAllFields(clazz);
        for(Field item : fs){
            ret.put(item,clazz);
        }
        Class superclass = clazz.getSuperclass();
        if(superclass!=null && !Object.class.equals(superclass)){
            Map<Field,Class> nexts=forceAllFields(superclass);
            ret.putAll(nexts);
        }
        return ret;
    }

    public static Set<Field> getAllFields(Class clazz){
        Set<Field> ret=new HashSet<>();
        Field[] dfs= clazz.getDeclaredFields();
        for(Field item : dfs){
            ret.add(item);
        }
        Field[] fs=clazz.getFields();
        for(Field item : fs){
            ret.add(item);
        }
        return ret;
    }

    public static boolean swaggerSupport(){
        String className="io.swagger.annotations.ApiModelProperty";
        return componentSupport(className);
    }

    public static boolean springCoreSupport(){
        String className="org.springframework.core.env.Environment";
        return componentSupport(className);
    }

    public static boolean springMvcSupport(){
        String className="org.springframework.web.bind.annotation.RequestMapping";
        return componentSupport(className);
    }

    public static boolean componentSupport(String componentClassName){
        boolean componentFind=false;
        try{
            Class<?> aClass = Class.forName(componentClassName);
            if(aClass!=null){
                componentFind=true;
            }
        }catch(Exception e){

        }
        return componentFind;
    }

    protected static List<String> genMerge(String defVal,String[] primarys,String[] secondarys){
        List<String> ret=new ArrayList<>();
        if(primarys.length==0 && secondarys.length==0){
            if(defVal!=null){
                ret.add(defVal);
            }
        }else if(primarys.length>0){
            for(String item : primarys){
                ret.add(item);
            }
        }else if(secondarys.length>0){
            for(String item : secondarys){
                ret.add(item);
            }
        }
        return ret;
    }

    protected static List<String> genUrls(String[] basePaths,String[] paths,String[] values){
        List<String> ret=new ArrayList<>();
        if(paths.length>0){
            ret.addAll(genUrls(basePaths,paths));
        }else{
            ret.addAll(genUrls(basePaths,values));
        }
        return ret;
    }

    protected static List<String> genUrls(String[] basePaths,String[] paths){
        List<String> ret=new ArrayList<>();
        if(basePaths==null || basePaths.length==0){
            basePaths=new String[]{""};
        }
        if(paths==null || paths.length==0){
            paths=new String[]{""};
        }
        for(String base : basePaths){
            for(String path : paths){
                String url=keepPathSep(base,path);
                ret.add(url);
            }
        }

        return ret;
    }

    public static String keepPathSep(String str1,String str2){
        return keepSep("/",str1,str2);
    }

    public static String keepSep(String sep,String str1,String str2){
        if(str1.endsWith(sep)){
            if(str2.startsWith(sep)){
                return str1+str2.substring(sep.length());
            }else{
                return str1+str2;
            }
        }else{
            if(str2.startsWith(sep)){
                return str1+str2;
            }else{
                return str1+sep+str2;
            }
        }
    }


}
