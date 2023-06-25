package i2f.core.lang.agent;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import i2f.core.container.collection.Collections;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/4/3 11:13
 * @desc
 */
public class AgentUtil {
    public static List<Class> getLoadedClasses(Instrumentation inst){
        Class[] classes=inst.getAllLoadedClasses();
        return Collections.arrayList(classes);
    }
    public static VirtualMachine agentByPid(String pid,String agentJarPath,String arg) throws Exception {
        VirtualMachine vm = VirtualMachine.attach(pid);
        vm.loadAgent(agentJarPath,arg);
        vm.detach();
        return vm;
    }
    public static List<VirtualMachine> agentByName(String name,String agentJarPath,String arg) throws Exception {
        List<VirtualMachine> ret=new ArrayList<>();
        List<VirtualMachineDescriptor> vmds=VirtualMachine.list();
        for(VirtualMachineDescriptor item : vmds){
            if(item.displayName().contains(name)){
                VirtualMachine vm=agentByPid(item.id(),agentJarPath,arg);
                ret.add(vm);
            }
        }
        return ret;
    }
}
