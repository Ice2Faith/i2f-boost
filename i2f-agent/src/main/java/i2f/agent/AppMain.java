package i2f.agent;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import i2f.core.lang.agent.AgentUtil;

import java.net.URL;
import java.util.List;
import java.util.Scanner;

/**
 * @author ltb
 * @date 2022/7/4 14:06
 * @desc
 */
public class AppMain {
    public static void main(String[] args) throws Exception {
        Scanner scanner=new Scanner(System.in);
        System.out.println("------------------------------------------------");
        System.out.println("java process list:");
        List<VirtualMachineDescriptor> vmds= VirtualMachine.list();
        System.out.println("[index]\t[pid]\t[displayName]");
        for (int i = 0; i <vmds.size(); i++) {
            VirtualMachineDescriptor item=vmds.get(i);
            System.out.println("["+i+"]\t["+ item.id()+"]\t"+item.displayName());
        }
        System.out.println("------------------------------------------------");
        System.out.println("please select one to attach:");
        System.out.print(">/ ");
        int idx= scanner.nextInt();
        VirtualMachineDescriptor vm=null;
        if(idx>=0 && idx< vmds.size()){
            vm=vmds.get(idx);
        }
        if(vm==null){
            System.out.println("not valid index select,exit!");
            return;
        }
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String name=AppMain.class.getName().replaceAll("\\.","/")+".class";
        URL url = loader.getResource(name);
        if(url==null){
            System.out.println("not found resource,exit!");
            return;
        }
        String file = url.getPath();
        idx=file.indexOf("!");
        if(idx<0){
            System.out.println("not jar env,exit!");
            return;
        }
        String agentJar=file.substring(0,idx);
        agentJar=agentJar.substring("file:/".length());
        System.out.println("agentJar:"+agentJar);

        System.out.println("------------------------------------------------");
        scanner.nextLine();
        String agentArg="args,ret,stat@com.i2f.**&stat@java.util.**";
        System.out.println("please input agent argument:");
        System.out.println("actions@method-ant-pattens&actions@method-ant-pattens");
        System.out.println("actions include: args,ret,stat");
        System.out.println("\targs: print method arguments");
        System.out.println("\tret:  print method return value");
        System.out.println("\tstat: print invoke method use time");
        System.out.println("method-ant-pattens means: the full name of an method's patten(ant)");
        System.out.println("\tsuch:com.**.save* will match com.TestMapper.saveData or com.i2f.UserService.saveUser");
        System.out.println("simple case:");
        System.out.println("\t"+agentArg);
        System.out.println("------------------------------------------------");
        System.out.println("please input watch argument:");
        System.out.print(">/ ");
        agentArg=scanner.nextLine();

        System.out.println("attach to process:"+vm.id()+"\t"+vm.displayName());
        VirtualMachine jvm=AgentUtil.agentByPid(vm.id(),agentJar,agentArg);

        System.out.println("attached.");

//        System.out.println("input any number exit.");
//        int exit=scanner.nextInt();
    }
}
