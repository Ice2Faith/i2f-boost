package com.i2f.demo.modules.generate;

import i2f.generator.GeneratorDriver;
import i2f.generator.data.JavaCodeContext;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author ltb
 * @date 2022/6/15 17:17
 * @desc
 */
@Component
public class Generator implements ApplicationListener<ApplicationEvent> {
    @Autowired
    private DataSource dataSource;

    @SneakyThrows
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        Connection conn= dataSource.getConnection();

        JavaCodeContext code=new JavaCodeContext();
        code.setPkg("com.i2f.agen");
        code.setAuthor("i2f");

        String templatePath="D:\\IDEA_ROOT\\DevCenter\\i2f-boost\\i2f-generator\\src\\main\\resources\\tpl";
        String outputPath="D:\\IDEA_ROOT\\DevCenter\\i2f-boost\\i2f-test\\src\\main\\java\\com\\i2f\\agen";
        GeneratorDriver.batch(conn, "sys_config",templatePath,outputPath,code);

        conn.close();

    }
}
