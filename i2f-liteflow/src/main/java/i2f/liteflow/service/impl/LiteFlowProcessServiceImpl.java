package i2f.liteflow.service.impl;

import i2f.liteflow.consts.LiteFlowErrorCode;
import i2f.liteflow.consts.LiteFlowHandleType;
import i2f.liteflow.consts.LiteFlowNodeType;
import i2f.liteflow.data.vo.LiteFlowDagVo;
import i2f.liteflow.data.vo.LiteFlowNodeVo;
import i2f.liteflow.data.vo.LiteFlowProcessVo;
import i2f.liteflow.exception.LiteFlowException;
import i2f.liteflow.mapper.LiteFlowDagMapper;
import i2f.liteflow.mapper.LiteFlowNodeMapper;
import i2f.liteflow.mapper.LiteFlowProcessMapper;
import i2f.liteflow.service.LiteFlowProcessService;
import i2f.liteflow.util.Io;
import i2f.liteflow.util.Json;
import i2f.liteflow.util.Uid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2023/6/7 11:44
 * @desc
 */
@Slf4j
@Service
public class LiteFlowProcessServiceImpl implements LiteFlowProcessService {
    @Resource
    private LiteFlowProcessMapper processMapper;

    @Resource
    private LiteFlowNodeMapper nodeMapper;

    @Resource
    private LiteFlowDagMapper dagMapper;

