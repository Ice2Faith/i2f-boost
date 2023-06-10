package i2f.liteflow.test;

import i2f.liteflow.data.vo.LiteFlowInstanceVo;
import i2f.liteflow.data.vo.LiteFlowLogVo;
import i2f.liteflow.service.LiteFlowProcessService;
import i2f.liteflow.service.LiteFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2023/6/7 15:59
 * @desc
 */
@Slf4j
@Component
public class TestLiteFlow implements ApplicationRunner {
    @Autowired
    private LiteFlowProcessService liteFlowProcessService;

    @Autowired
    private LiteFlowService liteFlowService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String json = liteFlowProcessService.exportAsJsonByPkey("leave");
        System.out.println(json);

        liteFlowProcessService.deployByJson(json,"root");

        LiteFlowLogVo beginVo=new LiteFlowLogVo();
        beginVo.setHandleMsg("孩子生病，请假半天");
        beginVo.setParam1("0.5");

        Map<String,Object> params=new HashMap<>();
        params.put("userId","1001");
        params.put("submitter","root");
        LiteFlowInstanceVo ins = liteFlowService.begin("leave", "LV1001", beginVo, params, "root");

        long instanceId=ins.getId();
        Map<String, Object> exParams = liteFlowService.getParams(instanceId);

        LiteFlowInstanceVo testIns = liteFlowService.getInstance(instanceId);

        Map<String,Object> updParams=new HashMap<>();
        updParams.put("username","张三");
        liteFlowService.updateParams(instanceId,updParams);

        Map<String,Object> rpcParams=new HashMap<>();
        rpcParams.put("userId","1002");
        rpcParams.put("username","李四");
        rpcParams.put("role","admin");
        liteFlowService.replaceParams(instanceId, rpcParams);

        List<LiteFlowLogVo> mgt2Tasks = liteFlowService.getTasks("mgt2");
        System.out.println(mgt2Tasks);

        for (LiteFlowLogVo task : mgt2Tasks) {
            LiteFlowLogVo log=new LiteFlowLogVo();
            log.setHandleMsg("不满足要求，重新填写");
            log.setParam2("1");
            liteFlowService.rollback(task.getId(), log,null,"mgt2");
        }

        List<LiteFlowLogVo> rootTasks = liteFlowService.getTasks("root");
        System.out.println(rootTasks);

        for (LiteFlowLogVo task : rootTasks) {
            LiteFlowLogVo log=new LiteFlowLogVo();
            log.setHandleMsg("重写填写了");
            log.setParam2("1");
            liteFlowService.complete(task.getId(), log,null,"root");
        }

        List<LiteFlowLogVo> tasks = liteFlowService.getTasks("mgt2");
        System.out.println(tasks);

        for (LiteFlowLogVo task : tasks) {
            liteFlowService.stop(task.getInstanceId(),"mgt2");
            liteFlowService.run(task.getInstanceId(),"mgt2");
        }

//        for (LiteFlowLogVo task : tasks) {
//            LiteFlowInstanceVo instance = liteFlowService.getInstance(task.getInstanceId());
//            liteFlowService.cancel(instance.getId(),instance.getCreateBy());
//            liteFlowService.stop(task.getInstanceId(),"mgt2");
//        }

        for (LiteFlowLogVo task : tasks) {
            LiteFlowLogVo log=new LiteFlowLogVo();
            log.setHandleMsg("核实无误");
            log.setParam2("1");
            liteFlowService.complete(task.getId(), log,null,"mgt2");
        }


        List<LiteFlowLogVo> mgtTasks = liteFlowService.getTasks("mgt");
        System.out.println(mgtTasks);

        for (LiteFlowLogVo task : mgtTasks) {
            LiteFlowLogVo log = new LiteFlowLogVo();
            log.setHandleMsg("已收到");
            log.setParam2("1");
            liteFlowService.complete(task.getId(), log, null, "mgt");
        }

        List<LiteFlowLogVo> hrTasks = liteFlowService.getTasks("hr2");
        System.out.println(hrTasks);

        for (LiteFlowLogVo task : hrTasks) {
            liteFlowService.claim(task.getId(),"hr1");

            liteFlowService.surrender(task.getId(),"hr1");

            liteFlowService.claim(task.getId(),"hr1");

            liteFlowService.redirect(task.getId(),"hr2","hr1");

            LiteFlowLogVo log = new LiteFlowLogVo();
            log.setHandleMsg("已收到");
            log.setParam2("1");
            liteFlowService.complete(task.getId(), log, null, "hr2");
        }


        List<LiteFlowLogVo> fnTasks = liteFlowService.getTasks("fn");
        System.out.println(fnTasks);

        for (LiteFlowLogVo task : fnTasks) {

            LiteFlowLogVo log = new LiteFlowLogVo();
            log.setHandleMsg("归档");
            log.setParam2("1");
            liteFlowService.complete(task.getId(), log, null, "fn");
        }
    }
}
