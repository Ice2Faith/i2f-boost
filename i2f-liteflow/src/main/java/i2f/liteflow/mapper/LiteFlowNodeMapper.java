package i2f.liteflow.mapper;

import i2f.liteflow.data.dom.LiteFlowNodeDo;
import i2f.liteflow.data.vo.LiteFlowNodeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023-06-07 14:23:03
 * @desc lite_flow_node 流程节点表
 */
@Mapper
public interface LiteFlowNodeMapper {

    List<LiteFlowNodeVo> list(@Param("post") LiteFlowNodeVo post);

    LiteFlowNodeVo findByPk(@Param("pkey") String pkey);

    List<LiteFlowNodeVo> findByProcessId(@Param("processId") Long processId);

    <T extends LiteFlowNodeDo> int insertSelective(@Param("post") T post);

    <T extends LiteFlowNodeDo> int updateSelectiveByPk(@Param("post") T post);

    int deleteByPk(@Param("pkey") String pkey);

    <T extends LiteFlowNodeDo> int insert(@Param("post") T post);

    <T extends LiteFlowNodeDo> int updateByPk(@Param("post") T post);

    <T extends LiteFlowNodeDo> int deleteSelective(@Param("post") T post);

    int insertBatch(@Param("list") Collection<? extends LiteFlowNodeDo> list);

    int deleteHistoryVersions(@Param("pkey")String pkey);
}