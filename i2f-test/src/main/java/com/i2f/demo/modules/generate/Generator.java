package com.i2f.demo.modules.generate;

import i2f.core.annotations.remark.Remark;
import i2f.core.db.data.TableColumnMeta;
import i2f.core.db.data.TableMeta;
import i2f.core.reflect.core.ReflectResolver;
import i2f.core.resource.ResourceUtil;
import i2f.generator.GeneratorDriver;
import i2f.generator.api.ApiLine;
import i2f.generator.api.ApiMethod;
import i2f.generator.er.ErEntity;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;

/**
 * @author ltb
 * @date 2022/6/15 17:17
 * @desc
 */
@RequestMapping("gen")
@Component
public class Generator implements ApplicationListener<ApplicationEvent> {
    @Autowired
    private DataSource dataSource;

    @SneakyThrows
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        Connection conn= dataSource.getConnection();

//        JavaCodeContext code=new JavaCodeContext();
//        code.setPkg("com.i2f.agen");
//        code.setAuthor("i2f");
//
//        String templatePath="D:\\IDEA_ROOT\\DevCenter\\i2f-boost\\i2f-generator\\src\\main\\resources\\tpl\\code";
//        String outputPath="D:\\IDEA_ROOT\\DevCenter\\i2f-boost\\i2f-test\\src\\main\\java\\com\\i2f\\agen";
//        GeneratorDriver.batch(conn, "sys_config",templatePath,outputPath,code);
//
//
//        String erTpl= ResourceUtil.getClasspathResourceAsString("tpl/er/er.xml.vm","UTF-8");
//
//        String er = GeneratorDriver.doc(conn, erTpl,
//                "ACT_RE_DEPLOYMENT",
//                "ACT_RE_MODEL",
//                "ACT_RE_PROCDEF",
//                "ACT_RU_EXECUTION",
//                "ACT_RU_JOB",
//                "ACT_RU_TASK",
//                "ACT_RU_TIMER_JOB");
//
//
//        System.out.println(er);
//
//
//        String docTpl= ResourceUtil.getClasspathResourceAsString("tpl/doc/doc.html.vm","UTF-8");
//
//        String doc = GeneratorDriver.doc(conn, docTpl,
//                "ACT_RE_DEPLOYMENT",
//                "ACT_RE_MODEL",
//                "ACT_RE_PROCDEF",
//                "ACT_RU_EXECUTION",
//                "ACT_RU_JOB",
//                "ACT_RU_TASK",
//                "ACT_RU_TIMER_JOB");
//
//        System.out.println(doc);
//
//

//        String apiTpl= ResourceUtil.getClasspathResourceAsString("tpl/api/api.html.vm","UTF-8");
//
//        String api = GeneratorDriver.api(conn, apiTpl,
//                "ACT_RE_DEPLOYMENT",
//                "ACT_RE_MODEL",
//                "ACT_RE_PROCDEF",
//                "ACT_RU_EXECUTION",
//                "ACT_RU_JOB",
//                "ACT_RU_TASK",
//                "ACT_RU_TIMER_JOB");
//
//        System.out.println(api);

        String apiVoTpl= ResourceUtil.getClasspathResourceAsString("tpl/api/api.html.vm","UTF-8");

        String apiVo = GeneratorDriver.apiVo(ApiMethod.class,apiVoTpl);

//        System.out.println(apiVo);

        String apiMethodTpl= ResourceUtil.getClasspathResourceAsString("tpl/api/api.html.vm","UTF-8");

        Method method = ReflectResolver.findMethodsByName(ApiMethod.class, "refresh").iterator().next();

        String apiMethod = GeneratorDriver.apiMethod(method,apiMethodTpl);

//        System.out.println(apiMethod);




        String apiMvcTpl= ResourceUtil.getClasspathResourceAsString("tpl/api/api.html.vm","UTF-8");

        String apiMvc = GeneratorDriver.apiMvc(this.getClass(),apiMvcTpl);

//        System.out.println(apiMvc);

        conn.close();

    }

    @Remark("欢迎接口")
    @PostMapping("hello")
    public Object hello(TableMeta meta,String userId){
        return null;
    }

    @Remark("GET的接口")
    @GetMapping("get")
    public Object get(TableColumnMeta column, String userId){
        return null;
    }

    @Remark("PUT的接口")
    @PutMapping("put")
    public Object put(ApiMethod ctx, String userId){
        return null;
    }

    @Remark("DELETE的接口")
    @DeleteMapping("delete")
    public Object get(ApiLine line, String userId){
        return null;
    }

    @Remark("ALL的接口")
    @RequestMapping("request")
    public Object get(ErEntity er, String userId){
        return null;
    }

    @Remark("多个映射的接口")
    @RequestMapping({"calc/add","calc/sub"})
    public Object calc(ErEntity er, String userId){
        return null;
    }
}
