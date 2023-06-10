package i2f.liteflow.data.vo;


import i2f.liteflow.data.dom.LiteFlowProcessDo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023-06-07 14:21:53
 * @desc lite_flow_process 流程表
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class LiteFlowProcessVo extends LiteFlowProcessDo {


    public LiteFlowProcessDo parent() {
        return (LiteFlowProcessDo) this;
    }


    /**
     * 版本 desc
     */
    protected String versionDesc;
    /**
     * 是否激活：0 否，1 是 desc
     */
    protected String activeDesc;

    protected List<LiteFlowNodeVo> nodes;

    protected List<LiteFlowDagVo> dags;


}