package i2f.agent;

import java.lang.instrument.Instrumentation;

/**
 * @author ltb
 * @date 2022/4/3 13:02
 * @desc
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
        String[] args=new String[0];
        if(arg!=null && !"".equals(arg)){
            args=arg.split(",");
        }
        inst.addTransformer(new InvokeTimeSysPrintClassesTransformer(args));
    }
}
