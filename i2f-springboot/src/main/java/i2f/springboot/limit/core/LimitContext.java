package i2f.springboot.limit.core;

import i2f.springboot.limit.consts.LimitType;
import i2f.springboot.limit.data.LimitAnnDto;
import i2f.springboot.limit.data.LimitDto;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Ice2Faith
 * @date 2023/8/3 8:57
 * @desc
 */
@Data
@Component
public class LimitContext {
    // 默认限流策略，全局性
    private Map<LimitType, LimitAnnDto> defaultLimitMap=new ConcurrentHashMap<>();

    // 当前运行的限流策略，也就是说，运行中可以实施变更此内容实现动态限流
    // 键值对应关系：{key:方法签名,value:{key:限流类型,value:{key:限流主体,value:限流控制信息}}}
    // 比如：{key:getUserInfo,value:{key:IP,value:{key:127.0.0.1,value:{xxx}}}}
    private Map<String, Map<LimitType, Map<String,LimitDto>>> limits=new ConcurrentHashMap<>();

    // 限流的键对应的方法，方便拓展获取一些其他的元信息进行页面图形化管理
    private Map<String, Method> limitResourcesMap=new ConcurrentHashMap<>();

}
