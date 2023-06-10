package i2f.liteflow.mapper;

import i2f.liteflow.data.dom.LiteFlowDagDo;
import i2f.liteflow.data.vo.LiteFlowDagVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023-06-07 14:23:55
 * @desc lite_flow_dag 流程有向图表
 */
@Mapper
public interface LiteFlowDagMapper {

    List<LiteFlowDagVo> list(@Param("post") LiteFlowDagVo post);

    LiteFlowDagVo findByPk(@Param("id") Long id);

    List<LiteFlowDagVo> findByProcessId(@Param("processId") Long processId);

    <T extends LiteFlowDagDo> int insertSelective(@Param("post") T post);

    <T extends LiteFlowDagDo> int updateSelectiveByPk(@Param("post") T post);

    int deleteByPk(@Param("id") Long id);

    <T extends LiteFlowDagDo> int insert(@Param("post") T post);

    <T extends LiteFlowDagDo> int updateByPk(@Param("post") T post);

    <T extends LiteFlowDagDo> int deleteSelective(@Param("post") T post);

    int insertBatch(@Param("list") Collection<? extends LiteFlowDagDo> list);

    int deleteHistoryVersions(@Param("pkey")String pkey);
}