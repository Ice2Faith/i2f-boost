package i2f.liteflow.data.vo;


import i2f.liteflow.data.dom.LiteFlowInstanceDo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2023-06-07 14:24:49
 * @desc lite_flow_instance 流程实例表
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class LiteFlowInstanceVo extends LiteFlowInstanceDo {


    public LiteFlowInstanceDo parent() {
        return (LiteFlowInstanceDo) this;
    }


    /**
     * 实例状态：0 停止，1 运行，2 结束 desc
     */
    protected String statusDesc;

    private Map<String,Object> params;


}