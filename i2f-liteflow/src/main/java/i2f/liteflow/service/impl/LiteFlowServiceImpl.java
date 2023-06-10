package i2f.liteflow.service.impl;

import i2f.liteflow.consts.LiteFlowErrorCode;
import i2f.liteflow.consts.LiteFlowHandleType;
import i2f.liteflow.consts.LiteFlowInstanceStatus;
import i2f.liteflow.consts.LiteFlowNodeType;
import i2f.liteflow.data.dom.LiteFlowActiveDo;
import i2f.liteflow.data.dom.LiteFlowNodeDo;
import i2f.liteflow.data.dto.LiteFlowDagDto;
import i2f.liteflow.data.dto.LiteFlowNodeContext;
import i2f.liteflow.data.vo.*;
import i2f.liteflow.exception.LiteFlowException;
import i2f.liteflow.ext.LiteFlowProvider;
import i2f.liteflow.mapper.LiteFlowActiveMapper;
import i2f.liteflow.mapper.LiteFlowInstanceMapper;
import i2f.liteflow.mapper.LiteFlowLogMapper;
import i2f.liteflow.mapper.LiteFlowParamsMapper;
import i2f.liteflow.service.LiteFlowProcessService;
import i2f.liteflow.service.LiteFlowService;
import i2f.liteflow.spel.SpelEnhancer;
import i2f.liteflow.spel.StandaloneSpelExpressionResolver;
import i2f.liteflow.util.Params;
import i2f.liteflow.util.Uid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2023/6/7 11:11
 * @desc
 */
@Slf4j
@Service
public class LiteFlowServiceImpl implements LiteFlowService {

    @Autowired
    private LiteFlowProcessService processService;

    @Resource
    private LiteFlowInstanceMapper instanceMapper;

    @Resource
    private LiteFlowLogMapper logMapper;

    @Resource
    private LiteFlowParamsMapper paramsMapper;

    @Resource
    private LiteFlowActiveMapper activeMapper;

