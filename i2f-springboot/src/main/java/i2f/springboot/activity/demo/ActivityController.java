package i2f.springboot.activity.demo;

import i2f.core.api.ApiResp;
import i2f.springboot.activity.ActivityManager;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/5/3 16:01
 * @desc 流程引擎测试启动控制类
 */
//@RestController
@RequestMapping("activity")
public class ActivityController {

    public static final String key = "request";

    @Autowired
    private ActivityManager activityManager;

    @RequestMapping("deploy")
    public ApiResp deploy() {
        Deployment deployment = activityManager.deployByClasspathBpmn("请假流程", "activity/request.bpmn20.xml", "activity/request.bpmn20.png");
        Map<String,Object> map=new HashMap<>();
        map.put("id",deployment.getId());
        map.put("name",deployment.getName());
        map.put("deploymentTime",deployment.getDeploymentTime());
        return ApiResp.success(map);
    }

    /**
     * 开始流程时，允许设置流程变量params,用来设置assignee的值
     * 这个在request.bpmn20.xml文件中会被使用到
     * @param params
     * @return
     */
    @RequestMapping("start")
    public ApiResp start(@RequestBody Map<String, Object> params) {
        ProcessInstance instance = activityManager.startByKey(key, params);
        Map<String,Object> map=new HashMap<>();
        map.put("id",instance.getId());
        map.put("processDefinitionId",instance.getProcessDefinitionId());
        map.put("processDefinitionKey",instance.getProcessDefinitionKey());
        map.put("processDefinitionName",instance.getName());
        map.put("processDefinitionVersion",instance.getProcessDefinitionVersion());
        map.put("businessKey",instance.getBusinessKey());
        map.put("startTime",instance.getStartTime());
        return ApiResp.success(map);
    }

    @RequestMapping("task/list")
    public ApiResp taskList(String who) {
        List<Task> list = activityManager.queryTaskByKeyAndAssignee(key, who);
        List<Map<String, Object>> ret=new ArrayList<>();

        for(Task item : list){
            Map<String, Object> map=new HashMap<>();
            map.put("id",item.getId());
            map.put("name",item.getName());
            map.put("assignee",item.getAssignee());
            map.put("executionId",item.getExecutionId());
            map.put("processInstanceId",item.getProcessInstanceId());
            map.put("processDefinitionId",item.getProcessDefinitionId());
            map.put("taskDefinitionKey",item.getTaskDefinitionKey());
            map.put("createTime",item.getCreateTime());

            ret.add(map);
        }

        return ApiResp.success(ret);
    }

    /**
     * 同时，在完成一个任务的时候，也允许设置流程变量，用于后续流程的使用
     * 因此，这里就可以实现动态变更后续负责人
     * 注意，是后续流程，也就是说，流程变量应该在未被使用之前可以被更新，流程使用之后，更新也无效
     * @param taskId
     * @param params
     * @return
     */
    @RequestMapping("task/complete/{taskId}")
    public ApiResp taskComplete(@PathVariable("taskId") String taskId,@RequestBody Map<String, Object> params) {
        activityManager.completeTask(taskId, params);
        return ApiResp.success("ok");
    }

    @RequestMapping("instance/list")
    public ApiResp instanceList() {
        List<ProcessInstance> list = activityManager.queryInstanceByKey(key);

        List<Map<String, Object>> ret=new ArrayList<>();

        for(ProcessInstance item : list){
            Map<String, Object> map=new HashMap<>();

            map.put("id",item.getId());
            map.put("processDefinitionId",item.getProcessDefinitionId());
            map.put("processDefinitionKey",item.getProcessDefinitionKey());
            map.put("processDefinitionName",item.getName());
            map.put("processDefinitionVersion",item.getProcessDefinitionVersion());
            map.put("businessKey",item.getBusinessKey());
            map.put("startTime",item.getStartTime());

            ret.add(map);
        }

        return ApiResp.success(ret);
    }

    @RequestMapping("instance/history")
    public ApiResp instanceHistory(String instanceId) {
        List<HistoricActivityInstance> list = activityManager.queryHistories(instanceId);
        List<Map<String, Object>> ret=new ArrayList<>();

        for(HistoricActivityInstance item : list){
            Map<String, Object> map=new HashMap<>();
            map.put("activityId",item.getActivityId());
            map.put("activityName",item.getActivityName());
            map.put("activityType",item.getActivityType());
            map.put("executionId",item.getExecutionId());
            map.put("assignee",item.getAssignee());
            map.put("taskId",item.getTaskId());
            map.put("processInstanceId",item.getProcessInstanceId());
            map.put("processDefinitionId",item.getProcessDefinitionId());
            map.put("startTime",item.getStartTime());
            map.put("endTime",item.getEndTime());
            map.put("id",item.getId());

            ret.add(map);
        }

        return ApiResp.success(ret);
    }

}
