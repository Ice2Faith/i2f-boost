package i2f.agent;

import i2f.core.lang.agent.AgentUtil;
import i2f.core.match.StringMatcher;

import java.lang.instrument.Instrumentation;
import java.util.*;

/**
 * @author ltb
 * @date 2022/4/3 13:02
 * @desc 一个简单的监控程序示例
 * 参数：
 * args,stat,ret@com.i2f.**,i2f.core.**&args@org.**
 * 参数解释:
 * 每一组参数使用&分隔
 * 因此上面就有两组
 * args,stat,ret@com.i2f.**,i2f.core.**
 * args@org.**
 * 每一组分为actions@full-method-ant-pattens
 * 含义为监视在一组ant-pattens定义的全限定函数名上的actions动作
 * args,stat,ret@com.i2f.**,i2f.core.**
 * 这一个就表示，监视com.i2f包和i2f.core包下面的所有方法的入参，统计，返回值
 * 可选的actions:
 * args: 监视入参
 * stat: 监视统计，调用耗时统计
 * ret: 监视返回值
 */
public class AgentMain {
    public static void premain(String arg, Instrumentation inst){
        agentProxy(arg,inst);
    }

    public static void agentmain(String arg, Instrumentation inst){
        agentProxy(arg,inst);
    }

    public static void agentProxy(String arg,Instrumentation inst){
        System.out.println("AgentMain start run ! , arg is " + arg);
        Map<String, Set<String>> actionPattens=new HashMap<>();
        String[] rules = arg.split("&");
        for(String rule : rules){
            String[] pair=rule.split("@",2);
            if(pair.length<2){
                continue;
            }
            String actions=pair[0];
            String pattens=pair[1];
            String[] pattenArr = pattens.split(",");
            String[] actionArr=actions.split(",");
            Set<String> set=new HashSet<>();
            for(String action : actionArr){
                action=action.trim().toLowerCase();
                if("".equals(action)){
                    continue;
                }
                set.add(action);
            }
            for(String patten : pattenArr){
                actionPattens.put(patten,set);
            }
        }

        inst.addTransformer(new InvokeWatchClassesTransformer(actionPattens),true);
        List<Class> list = AgentUtil.getLoadedClasses(inst);
        for(Class item : list){
            boolean isTar=false;
            for(String patten : actionPattens.keySet()){
                if(StringMatcher.antClass().match(item.getName(),patten)){
                    isTar=true;
                    break;
                }
            }
            if(item.getName().contains("$")){
                isTar=false;
            }
            if(isTar){
                try{
//                    System.out.println("retransform:"+item.getName());
                    inst.retransformClasses(item);
                }catch(Exception e){
                    e.printStackTrace();
//                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
