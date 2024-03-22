package i2f.workflow.test;

import i2f.workflow.FlowNode;
import i2f.workflow.FlowTask;

import java.security.SecureRandom;

/**
 * @author Ice2Faith
 * @date 2024/3/22 16:36
 * @desc
 */
public class TestTask implements FlowTask {
    protected static SecureRandom random=new SecureRandom();
    @Override
    public boolean trigger(FlowNode node) {
        if(node.getPrev().isEmpty()){
            return true;
        }
        return node.getPrev().size()==node.getDone().size();
    }

    @Override
    public void run(FlowNode node) throws Throwable {

//        Thread.sleep(random.nextInt(300));

        System.out.println(String.format("run(%s:%s)",node.getId(),node.getName()));

        if(random.nextDouble()<0.2){
            throw new IllegalStateException("运行失败");
        }
    }
}
