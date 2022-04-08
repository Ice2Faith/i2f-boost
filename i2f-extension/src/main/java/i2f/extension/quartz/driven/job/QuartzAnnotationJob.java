package i2f.extension.quartz.driven.job;


import i2f.core.reflect.core.ReflectResolver;
import i2f.extension.quartz.QuartzUtil;
import lombok.SneakyThrows;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class QuartzAnnotationJob implements Job {
    @SneakyThrows
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Method method=(Method) QuartzUtil.getJobData(jobExecutionContext,"method");
        if(Modifier.isStatic(method.getModifiers())){
            ReflectResolver.staticInvoke(method.getDeclaringClass(),method.getName());
        }else{
            Class clazz=method.getDeclaringClass();
            Object obj= ReflectResolver.instance(clazz);
            ReflectResolver.invoke(obj,method.getName());
        }
    }
}
