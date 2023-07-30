package com.i2f.demo.console.drools;

import com.i2f.demo.console.drools.model.HelloDto;
import i2f.core.io.stream.StreamUtil;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * @author ltb
 * @date 2022/5/3 20:27
 * @desc
 */
public class TestHelloDrools {
    public static void main(String[] args) throws Exception {
//        hello();
//        holder();
        drl();
    }

    public static void drl() throws IOException {
        File dir=new File("D:\\IDEA_ROOT\\DevCenter\\i2f-boost\\i2f-test\\src\\main\\resources\\drools");
        File drlFile=new File(dir,"Hello.drl");
        String drl = StreamUtil.readString(drlFile, "UTF-8");
        KieBase base = DroolsUtil.getBaseByDrl(drl);
        Random rand=new Random();
        for(int i=0;i<5;i++) {
            System.out.println("---------------------------------------");

            KieSession kieSession = base.newKieSession();
            HelloDto bean = new HelloDto();
            bean.setInput(rand.nextInt(50));
            System.out.println("init:input="+bean.getInput()+",output="+bean.getOutput());

            DefaultDroolsHolder.droolsFire(kieSession,bean);
            System.out.println("result:input="+bean.getInput()+",output="+bean.getOutput());

        }

    }

    public static void holder(){
        Random rand=new Random();
        for(int i=0;i<5;i++) {
            System.out.println("---------------------------------------");

            HelloDto bean = new HelloDto();
            bean.setInput(rand.nextInt(50));
            System.out.println("init:input="+bean.getInput()+",output="+bean.getOutput());

            DefaultDroolsHolder.defaultDroolsFire(bean);
            System.out.println("result:input="+bean.getInput()+",output="+bean.getOutput());

        }

    }

    public static void hello(){
        /**
         * drools配置文件
         * 在classpath下面的
         * org.drools.compiler.kie.builder.impl.ClasspathKieProject类中指定了
         * 方法：discoverKieModules
         * String[] configFiles = new String[]{"META-INF/kmodule.xml", "META-INF/kmodule-spring.xml"};
         * 因此，配置文件名称只能是这二者之一，放在classpath下面
         */
        Random rand=new Random();
        // 获取KieServices
        KieServices kieServices=KieServices.Factory.get();
        // 获取规则保持对象
        KieContainer kieContainer=kieServices.newKieClasspathContainer();

        for(int i=0;i<5;i++){
            System.out.println("---------------------------------------");
            System.out.println("----begin session----");
            // 获取一次回话的Session
            KieSession session=kieContainer.newKieSession();

            HelloDto bean=new HelloDto();
            bean.setInput(rand.nextInt(50));

            System.out.println("----insert context bean----");
            // 将规则匹配用到的bean放入session中
            session.insert(bean);
            System.out.println("spy:input="+bean.getInput()+",output="+bean.getOutput());

            System.out.println("----fire rule----");
            // 进行规则匹配并执行规则
            session.fireAllRules();
            // 执行fire之后，其实值就已经获取到值了，因此已经可以直接使用了
            System.out.println("spy:input="+bean.getInput()+",output="+bean.getOutput());

            System.out.println("----dispose session----");
            // 关闭回话
            session.dispose();
            System.out.println("spy:input="+bean.getInput()+",output="+bean.getOutput());
        }
    }


}
