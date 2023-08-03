package i2f.springboot.limit;

import i2f.springboot.limit.consts.LimitType;
import i2f.springboot.limit.core.LimitContext;
import i2f.springboot.limit.data.LimitAnnDto;
import i2f.springboot.limit.data.LimitPropDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2023/8/3 9:22
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.limit.enable:true}")
@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "i2f.springboot.config.limit")
public class LimitConfig implements InitializingBean {

    @Autowired
    private LimitContext limitContext;

    private LimitPropDto global;

    private LimitPropDto ip;

    private LimitPropDto user;

    @Override
    public void afterPropertiesSet() throws Exception {
        if(global!=null && global.isEnable()){
            limitContext.getDefaultLimitMap().put(LimitType.GLOBAL,parse(global,LimitType.GLOBAL));
        }
        if(ip!=null && ip.isEnable()){
            limitContext.getDefaultLimitMap().put(LimitType.IP,parse(ip,LimitType.IP));
        }
        if(user!=null && user.isEnable()){
            limitContext.getDefaultLimitMap().put(LimitType.USER,parse(user,LimitType.USER));
        }

        log.info("LimitConfig config done.");
    }

    public static LimitAnnDto parse(LimitPropDto dto,LimitType type){
        LimitAnnDto ret=new LimitAnnDto();
        ret.setValue(dto.isEnable());
        ret.setWindow(dto.getWindow());
        TimeUnit unit=TimeUnit.SECONDS;
        for (TimeUnit item : TimeUnit.values()) {
            if(String.valueOf(item).equalsIgnoreCase(dto.getUnit())){
                unit=item;
                break;
            }
        }
        ret.setUnit(unit);
        ret.setCount(dto.getCount());
        ret.setType(type);
        return ret;
    }
}
