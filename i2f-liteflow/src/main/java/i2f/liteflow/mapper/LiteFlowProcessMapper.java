package i2f.liteflow.mapper;

import i2f.liteflow.data.dom.LiteFlowProcessDo;
import i2f.liteflow.data.vo.LiteFlowProcessVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023-06-07 14:21:53
 * @desc lite_flow_process 流程表
 */
@Mapper
public interface LiteFlowProcessMapper {

    List<LiteFlowProcessVo> list(@Param("post") LiteFlowProcessVo post);

    LiteFlowProcessVo findByPk(@Param("id") Long id);

    <T extends LiteFlowProcessDo> int insertSelective(@Param("post") T post);

    <T extends LiteFlowProcessDo> int updateSelectiveByPk(@Param("post") T post);

    int deleteByPk(@Param("id") Long id);

    <T extends LiteFlowProcessDo> int insert(@Param("post") T post);

    <T extends LiteFlowProcessDo> int updateByPk(@Param("post") T post);

    <T extends LiteFlowProcessDo> int deleteSelective(@Param("post") T post);

    int insertBatch(@Param("list") Collection<? extends LiteFlowProcessDo> list);

    int updateAsDeactiveByPKey(@Param("pkey")String pkey);

    int countByPKey(@Param("pkey")String pkey);

    Long processIdByPkey(@Param("pkey")String pkey);

    Integer getMaxVersion(@Param("pkey")String pkey);

    int deleteHistoryVersions(@Param("pkey")String pkey);
}