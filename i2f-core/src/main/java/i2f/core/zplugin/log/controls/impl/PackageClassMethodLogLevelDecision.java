package i2f.core.zplugin.log.controls.impl;

import i2f.core.match.StringMatcher;
import i2f.core.zplugin.log.context.LogLevelMappingHolder;
import i2f.core.zplugin.log.controls.LogDecision;
import i2f.core.zplugin.log.data.LogData;
import i2f.core.zplugin.log.enums.LogLevel;

import java.util.List;

/**
 * @author ltb
 * @date 2022/3/3 14:02
 * @desc
 */
public class PackageClassMethodLogLevelDecision implements LogDecision {
    @Override
    public boolean decision(LogData log) {
        LogLevel logLevel=log.getLevel();
        if(logLevel==null){
            return true;
        }
        String location=log.getClazzName();
        if(log.getMethodName()!=null && !"".equals(log.getMethodName())){
            location=log.getClazzName()+"."+log.getMethodName();
        }
        if(LogLevelMappingHolder.getMapping().isEmpty()){
            return log.getLevel().level()<=LogLevel.WARN.level();
        }
        List<String> priorPattens= StringMatcher.antClass().priorMatches(location,LogLevelMappingHolder.getMapping().keySet());
        if(priorPattens.size()>0){
            String patten= priorPattens.get(0);
            LogLevel level=LogLevelMappingHolder.level(patten);
            if(level!=null){
                return logLevel.level()<= level.level();
            }
        }

        return log.getLevel().level()<=LogLevel.WARN.level();
    }
}
