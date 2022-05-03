package i2f.springboot.activity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * @author ltb
 * @date 2022/5/3 13:43
 * @desc
 * 流程过程：
 * 1.部署
 * 2.启动流程
 * 3.完成流程中的每一个任务
 * 4.流程结束
 *
 * 和一般业务的结合：
 * 1.业务触发启动一个流程
 * 2.流程流转的处理人员查询自己的待办任务
 * 3.处理人员完成自己的流程，流转到下一个流程
 * 4.循环上述2和3步骤，直到流程结束
 */

@ConditionalOnBean(ActivityConfig.class)
@ConditionalOnExpression("${i2f.springboot.config.activity.enable-activity-manager:true}")
@Slf4j
@Data
@NoArgsConstructor
@Component
public class ActivityManager {

    @Autowired
    private ProcessEngine processEngine;

    public Deployment deployByClasspathBpmn(String name,String bpmnFile){
        return deployByClasspathBpmn(name,bpmnFile,null);
    }

    /**
     * 部署流程，将bpmn解析存储到数据库的过程
     * 也就是解析bpmn
     * 需要以bpmn或者bpmn20.xml结尾的资源内容才会去部署流程
     * @param name 流程名称
     * @param bpmnFile classpath下面的bpmn文件路径
     * @param attachFile 对应这个bpmn文件的附件，可以是图片，文档等，一般用来辅助阅读，可以为空
     * @return
     */
    public Deployment deployByClasspathBpmn(String name,String bpmnFile,String attachFile){
        RepositoryService repositoryService= processEngine.getRepositoryService();

        DeploymentBuilder builder=repositoryService.createDeployment();
        builder.name(name);
        builder.addClasspathResource(bpmnFile);
        if(attachFile!=null && !"".equals(attachFile)) {
            builder.addClasspathResource(attachFile);
        }

        Deployment deployment=builder.deploy();

        return deployment;
    }

    public Deployment deployByZip(File zipFile) throws IOException {
        FileInputStream fileInputStream=new FileInputStream(zipFile);
        Deployment deployment=deployByZip(fileInputStream);
        fileInputStream.close();
        return deployment;
    }

    public Deployment deployByClasspathZip(String zipFile){
        InputStream inputStream=Thread.currentThread().getContextClassLoader().getResourceAsStream(zipFile);
        return deployByZip(inputStream);
    }

    public Deployment deployByZip(InputStream inputStream){
        ZipInputStream zipInputStream=new ZipInputStream(inputStream);
        return deployByZip(zipInputStream);
    }

    /**
     * 使用zip方式进行部署，这样可以部署多个
     * @param zipInputStream
     * @return
     */
    public Deployment deployByZip(ZipInputStream zipInputStream){
        RepositoryService repositoryService= processEngine.getRepositoryService();

        DeploymentBuilder builder=repositoryService.createDeployment();
        builder.addZipInputStream(zipInputStream);

        Deployment deployment=builder.deploy();

        return deployment;
    }

    /**
     * 获取部署时的资源文件
     * @param deploymentId 部署ID
     * @return key:文件名 value:输入流
     */
    public Map<String,InputStream> queryDeployResourcesById(String deploymentId){
        RepositoryService repositoryService= processEngine.getRepositoryService();

        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploymentId)
                .singleResult();

        Map<String,InputStream> ret=new HashMap<>();

        String bpmnFileName = definition.getDiagramResourceName();
        String attachFileName = definition.getResourceName();

        if(bpmnFileName!=null && !"".equals(bpmnFileName)){
            InputStream is=repositoryService.getResourceAsStream(deploymentId,bpmnFileName);
            ret.put(bpmnFileName,is);
        }
        if(attachFileName!=null && !"".equals(attachFileName)){
            InputStream is=repositoryService.getResourceAsStream(deploymentId,attachFileName);
            ret.put(attachFileName,is);
        }

