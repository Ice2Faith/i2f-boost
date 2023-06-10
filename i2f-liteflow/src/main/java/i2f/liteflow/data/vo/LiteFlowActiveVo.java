package i2f.liteflow.data.vo;


import i2f.liteflow.data.dom.LiteFlowActiveDo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Ice2Faith
 * @date 2023-06-10 16:22:38
 * @desc lite_flow_active 流程激活表
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class LiteFlowActiveVo extends LiteFlowActiveDo {


    public LiteFlowActiveDo parent() {
        return (LiteFlowActiveDo) this;
    }


}