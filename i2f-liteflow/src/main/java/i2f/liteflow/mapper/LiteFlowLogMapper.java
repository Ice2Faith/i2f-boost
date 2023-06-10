package i2f.liteflow.mapper;

import i2f.liteflow.data.dom.LiteFlowLogDo;
import i2f.liteflow.data.vo.LiteFlowLogVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023-06-07 14:25:56
 * @desc lite_flow_log 流程日志表
 */
@Mapper
public interface LiteFlowLogMapper {

    List<LiteFlowLogVo> list(@Param("post") LiteFlowLogVo post);

    LiteFlowLogVo findByPk(@Param("id") Long id);

    <T extends LiteFlowLogDo> int insertSelective(@Param("post") T post);

    <T extends LiteFlowLogDo> int updateSelectiveByPk(@Param("post") T post);

    int deleteByPk(@Param("id") Long id);

    <T extends LiteFlowLogDo> int insert(@Param("post") T post);

    <T extends LiteFlowLogDo> int updateByPk(@Param("post") T post);

    <T extends LiteFlowLogDo> int deleteSelective(@Param("post") T post);

    int insertBatch(@Param("list") Collection<? extends LiteFlowLogDo> list);

    List<LiteFlowLogVo> getTasks(@Param("oper") String oper,
                                 @Param("organs") Collection<String> organs);

    LiteFlowLogVo getNewestTask(@Param("instanceId")Long instanceId);

    List<LiteFlowLogVo> getInstanceLogs(@Param("instanceId")Long instanceId);

    int getNewestTaskCount(@Param("instanceId")Long instanceId,
                           @Param("nodeKey")String nodeKey);

    int updateAsPrepared(@Param("instanceId")Long instanceId,
                         @Param("nextKey")String nextKey);

    int updateHandleBy(@Param("logId")Long logId,@Param("handleBy")String handleBy);

    int deleteByInstanceId(@Param("instanceId")Long instanceId);
}