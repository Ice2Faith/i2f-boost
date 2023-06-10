package i2f.liteflow.mapper;

import i2f.liteflow.data.dom.LiteFlowParamsDo;
import i2f.liteflow.data.vo.LiteFlowParamsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023-06-07 14:27:04
 * @desc lite_flow_params 流程参数表
 */
@Mapper
public interface LiteFlowParamsMapper {

    List<LiteFlowParamsVo> list(@Param("post") LiteFlowParamsVo post);

    LiteFlowParamsVo findByPk(@Param("id") Long id);

    <T extends LiteFlowParamsDo> int insertSelective(@Param("post") T post);

    <T extends LiteFlowParamsDo> int updateSelectiveByPk(@Param("post") T post);

    int deleteByPk(@Param("id") Long id);

    <T extends LiteFlowParamsDo> int insert(@Param("post") T post);

    <T extends LiteFlowParamsDo> int updateByPk(@Param("post") T post);

    <T extends LiteFlowParamsDo> int deleteSelective(@Param("post") T post);

    int insertBatch(@Param("list") Collection<? extends LiteFlowParamsDo> list);

    List<LiteFlowParamsVo> getParamsByInstanceId(@Param("instanceId")Long instanceId);

    int deleteByInstanceId(@Param("instanceId")Long instanceId);
}