package i2f.generator.module;

import i2f.core.reflect.core.ReflectResolver;
import i2f.generator.GeneratorDriver;
import i2f.generator.api.ApiMethod;
import i2f.generator.api.ApiMethodResolver;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023/5/13 22:10
 * @desc
 */
public class ModuleResolver {
    public static ModuleController parse(Class<?> clazz) {
        return new ModuleResolver(clazz).parse();
    }

    private Class<?> clazz;
    private ModuleController module;

    public ModuleResolver(Class<?> clazz) {
        this.clazz = clazz;
    }

    public ModuleController parse() {
        module = new ModuleController();
        parseClassBasic();
        parseClassMvc();
        parseClassSwagger();
        parseApiMethods();


        return module;
    }

    public void parseClassBasic() {
        module.setMethods(new ArrayList<>());
        module.setClazz(clazz);
        String clazzName = clazz.getName();
        if (clazzName != null) {
            int idx = clazzName.indexOf("$$Enhancer");
            if (idx >= 0) {
                clazzName = clazzName.substring(0, idx);
            }
        }
        module.setFullClassName(clazzName);
        String simpleName = clazzName;
        if (simpleName != null) {
            int idx = simpleName.lastIndexOf(".");
            if (idx >= 0) {
                simpleName = simpleName.substring(0, idx);
            }
        }
        module.setClassName(simpleName);
        String trimName = simpleName;
        int idx = trimName.indexOf("Controller");
        if (idx >= 0) {
            trimName = trimName.substring(0, idx);
        }
        module.setTrimName(trimName);
    }

    public void parseClassMvc() {
        if (!ApiMethodResolver.springMvcSupport()) {
            return;
        }
        RequestMapping classRequestMapping = ReflectResolver.findAnnotation(clazz, RequestMapping.class, false);
        ResponseBody classResponseBody = ReflectResolver.findAnnotation(clazz, ResponseBody.class, false);
        RestController classRestController = ReflectResolver.findAnnotation(clazz, RestController.class, false);


        String[] classUrl = new String[0];
        if (classRequestMapping != null) {
            if (classRequestMapping.path().length > 0) {
                classUrl = classRequestMapping.path();
            } else {
                classUrl = classRequestMapping.value();
            }
        }
        if (classUrl.length > 0) {
            module.setBaseUrl(classUrl[0]);
        } else {
            module.setBaseUrl("");
        }

        String remark = "";
        if (classResponseBody != null || classRestController != null) {
            remark = RequestBody.class.getSimpleName();
        }
        module.setRemark(remark);
    }

    public void parseClassSwagger() {
        if (!ApiMethodResolver.swaggerSupport()) {
            return;
        }
        Api annApi = ReflectResolver.findAnnotation(clazz, Api.class, false);
        String comment = "";
        if (annApi != null) {
            comment += annApi.value();
            String[] annTags = annApi.tags();
            if (annTags.length > 0 && !(annTags.length == 1 && "".equals(annTags[0]))) {
                comment += "[";
                for (int i = 0; i < annTags.length; i++) {
                    if (i != 0) {
                        comment += ", ";
                    }
                    comment += annTags[i];
                }
                comment += "]";
            }
        }
        module.setComment(comment);
    }

    public void parseApiMethods() {
        List<ApiMethod> methods = GeneratorDriver.getMvcApiMethods(clazz, ApiMethodResolver.TraceLevel.NONE);
        module.setMethods(methods);
    }
}
