package i2f.extension.quartz.driven.job;


import i2f.core.reflection.reflect.core.ReflectResolver;
import i2f.extension.quartz.QuartzUtil;
import i2f.extension.quartz.driven.model.QuartzJobMeta;
import lombok.SneakyThrows;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

public class QuartzAnnotationJob implements Job {
    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        QuartzJobMeta meta=(QuartzJobMeta) QuartzUtil.getJobData(context,"meta");
        Method method=meta.getRunMethod();
        if(method==null){
            Class clazz=ReflectResolver.getClazz(meta.getRunClassName());
            Set<Method> methods=ReflectResolver.findMethodsByName(clazz,meta.getRunMethodName());
            method=methods.iterator().next();
            meta.setRunClass(clazz);
            meta.setRunMethod(method);
        }
        if(Modifier.isStatic(method.getModifiers())){
            ReflectResolver.staticInvoke(method.getDeclaringClass(),method.getName());
        }else{
            Class clazz=method.getDeclaringClass();
            Object obj= meta.getInvokeObj();
            if(obj==null){
                obj=ReflectResolver.instance(clazz);
            }
            ReflectResolver.invoke(obj,method.getName());
        }
    }
}
