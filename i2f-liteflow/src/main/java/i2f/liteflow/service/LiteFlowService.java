package i2f.liteflow.service;

import i2f.liteflow.data.vo.LiteFlowInstanceVo;
import i2f.liteflow.data.vo.LiteFlowLogVo;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2023/6/5 16:28
 * @desc
 */
public interface LiteFlowService {


    LiteFlowInstanceVo begin(Long processId,
                             String businessKey,
                             LiteFlowLogVo nodeVo,
                             Map<String,Object> params,
                             String oper);

    LiteFlowInstanceVo begin(String processPKey,
                             String businessKey,
                             LiteFlowLogVo nodeVo,
                             Map<String,Object> params,
                             String oper);

    LiteFlowInstanceVo getInstance(Long instanceId);

    Map<String,Object> getParams(Long instanceId);

    void updateParams(long instanceId,Map<String,Object> params);

    void replaceParams(long instanceId,Map<String,Object> params);

    List<LiteFlowLogVo> getTasks(String oper);

    void cancel(Long instanceId, String oper);

    void run(Long instanceId,String oper);

    void stop(Long instanceId,String oper);

    void complete(Long logId,
                  LiteFlowLogVo nodeVo,
                  Map<String,Object> params,
                  String oper);

    void claim(Long logId,String oper);

    void surrender(Long logId,String oper);

    void redirect(Long logId,String newOper,String oper);

    void delete(Long instanceId,String oper);

    void rollback(Long logId,
                  LiteFlowLogVo nodeVo,
                  Map<String,Object> params,
                  String oper);

    List<LiteFlowLogVo> history(Long instanceId);
}