    @Autowired
    private LiteFlowProvider provider;

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    @Override
    public LiteFlowInstanceVo begin(Long processId, String businessKey, LiteFlowLogVo nodeVo, Map<String, Object> params, String oper) {
        if (StringUtils.isEmpty(oper)) {
            throw new LiteFlowException(LiteFlowErrorCode.MISS_OPER, "缺少操作人");
        }
        LiteFlowProcessVo process = processService.process(processId);
        if (process == null) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_FOUND, "未找到流程");
        }
        LiteFlowDagDto dagDto = LiteFlowDagDto.parse(process);
        Date now = new Date();

        LiteFlowInstanceVo ins = new LiteFlowInstanceVo();
        ins.setId(Uid.getId());
        ins.setProcessId(process.getId());
        ins.setBusinessKey(businessKey);
        ins.setStatus(LiteFlowInstanceStatus.RUN.code());
        ins.setCreateTime(now);
        ins.setCreateBy(oper);
        instanceMapper.insert(ins);

        LiteFlowLogVo beginNode = new LiteFlowLogVo();
        beginNode.setId(Uid.getId());
        beginNode.setInstanceId(ins.getId());
        beginNode.setNodeKey(dagDto.beginKey());
        beginNode.setPrevId(null);
        beginNode.setPrepared(1);
        beginNode.setNewest(0);
        beginNode.setHandleTime(now);
        beginNode.setHandleMsg(nodeVo.getHandleMsg());
        beginNode.setHandleBy(oper);
        beginNode.setParam1(nodeVo.getParam1());
        beginNode.setParam2(nodeVo.getParam2());
        beginNode.setParam3(nodeVo.getParam3());
        beginNode.setParam4(nodeVo.getParam4());
        beginNode.setParam5(nodeVo.getParam5());
        logMapper.insert(beginNode);

        if (params != null) {
            updateParams(ins.getId(), params);
        }

        prepareNexts(beginNode.getId(), nodeVo.getNextHandleBy());

        return ins;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    @Override
    public LiteFlowInstanceVo begin(String processPKey, String businessKey, LiteFlowLogVo nodeVo, Map<String, Object> params, String oper) {
        Long processId = processService.processIdByPkey(processPKey);
        return begin(processId, businessKey, nodeVo, params, oper);
    }

    @Override
    public LiteFlowInstanceVo getInstance(Long instanceId) {
        LiteFlowInstanceVo ret = instanceMapper.findByPk(instanceId);
        if (ret == null) {
            return ret;
        }
        Map<String, Object> params = getParams(instanceId);
        ret.setParams(params);
        return ret;
    }

    @Override
    public Map<String, Object> getParams(Long instanceId) {
        List<LiteFlowParamsVo> olds = paramsMapper.getParamsByInstanceId(instanceId);
        Map<String, Object> oldMap = Params.mapOf(olds);
        return oldMap;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    @Override
    public void updateParams(long instanceId, Map<String, Object> params) {
        Map<String, Object> oldMap = getParams(instanceId);
        Map<String, Object> mergeMap = Params.mergeMap(oldMap, params);
        List<LiteFlowParamsVo> list = Params.listOf(mergeMap);
        for (LiteFlowParamsVo item : list) {
            item.setInstanceId(instanceId);
        }
        paramsMapper.deleteByInstanceId(instanceId);
        if (list.iterator().hasNext()) {
            paramsMapper.insertBatch(list);
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    @Override
    public void replaceParams(long instanceId, Map<String, Object> params) {
        List<LiteFlowParamsVo> list = Params.listOf(params);
        for (LiteFlowParamsVo item : list) {
            item.setInstanceId(instanceId);
        }
        paramsMapper.deleteByInstanceId(instanceId);
        if (list.iterator().hasNext()) {
            paramsMapper.insertBatch(list);
        }
    }

    @Override
    public List<LiteFlowLogVo> getTasks(String oper) {
        if (StringUtils.isEmpty(oper)) {
            throw new LiteFlowException(LiteFlowErrorCode.MISS_OPER, "缺少操作人");
        }
        Set<String> organs = provider.getPersonOrgans(oper);
        List<LiteFlowLogVo> ret = logMapper.getTasks(oper, organs);
        for (LiteFlowLogVo item : ret) {
            LiteFlowInstanceVo instance = getInstance(item.getInstanceId());
            item.setInstance(instance);
        }
        return ret;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    @Override
    public void cancel(Long instanceId, String oper) {
        if (StringUtils.isEmpty(oper)) {
            throw new LiteFlowException(LiteFlowErrorCode.MISS_OPER, "缺少操作人");
        }
        LiteFlowInstanceVo instance = getInstance(instanceId);
        if (instance == null) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_FOUND, "未找到流程实例");
        }
        String createBy = instance.getCreateBy();
        if (!createBy.equals(oper)) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_AUTH, "您不是流程发起人，无权撤销流程");
        }
        LiteFlowInstanceVo updInfo = new LiteFlowInstanceVo();
        updInfo.setId(instanceId);
        updInfo.setStatus(LiteFlowInstanceStatus.FINISH.code());
        instanceMapper.updateSelectiveByPk(updInfo);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    @Override
    public void run(Long instanceId, String oper) {
        if (StringUtils.isEmpty(oper)) {
            throw new LiteFlowException(LiteFlowErrorCode.MISS_OPER, "缺少操作人");
        }
        LiteFlowInstanceVo instance = getInstance(instanceId);
        if (instance == null) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_FOUND, "未找到流程实例");
        }
        if (instance.getStatus() == LiteFlowInstanceStatus.FINISH.code()) {
            throw new LiteFlowException(LiteFlowErrorCode.INS_FINISHED, "流程已经结束，不能再操作");
        }
        LiteFlowLogVo log = logMapper.getNewestTask(instanceId);
        if (log.getPrepared() == 0) {
            throw new LiteFlowException(LiteFlowErrorCode.LOG_NOT_PREPARED, "节点未就绪，无法进行操作");
        }
        String handleBy = log.getHandleBy();
        if (handleBy == null || !handleBy.equals(oper)) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_PERM, "您不是节点操作人，无权运行节点");
        }
        LiteFlowInstanceVo updInfo = new LiteFlowInstanceVo();
        updInfo.setId(instanceId);
        updInfo.setStatus(LiteFlowInstanceStatus.RUN.code());
        instanceMapper.updateSelectiveByPk(updInfo);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    @Override
    public void stop(Long instanceId, String oper) {
        if (StringUtils.isEmpty(oper)) {
            throw new LiteFlowException(LiteFlowErrorCode.MISS_OPER, "缺少操作人");
        }
        LiteFlowInstanceVo instance = getInstance(instanceId);
        if (instance == null) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_FOUND, "未找到流程实例");
        }
        if (instance.getStatus() == LiteFlowInstanceStatus.FINISH.code()) {
            throw new LiteFlowException(LiteFlowErrorCode.INS_FINISHED, "流程已经结束，不能再操作");
        }
        LiteFlowLogVo log = logMapper.getNewestTask(instanceId);
        if (log.getPrepared() == 0) {
            throw new LiteFlowException(LiteFlowErrorCode.LOG_NOT_PREPARED, "节点未就绪，无法进行操作");
        }
        String handleBy = log.getHandleBy();
        if (handleBy == null || !handleBy.equals(oper)) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_PERM, "您不是节点操作人，无权运行节点");
        }
        LiteFlowInstanceVo updInfo = new LiteFlowInstanceVo();
        updInfo.setId(instanceId);
        updInfo.setStatus(LiteFlowInstanceStatus.STOP.code());
        instanceMapper.updateSelectiveByPk(updInfo);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    @Override
    public void complete(Long logId, LiteFlowLogVo nodeVo, Map<String, Object> params, String oper) {
        if (StringUtils.isEmpty(oper)) {
            throw new LiteFlowException(LiteFlowErrorCode.MISS_OPER, "缺少操作人");
        }
        LiteFlowLogVo log = logMapper.findByPk(logId);
        if (log == null) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_FOUND, "未找到流程任务");
        }
        if (log.getNewest() == 0) {
            throw new LiteFlowException(LiteFlowErrorCode.LOG_COMPLETED, "流程任务已经完成，非法操作");
        }
        if (log.getPrepared() == 0) {
            throw new LiteFlowException(LiteFlowErrorCode.LOG_NOT_PREPARED, "节点未就绪，无法进行操作");
        }
        if (!oper.equals(log.getHandleBy())) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_PERM, "您不是节点操作人，无权完成节点");
        }
        LiteFlowInstanceVo instance = getInstance(log.getInstanceId());
        if (instance == null) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_FOUND, "未找到流程实例");
        }
        if (instance.getStatus() == LiteFlowInstanceStatus.FINISH.code()) {
            throw new LiteFlowException(LiteFlowErrorCode.INS_FINISHED, "流程已经结束，不能再操作");
        }
        if (instance.getStatus() == LiteFlowInstanceStatus.STOP.code()) {
            throw new LiteFlowException(LiteFlowErrorCode.INS_STOPPED, "流程已经停止，请运行后再操作");
        }

        Date now = new Date();

        LiteFlowLogVo updInfo = new LiteFlowLogVo();
        updInfo.setId(log.getId());
        updInfo.setNewest(0);
        updInfo.setHandleTime(now);
        updInfo.setHandleMsg(nodeVo.getHandleMsg());
        updInfo.setParam1(nodeVo.getParam1());
        updInfo.setParam2(nodeVo.getParam2());
        updInfo.setParam3(nodeVo.getParam3());
        updInfo.setParam4(nodeVo.getParam4());
        updInfo.setParam5(nodeVo.getParam5());
        logMapper.updateSelectiveByPk(updInfo);

        if (params != null) {
            updateParams(instance.getId(), params);
        }

        prepareNexts(updInfo.getId(), nodeVo.getNextHandleBy());
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    @Override
    public void claim(Long logId, String oper) {
        if (StringUtils.isEmpty(oper)) {
            throw new LiteFlowException(LiteFlowErrorCode.MISS_OPER, "缺少操作人");
        }
        LiteFlowLogVo log = logMapper.findByPk(logId);
        if (log == null) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_FOUND, "未找到流程任务");
        }
        if (log.getNewest() == 0) {
            throw new LiteFlowException(LiteFlowErrorCode.LOG_COMPLETED, "流程任务已经完成，非法操作");
        }
        if (log.getPrepared() == 0) {
            throw new LiteFlowException(LiteFlowErrorCode.LOG_NOT_PREPARED, "节点未就绪，无法进行操作");
        }
        if (!StringUtils.isEmpty(log.getHandleBy())) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_PERM, "您不是节点操作人，节点已被他人拾取");
        }
        LiteFlowInstanceVo instance = getInstance(log.getInstanceId());
        if (instance == null) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_FOUND, "未找到流程实例");
        }
        if (instance.getStatus() == LiteFlowInstanceStatus.FINISH.code()) {
            throw new LiteFlowException(LiteFlowErrorCode.INS_FINISHED, "流程已经结束，不能再操作");
        }
        if (instance.getStatus() == LiteFlowInstanceStatus.STOP.code()) {
            throw new LiteFlowException(LiteFlowErrorCode.INS_STOPPED, "流程已经停止，请运行后再操作");
        }
        LiteFlowProcessVo process = processService.process(instance.getProcessId());
        if (process == null) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_FOUND, "未找到流程");
        }
        LiteFlowDagDto dagDto = LiteFlowDagDto.parse(process);

        String currKey = log.getNodeKey();
        LiteFlowNodeDo node = dagDto.getNodeMap().get(currKey);
        if (node.getHandleType() != LiteFlowHandleType.ORGAN.code()) {
            throw new LiteFlowException(LiteFlowErrorCode.BAD_NODE_HANDLE_TYPE, "节点不是组织类型，不能进行拾取");
        }

        Set<String> organs = provider.getPersonOrgans(oper);
        if (!organs.contains(node.getHandleBy())) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_AUTH, "无权拾取，您不是该流程的处理组织成员");
        }

        logMapper.updateHandleBy(log.getId(), oper);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    @Override
    public void surrender(Long logId, String oper) {
        if (StringUtils.isEmpty(oper)) {
            throw new LiteFlowException(LiteFlowErrorCode.MISS_OPER, "缺少操作人");
        }
        LiteFlowLogVo log = logMapper.findByPk(logId);
        if (log == null) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_FOUND, "未找到流程任务");
        }
        if (log.getNewest() == 0) {
            throw new LiteFlowException(LiteFlowErrorCode.LOG_COMPLETED, "流程任务已经完成，非法操作");
        }
        if (log.getPrepared() == 0) {
            throw new LiteFlowException(LiteFlowErrorCode.LOG_NOT_PREPARED, "节点未就绪，无法进行操作");
        }
        if (!oper.equals(log.getHandleBy())) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_PERM, "您不是节点操作人，无需归还");
        }
        LiteFlowInstanceVo instance = getInstance(log.getInstanceId());
        if (instance == null) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_FOUND, "未找到流程实例");
        }
        if (instance.getStatus() == LiteFlowInstanceStatus.FINISH.code()) {
            throw new LiteFlowException(LiteFlowErrorCode.INS_FINISHED, "流程已经结束，不能再操作");
        }
        if (instance.getStatus() == LiteFlowInstanceStatus.STOP.code()) {
            throw new LiteFlowException(LiteFlowErrorCode.INS_STOPPED, "流程已经停止，请运行后再操作");
        }
        LiteFlowProcessVo process = processService.process(instance.getProcessId());
        if (process == null) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_FOUND, "未找到流程");
        }
        LiteFlowDagDto dagDto = LiteFlowDagDto.parse(process);

        String currKey = log.getNodeKey();
        LiteFlowNodeDo node = dagDto.getNodeMap().get(currKey);
        if (node.getHandleType() != LiteFlowHandleType.ORGAN.code()) {
            throw new LiteFlowException(LiteFlowErrorCode.BAD_NODE_HANDLE_TYPE, "节点不是组织类型，不能进行返还");
        }

        logMapper.updateHandleBy(log.getId(), null);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    @Override
    public void redirect(Long logId, String newOper, String oper) {
        if (StringUtils.isEmpty(oper)) {
            throw new LiteFlowException(LiteFlowErrorCode.MISS_OPER, "缺少操作人");
        }
        LiteFlowLogVo log = logMapper.findByPk(logId);
        if (log == null) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_FOUND, "未找到流程任务");
        }
        if (log.getNewest() == 0) {
            throw new LiteFlowException(LiteFlowErrorCode.LOG_COMPLETED, "流程任务已经完成，非法操作");
        }
        if (log.getPrepared() == 0) {
            throw new LiteFlowException(LiteFlowErrorCode.LOG_NOT_PREPARED, "节点未就绪，无法进行操作");
        }
        if (!oper.equals(log.getHandleBy())) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_PERM, "您不是节点操作人，不能重新指派");
        }
        LiteFlowInstanceVo instance = getInstance(log.getInstanceId());
        if (instance == null) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_FOUND, "未找到流程实例");
        }
        if (instance.getStatus() == LiteFlowInstanceStatus.FINISH.code()) {
            throw new LiteFlowException(LiteFlowErrorCode.INS_FINISHED, "流程已经结束，不能再操作");
        }
        if (instance.getStatus() == LiteFlowInstanceStatus.STOP.code()) {
            throw new LiteFlowException(LiteFlowErrorCode.INS_STOPPED, "流程已经停止，请运行后再操作");
        }
        LiteFlowProcessVo process = processService.process(instance.getProcessId());
        if (process == null) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_FOUND, "未找到流程");
        }
        LiteFlowDagDto dagDto = LiteFlowDagDto.parse(process);

        String currKey = log.getNodeKey();
        LiteFlowNodeDo node = dagDto.getNodeMap().get(currKey);
        if (node.getHandleType() == LiteFlowHandleType.ORGAN.code()) {
            if (newOper != null) {
                Set<String> organs = provider.getPersonOrgans(oper);
                if (!organs.contains(node.getHandleBy())) {
                    throw new LiteFlowException(LiteFlowErrorCode.NOT_AUTH, "无权指派，目标人员不是该组织成员");
                }
            }
            logMapper.updateHandleBy(log.getId(), newOper);
        } else {
            if (StringUtils.isEmpty(newOper)) {
                throw new LiteFlowException(LiteFlowErrorCode.MISS_OPER, "缺少目标人员");
            }
            logMapper.updateHandleBy(log.getId(), newOper);
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    @Override
    public void delete(Long instanceId, String oper) {
        if (StringUtils.isEmpty(oper)) {
            throw new LiteFlowException(LiteFlowErrorCode.MISS_OPER, "缺少操作人");
        }
        LiteFlowInstanceVo instance = getInstance(instanceId);
        if (instance == null) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_FOUND, "未找到流程实例");
        }
        if(!oper.equals(instance.getCreateBy())){
            throw new LiteFlowException(LiteFlowErrorCode.NOT_AUTH, "您不是流程发起人，不能删除流程");
        }
        if (instance.getStatus() != LiteFlowInstanceStatus.FINISH.code()) {
            throw new LiteFlowException(LiteFlowErrorCode.INS_FINISHED, "流程未结束，不能删除");
        }
        instanceMapper.deleteByPk(instanceId);
        logMapper.deleteByInstanceId(instanceId);
        paramsMapper.deleteByInstanceId(instanceId);
        activeMapper.deleteByInstanceId(instanceId);
    }

    @Override
    public void rollback(Long logId, LiteFlowLogVo nodeVo, Map<String, Object> params, String oper) {
        if (StringUtils.isEmpty(oper)) {
            throw new LiteFlowException(LiteFlowErrorCode.MISS_OPER, "缺少操作人");
        }
        LiteFlowLogVo log = logMapper.findByPk(logId);
        if (log == null) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_FOUND, "未找到流程任务");
        }
        if (log.getNewest() == 0) {
            throw new LiteFlowException(LiteFlowErrorCode.LOG_COMPLETED, "流程任务已经完成，非法操作");
        }
        if (log.getPrepared() == 0) {
            throw new LiteFlowException(LiteFlowErrorCode.LOG_NOT_PREPARED, "节点未就绪，无法进行操作");
        }
        if (!oper.equals(log.getHandleBy())) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_PERM, "您不是节点操作人，无权完成节点");
        }
        LiteFlowInstanceVo instance = getInstance(log.getInstanceId());
        if (instance == null) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_FOUND, "未找到流程实例");
        }
        if (instance.getStatus() == LiteFlowInstanceStatus.FINISH.code()) {
            throw new LiteFlowException(LiteFlowErrorCode.INS_FINISHED, "流程已经结束，不能再操作");
        }
        if (instance.getStatus() == LiteFlowInstanceStatus.STOP.code()) {
            throw new LiteFlowException(LiteFlowErrorCode.INS_STOPPED, "流程已经停止，请运行后再操作");
        }
        LiteFlowProcessVo process = processService.process(instance.getProcessId());
        if (process == null) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_FOUND, "未找到流程");
        }
        LiteFlowDagDto dagDto = LiteFlowDagDto.parse(process);
        LiteFlowNodeDo node = dagDto.getNodeMap().get(log.getNodeKey());
        if(node.getNodeType()==LiteFlowNodeType.BEGIN.code()){
            throw new LiteFlowException(LiteFlowErrorCode.NOT_FOUND, "已经是流程开始，不能再进行回退");
        }

        Date now = new Date();

        LiteFlowLogVo updInfo = new LiteFlowLogVo();
        updInfo.setId(log.getId());
        updInfo.setNewest(0);
        updInfo.setHandleTime(now);
        updInfo.setHandleMsg(nodeVo.getHandleMsg());
        updInfo.setParam1(nodeVo.getParam1());
        updInfo.setParam2(nodeVo.getParam2());
        updInfo.setParam3(nodeVo.getParam3());
        updInfo.setParam4(nodeVo.getParam4());
        updInfo.setParam5(nodeVo.getParam5());
        logMapper.updateSelectiveByPk(updInfo);

        List<LiteFlowLogVo> logs = logMapper.getInstanceLogs(instance.getId());
        Map<String,LiteFlowLogVo> newestMap=new HashMap<>();
        for (LiteFlowLogVo item : logs) {
            String nodeKey = item.getNodeKey();
            if(!newestMap.containsKey(nodeKey)){
                newestMap.put(nodeKey,item);
            }
        }

        List<LiteFlowActiveVo> activeDags = activeMapper.getEndKeys(instance.getId(), log.getNodeKey());
        List<LiteFlowLogVo> finalList=new LinkedList<>();
        for (LiteFlowActiveVo item : activeDags) {
            String pkey = item.getBeginNodePkey();
            LiteFlowLogVo logVo = newestMap.get(pkey);
            logVo.setId(Uid.getId());
            logVo.setHandleTime(null);
            logVo.setHandleMsg(null);
            logVo.setPrevId(logId);
            logVo.setNewest(1);
            logVo.setParam1(null);
            logVo.setParam2(null);
            logVo.setParam3(null);
            logVo.setParam4(null);
            logVo.setParam5(null);
            finalList.add(logVo);
        }
        if(finalList.iterator().hasNext()){
            logMapper.insertBatch(finalList);
        }
    }

    @Override
    public List<LiteFlowLogVo> history(Long instanceId) {
        return logMapper.getInstanceLogs(instanceId);
    }

    public void prepareNexts(Long logId, String nextHandleBy) {
        LiteFlowLogVo log = logMapper.findByPk(logId);
        if (log == null) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_FOUND, "未找到流程任务");
        }
        LiteFlowInstanceVo instance = getInstance(log.getInstanceId());
        if (instance == null) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_FOUND, "未找到流程实例");
        }
        LiteFlowProcessVo process = processService.process(instance.getProcessId());
        if (process == null) {
            throw new LiteFlowException(LiteFlowErrorCode.NOT_FOUND, "未找到流程");
        }
        LiteFlowDagDto dagDto = LiteFlowDagDto.parse(process);
        Date now = new Date();

        String currKey = log.getNodeKey();
        LiteFlowNodeDo node = dagDto.getNodeMap().get(currKey);
        Integer nodeType = node.getNodeType();
        if (nodeType == LiteFlowNodeType.END.code()) {
            LiteFlowInstanceVo insUpdInfo=new LiteFlowInstanceVo();
            insUpdInfo.setId(log.getInstanceId());
            insUpdInfo.setStatus(LiteFlowInstanceStatus.FINISH.code());
            instanceMapper.updateSelectiveByPk(insUpdInfo);
            return;
        }

        // 激发后置节点
        Set<String> nextKeys = dagDto.getOutMap().get(currKey);

        LiteFlowNodeContext context = new LiteFlowNodeContext();
        context.setDag(dagDto);
        context.setEnhancer(new SpelEnhancer());
        context.setInstance(instance);
        context.setLog(log);
        context.setParams(instance.getParams());

        List<LiteFlowLogVo> nexts = new ArrayList<>();
        for (String key : nextKeys) {
            LiteFlowDagVo dag = dagDto.getDag(currKey, key);
            String expression = dag.getFlowCondition();
            boolean ok = true;
            if (!StringUtils.isEmpty(expression)) {
                ok = StandaloneSpelExpressionResolver.getBool(expression, context);
            }
            if (!ok) {
                continue;
            }

            LiteFlowLogVo item = new LiteFlowLogVo();
            item.setId(Uid.getId());
            item.setInstanceId(log.getInstanceId());
            item.setNodeKey(key);
            item.setPrevId(logId);
            item.setPrepared(0);
            item.setNewest(1);
            item.setHandleTime(null);
            item.setHandleMsg(null);
            item.setHandleBy(null);
            LiteFlowNodeDo nextNode = dagDto.getNodeMap().get(key);
            if (nextNode.getHandleType() == LiteFlowHandleType.PERSON.code()) {
                if (!StringUtils.isEmpty(nextHandleBy)) {
                    item.setHandleBy(nextHandleBy);
                } else {
                    item.setHandleBy(nextNode.getHandleBy());
                }
            } else {
                if (!StringUtils.isEmpty(nextHandleBy)) {
                    Set<String> organs = provider.getPersonOrgans(nextHandleBy);
                    if (organs.contains(nextNode.getHandleBy())) {
                        item.setHandleBy(nextHandleBy);
                    }
                }
            }

            nexts.add(item);

            if(node.getNodeType()!=LiteFlowNodeType.PARALLEL_BEGIN.code()){
                break;
            }
        }
        List<LiteFlowLogVo> finalNexts = new LinkedList<>();
        List<LiteFlowLogVo> instanceLogs = logMapper.getInstanceLogs(instance.getId());
        Set<String> logKeys = new HashSet<>();
        for (LiteFlowLogVo item : instanceLogs) {
            String nodeKey = item.getNodeKey();
            if(item.getPrepared()==0) {
                logKeys.add(nodeKey);
            }
        }
        Set<String> nextActives = new HashSet<>();
        List<LiteFlowActiveDo> actives = new ArrayList<>();
        for (LiteFlowLogVo item : nexts) {
            String nodeKey = item.getNodeKey();
            if (!logKeys.contains(nodeKey)) {
                finalNexts.add(item);
            }
            nextActives.add(nodeKey);
            actives.add(new LiteFlowActiveVo()
                    .setId(Uid.getId())
                    .setInstanceId(instance.getId())
                    .setBeginNodePkey(currKey)
                    .setEndNodePkey(nodeKey)
            );
        }
        if(finalNexts.iterator().hasNext()) {
            logMapper.insertBatch(finalNexts);
        }
        activeMapper.deleteBeginKeys(instance.getId(), currKey);
        if(actives.iterator().hasNext()) {
            activeMapper.insertBatch(actives);
        }


        // 当前节点完成，可能导致原来未就绪的后置节点就绪
        for (String nextKey : nextActives) {
            LiteFlowNodeDo nextNode = dagDto.getNodeMap().get(currKey);
            List<LiteFlowActiveVo> activeDags = activeMapper.getEndKeys(instance.getId(), nextKey);
            boolean finished = true;
            if(nextNode.getNodeType()==LiteFlowNodeType.PARALLEL_END.code()){
                finished=false;
            }
            for (LiteFlowActiveVo activeDag : activeDags) {
                int cnt = logMapper.getNewestTaskCount(instance.getId(), activeDag.getBeginNodePkey());
                if (cnt > 0) {
                    if(nextNode.getNodeType()==LiteFlowNodeType.PARALLEL_END.code()){
                        finished=true;
                        break;
                    }
                    finished = false;
                    break;
                }
            }

            if (finished) {
                logMapper.updateAsPrepared(instance.getId(), nextKey);
            }
        }
    }
}
