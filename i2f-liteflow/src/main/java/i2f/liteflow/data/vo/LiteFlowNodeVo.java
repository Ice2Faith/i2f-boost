package i2f.liteflow.data.vo;


import i2f.liteflow.data.dom.LiteFlowNodeDo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Ice2Faith
 * @date 2023-06-07 14:23:03
 * @desc lite_flow_node 流程节点表
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class LiteFlowNodeVo extends LiteFlowNodeDo {


    public LiteFlowNodeDo parent() {
        return (LiteFlowNodeDo) this;
    }


    /**
     * 节点类型：0 开始，1 结束，2 常规，3 并行开始，4 并行结束 desc
     */
    protected String nodeTypeDesc;
    /**
     * 处理类型：0 个人，1 组织 desc
     */
    protected String handleTypeDesc;


}