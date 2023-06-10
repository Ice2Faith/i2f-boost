package i2f.liteflow.data.vo;


import i2f.liteflow.data.dom.LiteFlowDagDo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Ice2Faith
 * @date 2023-06-07 14:23:55
 * @desc lite_flow_dag 流程有向图表
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class LiteFlowDagVo extends LiteFlowDagDo {


    public LiteFlowDagDo parent() {
        return (LiteFlowDagDo) this;
    }


}