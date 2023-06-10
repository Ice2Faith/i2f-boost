package i2f.liteflow.data.vo;


import i2f.liteflow.data.dom.LiteFlowParamsDo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Ice2Faith
 * @date 2023-06-07 14:27:04
 * @desc lite_flow_params 流程参数表
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class LiteFlowParamsVo extends LiteFlowParamsDo {


    public LiteFlowParamsDo parent() {
        return (LiteFlowParamsDo) this;
    }


}