package com.i2f.demo.console.drools;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

/**
 * @author ltb
 * @date 2022/5/3 21:16
 * @desc
 */
public class DefaultDroolsHolder {
    private static volatile KieServices kieServices;
    private static volatile KieContainer kieContainer;
    public static KieServices getKieServices(){
        if(kieServices==null){
            synchronized (DefaultDroolsHolder.class){
                if(kieServices==null){
                    kieServices=KieServices.Factory.get();
                }
            }
        }
        return kieServices;
    }
    public static KieContainer getDefaultContainer(){
        if(kieContainer==null){
            synchronized (DefaultDroolsHolder.class){
                if(kieContainer==null){
                    kieContainer=getKieServices().newKieClasspathContainer();
                }
            }
        }
        return kieContainer;
    }
    public static<T> T defaultDroolsFire(T bean){
        KieSession session=getDefaultContainer().newKieSession();
        return droolsFire(session,bean);
    }
    public static<T> T droolsFire(KieSession session, T bean){
        session.insert(bean);
        session.fireAllRules();
        session.dispose();
        return bean;
    }
}