    @Transactional(isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    @Override
    public LiteFlowProcessVo deploy(LiteFlowProcessVo webVo,String oper) {
        if(StringUtils.isEmpty(oper)){
            throw new LiteFlowException(LiteFlowErrorCode.MISS_OPER,"缺少操作人");
        }
        if (StringUtils.isEmpty(webVo.getPkey())) {
            throw new LiteFlowException(LiteFlowErrorCode.MISS_PROCESS_PKEY, "缺少流程定义键");
        }
        if(StringUtils.isEmpty(webVo.getName())){
            throw new LiteFlowException(LiteFlowErrorCode.MISS_PROCESS_NAME,"缺少流程定义名称");
        }
        webVo.setActive(1);
        webVo.setId(Uid.getId());

        List<LiteFlowNodeVo> nodes = webVo.getNodes();
        if (nodes == null || !nodes.iterator().hasNext()) {
            throw new LiteFlowException(LiteFlowErrorCode.MISS_NODE, "缺少流程定义节点");
        }
        LiteFlowNodeVo beginNode=null;
        LiteFlowNodeVo endNode=null;
        Set<String> nodeKeys = new HashSet<>();
        for (LiteFlowNodeVo item : nodes) {
            if (StringUtils.isEmpty(item.getPkey())) {
                throw new LiteFlowException(LiteFlowErrorCode.MISS_NODE_PKEY, "缺少流程定义节点键");
            }
            if (item.getNodeType() == null) {
                throw new LiteFlowException(LiteFlowErrorCode.MISS_NODE_TYPE, "缺少流程定义节点类型");
            }
            if (item.getHandleType() == null) {
                throw new LiteFlowException(LiteFlowErrorCode.MISS_NODE_HANDLE_TYPE, "缺少流程定义节点处理类型");
            }
            if (StringUtils.isEmpty(item.getHandleBy())) {
                throw new LiteFlowException(LiteFlowErrorCode.MISS_NODE_HANDLE_BY, "缺少流程定义节点处理主体");
            }
            if (!LiteFlowNodeType.isCode(item.getNodeType())) {
                throw new LiteFlowException(LiteFlowErrorCode.BAD_NODE_TYPE, "错误的流程定义节点类型");
            }
            if (!LiteFlowHandleType.isCode(item.getHandleType())) {
                throw new LiteFlowException(LiteFlowErrorCode.BAD_NODE_HANDLE_TYPE, "错误的流程定义节点处理类型");
            }
            if(item.getNodeType()==LiteFlowNodeType.BEGIN.code()){
                if(beginNode!=null){
                    throw new LiteFlowException(LiteFlowErrorCode.BAD_NODE_TYPE, "一个流程中只允许出现一个开始节点");
                }
                beginNode=item;
            }
            if(item.getNodeType()==LiteFlowNodeType.END.code()){
                if(endNode!=null){
                    throw new LiteFlowException(LiteFlowErrorCode.BAD_NODE_TYPE, "一个流程中只允许出现一个结束节点");
                }
                endNode=item;
            }
            item.setProcessId(webVo.getId());
            nodeKeys.add(item.getPkey());
        }
        if(beginNode==null){
            throw new LiteFlowException(LiteFlowErrorCode.MISS_NODE, "流程缺少开始节点");
        }
        if(endNode==null){
            throw new LiteFlowException(LiteFlowErrorCode.MISS_NODE, "流程缺少结束节点");
        }
        List<LiteFlowDagVo> dags = webVo.getDags();
        if (dags == null || !dags.iterator().hasNext()) {
            throw new LiteFlowException(LiteFlowErrorCode.MISS_DAG, "缺少流程定义有向图");
        }
        for (LiteFlowDagVo item : dags) {
            if (StringUtils.isEmpty(item.getBeginNodePkey())) {
                throw new LiteFlowException(LiteFlowErrorCode.MISS_DAG_BEGIN_PKEY, "缺少流程定义有向图开始节点键");
            }
            if (StringUtils.isEmpty(item.getEndNodePkey())) {
                throw new LiteFlowException(LiteFlowErrorCode.MISS_DAG_END_PKEY, "缺少流程定义有向图结束节点键");
            }
            if (!nodeKeys.contains(item.getBeginNodePkey())) {
                throw new LiteFlowException(LiteFlowErrorCode.BAD_DAG_BEGIN_PKEY, "错误的流程定义有向图开始键");
            }
            if (!nodeKeys.contains(item.getEndNodePkey())) {
                throw new LiteFlowException(LiteFlowErrorCode.BAD_DAG_END_PKEY, "错误的流程定义有向图结束键");
            }
            item.setProcessId(webVo.getId());
            item.setId(Uid.getId());
        }

        webVo.setCreateTime(new Date());
        webVo.setCreateBy(oper);
        processMapper.updateAsDeactiveByPKey(webVo.getPkey());
        Integer version=processMapper.getMaxVersion(webVo.getPkey());
        if(version==null){
            version=0;
        }
        webVo.setVersion(version);
        processMapper.insert(webVo);
        nodeMapper.insertBatch(webVo.getNodes());
        dagMapper.insertBatch(webVo.getDags());
        return webVo;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    @Override
    public LiteFlowProcessVo deployByJson(String json,String oper) {
        LiteFlowProcessVo vo = Json.parseJson(json, LiteFlowProcessVo.class);
        return deploy(vo,oper);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    @Override
    public LiteFlowProcessVo deployByJson(File file,String oper) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        return deployByJson(fis,oper);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    @Override
    public LiteFlowProcessVo deployByJson(InputStream is,String oper) throws IOException {
        String json = Io.readString(is, "UTF-8");
        return deployByJson(json,oper);
    }

    @Override
    public LiteFlowProcessVo process(Long processId) {
        LiteFlowProcessVo ret = processMapper.findByPk(processId);
        List<LiteFlowNodeVo> nodes = nodeMapper.findByProcessId(processId);
        ret.setNodes(nodes);
        List<LiteFlowDagVo> dags = dagMapper.findByProcessId(processId);
        ret.setDags(dags);
        return ret;
    }

    @Override
    public String exportAsJson(Long processId) {
        LiteFlowProcessVo process = process(processId);
        String json = Json.toJson(process, true);
        return json;
    }

    @Override
    public void exportAsJson(Long processId, OutputStream os) throws IOException {
        String json = exportAsJson(processId);
        Io.writeString(json,"UTF-8",os,true);
    }

    @Override
    public void exportAsJson(Long processId, File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        exportAsJson(processId,fos);
    }

    @Override
    public Long processIdByPkey(String pkey) {
        return processMapper.processIdByPkey(pkey);
    }

    @Override
    public LiteFlowProcessVo processByPkey(String pkey) {
        Long processId=processMapper.processIdByPkey(pkey);
        return process(processId);
    }

    @Override
    public String exportAsJsonByPkey(String pkey) {
        Long processId=processMapper.processIdByPkey(pkey);
        return exportAsJson(processId);
    }

    @Override
    public void exportAsJsonByPkey(String pkey, OutputStream os) throws IOException {
        Long processId=processMapper.processIdByPkey(pkey);
        exportAsJson(processId,os);
    }

    @Override
    public void exportAsJsonByPkey(String pkey, File file) throws IOException {
        Long processId=processMapper.processIdByPkey(pkey);
        exportAsJson(processId,file);
    }

    @Override
    public void dropHistoryVersions(String pkey) {
        nodeMapper.deleteHistoryVersions(pkey);
        dagMapper.deleteHistoryVersions(pkey);
        processMapper.deleteHistoryVersions(pkey);
    }
}
