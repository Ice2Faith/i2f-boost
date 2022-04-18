package i2f.extension.quartz.driven;


import i2f.core.pkg.PackageScanner;
import i2f.core.pkg.data.ClassMetaData;
import i2f.core.reflect.core.ReflectResolver;
import i2f.extension.quartz.QuartzUtil;
import i2f.extension.quartz.driven.anntation.QuartzSchedule;
import i2f.extension.quartz.driven.enums.ScheduleType;
import i2f.extension.quartz.driven.job.QuartzAnnotationJob;
import i2f.extension.quartz.driven.model.QuartzJobMeta;
import org.quartz.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class QuartzScanner {
    public static List<QuartzJobMeta> scanBasePackage(String ... pkgs) throws IOException {
        List<ClassMetaData> classNames= PackageScanner.scanClasses(null,pkgs);
        List<Class> classes=new ArrayList<>();
        for(ClassMetaData item : classNames){
            classes.add(item.getClazz());
        }
        return scans(classes);
    }
    public static List<QuartzJobMeta> scans(List<Class> classes){
        List<QuartzJobMeta> ret=new ArrayList<>();
        for(Class item : classes){
            List<QuartzJobMeta> list=scan(item);
            ret.addAll(list);
        }
        return ret;
    }
    public static List<QuartzJobMeta> scans(Class ... clazzes){
        List<QuartzJobMeta> ret=new ArrayList<>();
        for(Class item : clazzes){
            List<QuartzJobMeta> list=scan(item);
            ret.addAll(list);
        }
        return ret;
    }
    public static List<QuartzJobMeta> scan(Class clazz){
        List<QuartzJobMeta> ret=new ArrayList<>();
        Set<Method> methods= ReflectResolver.getMethodsWithAnnotations(clazz,false,QuartzSchedule.class);
        if(methods.size()==0){
            return ret;
        }
        for(Method item : methods){
            QuartzSchedule ann= ReflectResolver.findAnnotation(item,QuartzSchedule.class,false);
            if(ann==null || !ann.value()){
                continue;
            }
            QuartzJobMeta meta= QuartzJobMeta.build()
                    .buildByAnnotation(ann)
                    .buildByMethod(item);
            ret.add(meta);
        }
        return ret;
    }


    public static Scheduler makeSchedule(Scheduler scheduler,QuartzJobMeta meta) throws SchedulerException {
        if(meta==null){
            return null;
        }
        JobDataMap dataMap=new JobDataMap();
        dataMap.put("meta",meta);
        JobDetail jobDetail= QuartzUtil.getJobDetail(QuartzAnnotationJob.class,meta.getName(), meta.getGroup(),dataMap);
        Trigger trigger=null;
        if(meta.getType()== ScheduleType.Interval){
            trigger=QuartzUtil.getIntervalTrigger(meta.getName(),meta.getGroup(),meta.getIntervalTimeUnit().toMillis(meta.getIntervalTime()),meta.getIntervalCount());
        }else if(meta.getType()== ScheduleType.Cron){
            trigger=QuartzUtil.getCronTrigger(meta.getName(),meta.getGroup(),meta.getCron());
        }
        TriggerKey triggerKey=QuartzUtil.triggerKey(meta.getName(),meta.getGroup());
        Trigger hasTrigger=scheduler.getTrigger(triggerKey);
        if(hasTrigger!=null){
            QuartzUtil.updateTrigger(scheduler,triggerKey,trigger);
        }else{
            QuartzUtil.doSchedule(scheduler,jobDetail,trigger);
        }
        return scheduler;
    }
}
