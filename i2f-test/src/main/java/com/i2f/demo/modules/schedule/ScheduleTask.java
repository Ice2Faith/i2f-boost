package com.i2f.demo.modules.schedule;

import com.i2f.demo.modules.mybatis.domain.ConfigDomain;
import com.i2f.demo.modules.mybatis.mapper.ConfigMapper;
import i2f.extension.quartz.driven.anntation.QuartzSchedule;
import i2f.springboot.refresh.RefreshConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/3/27 16:11
 * @desc
 */
@Slf4j
@Configuration
public class ScheduleTask {

    @Resource
    private ConfigMapper configMapper;

    @Autowired
    private RefreshConfig refreshConfig;

//    @Scheduled(fixedRate = 60*1000)
    public void doTask(){
        log.info("task...");
        ConfigDomain req=new ConfigDomain();
        req.setGroupKey("media");
        req.setTypeKey("media_type");
        List<Map<String,Object>> list= configMapper.qryConfig(req);
        System.out.println(list);

        List<ConfigDomain> domain= configMapper.qryDomain(req);
        System.out.println(domain);
        refreshConfig.refresh();
    }

    @QuartzSchedule(name="task",group = "quartz",intervalTimeUnit = TimeUnit.SECONDS)
    public void doQuartzTask(){
        log.info("task...");
        ConfigDomain req=new ConfigDomain();
        req.setGroupKey("media");
        req.setTypeKey("media_type");
        List<Map<String,Object>> list= configMapper.qryConfig(req);
        System.out.println(list);

        List<ConfigDomain> domain= configMapper.qryDomain(req);
        System.out.println(domain);
    }
}