        return ret;
    }

    public InputStream queryDeployBpmnResourceById(String deploymentId){
        RepositoryService repositoryService= processEngine.getRepositoryService();

        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploymentId)
                .singleResult();

        String bpmnFileName = definition.getDiagramResourceName();

        InputStream bpmnInputStream=repositoryService.getResourceAsStream(deploymentId,bpmnFileName);

        return bpmnInputStream;
    }

    public InputStream queryDeployAttachResourceById(String deploymentId){
        RepositoryService repositoryService= processEngine.getRepositoryService();

        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploymentId)
                .singleResult();

        String attachFileName = definition.getResourceName();

        InputStream attachInputStream=repositoryService.getResourceAsStream(deploymentId,attachFileName);

        return attachInputStream;
    }

    /**
     * 查询所有名称为指定名称的部署流程信息
     * @param name
     * @return
     */
    public List<Deployment> queryDeploymentsByNameLike(String name){
        RepositoryService repositoryService= processEngine.getRepositoryService();

        List<Deployment> list=repositoryService.createDeploymentQuery()
                .deploymentNameLike(name)
                .list();

        return list;
    }

    /**
     * 查询流程定义的列表，根据指定的key,查询所有的版本定义
     * @param key
     * @return
     */
    public List<ProcessDefinition> queryProcessDefinitionsByKey(String key){
        RepositoryService repositoryService= processEngine.getRepositoryService();

        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(key)
                .orderByProcessDefinitionVersion()
                .desc()
                .list();

        return list;
    }

    public ProcessInstance startByKey(Deployment deployment){
        String key=deployment.getKey();
        return startByKey(key);
    }

    public ProcessInstance startByKey(String key){
        return startByKey(key,null,new HashMap<>());
    }

    public ProcessInstance startByKey(String key,String businessKey){
        return startByKey(key,businessKey,new HashMap<>());
    }

    public ProcessInstance startByKey(String key,Map<String,Object> params){
        return startByKey(key,null,params);
    }

    /**
     * 使用key的方式启动一个流程，也就是在bpmn文件中定义的id
     * @param key 就是bpmn文件中定义个key,也就是id
     * @param businessKey 和自身业务关联的业务key,可以用来管理业务表单信息
     * @param params 启动的流程参数，将会参数到路程的流转中，比如常见的发起人是谁、中间流转处理人是谁等
     * @return
     */
    public ProcessInstance startByKey(String key,String businessKey, Map<String,Object> params){
        RuntimeService runtimeService= processEngine.getRuntimeService();

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key,businessKey,params);

        return processInstance;
    }

    /**
     * 查询某个指定流程下的所有流程实例
     * @param key
     * @return
     */
    public List<ProcessInstance> queryInstanceByKey(String key){
        RuntimeService runtimeService= processEngine.getRuntimeService();

        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(key)
                .orderByProcessDefinitionId()
                .asc()
                .list();

        return list;
    }

    /**
     * 获取指定流程复制人的待办流程列表
     * @param key
     * @param assignee
     * @return
     */
    public List<Task> queryTaskByKeyAndAssignee(String key,String assignee){
        TaskService taskService= processEngine.getTaskService();

        List<Task> list=taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskAssignee(assignee)
                .orderByTaskCreateTime()
                .desc()
                .list();
        return list;
    }

    public void completeTask(Task task){
        String taskId=task.getId();
        completeTask(taskId);
    }
    public void completeTask(String taskId){
        completeTask(taskId,new HashMap<>());
    }

    /**
     * 完成指定的任务
     * @param taskId 要完成的任务ID
     * @param params 完成任务时，可以附加变更流程控制变量，比如可以影响下一流程的处理人，流程的条件变量等
     */
    public void completeTask(String taskId,Map<String,Object> params){
        TaskService taskService= processEngine.getTaskService();

        taskService.complete(taskId,params);
    }

    public List<HistoricActivityInstance> queryHistories(ProcessInstance instance){
        String instanceId=instance.getProcessInstanceId();
        return queryHistories(instanceId);
    }
    /**
     * 查询某个流程实例的历史流程
     * @param instanceId
     * @return
     */
    public List<HistoricActivityInstance> queryHistories(String instanceId){
        HistoryService historyService=processEngine.getHistoryService();

        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId)
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();

        return list;
    }

    public void deleteDeploy(ProcessDefinition processDefinition){
        deleteDeploy(processDefinition,false);
    }
    public void deleteDeploy(ProcessDefinition processDefinition,boolean cascade){
        String deploymentId=processDefinition.getDeploymentId();
        deleteDeploy(deploymentId,cascade);
    }

    public void deleteDeploy(Deployment deployment){
        deleteDeploy(deployment,false);
    }
    public void deleteDeploy(Deployment deployment,boolean cascade){
        String deploymentId=deployment.getId();
        deleteDeploy(deploymentId,cascade);
    }

    public void deleteDeploy(String deploymentId){
        deleteDeploy(deploymentId,false);
    }

    /**
     * 删除流程定义
     * 当有流程未完成时，删除是会失败的
     * 如果需要强制删除，则使用级联删除，即cascade=true
     * @param deploymentId 流程定义ID
     * @param cascade 是否级联删除所有相关信息，指定为true,则会连历史表一起删除
     */
    public void deleteDeploy(String deploymentId, boolean cascade){
        RepositoryService repositoryService= processEngine.getRepositoryService();

        repositoryService.deleteDeployment(deploymentId, cascade);
    }
}
