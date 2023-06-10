package i2f.liteflow.mapper;

import i2f.liteflow.data.dom.LiteFlowInstanceDo;
import i2f.liteflow.data.vo.LiteFlowInstanceVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023-06-07 14:24:49
 * @desc lite_flow_instance 流程实例表
 */
@Mapper
public interface LiteFlowInstanceMapper {
    List<LiteFlowInstanceVo> list(@Param("post") LiteFlowInstanceVo post);

    LiteFlowInstanceVo findByPk(@Param("id") Long id);

    <T extends LiteFlowInstanceDo> int insertSelective(@Param("post") T post);

    <T extends LiteFlowInstanceDo> int updateSelectiveByPk(@Param("post") T post);

    int deleteByPk(@Param("id") Long id);

    <T extends LiteFlowInstanceDo> int insert(@Param("post") T post);

    <T extends LiteFlowInstanceDo> int updateByPk(@Param("post") T post);

    <T extends LiteFlowInstanceDo> int deleteSelective(@Param("post") T post);

    int insertBatch(@Param("list") Collection<? extends LiteFlowInstanceDo> list);
}