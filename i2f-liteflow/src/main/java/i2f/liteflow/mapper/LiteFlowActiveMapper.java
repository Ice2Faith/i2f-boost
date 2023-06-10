package i2f.liteflow.mapper;

import i2f.liteflow.data.dom.LiteFlowActiveDo;
import i2f.liteflow.data.vo.LiteFlowActiveVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023-06-10 16:22:38
 * @desc lite_flow_active 流程激活表
 */
@Mapper
public interface LiteFlowActiveMapper {

    List<LiteFlowActiveVo> list(@Param("post") LiteFlowActiveVo post);

    LiteFlowActiveVo findByPk(@Param("id") Long id);

    <T extends LiteFlowActiveDo> int insertSelective(@Param("post") T post);

    <T extends LiteFlowActiveDo> int updateSelectiveByPk(@Param("post") T post);

    int deleteByPk(@Param("id") Long id);

    <T extends LiteFlowActiveDo> int insert(@Param("post") T post);

    <T extends LiteFlowActiveDo> int updateByPk(@Param("post") T post);

    <T extends LiteFlowActiveDo> int deleteSelective(@Param("post") T post);

    int insertBatch(@Param("list") Collection<? extends LiteFlowActiveDo> list);

    int deleteBeginKeys(@Param("instanceId")Long instanceId,
                        @Param("beginKey")String beginKey);

    List<LiteFlowActiveVo> getEndKeys(@Param("instanceId")Long instanceId,
                                      @Param("nextKey")String nextKey);

    int deleteByInstanceId(@Param("instanceId")Long instanceId);
}