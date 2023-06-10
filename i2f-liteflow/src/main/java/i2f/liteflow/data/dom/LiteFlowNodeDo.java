package i2f.liteflow.data.dom;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

/**
 * @author Ice2Faith
 * @date 2023-06-07 14:23:03
 * @desc lite_flow_node 流程节点表
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class LiteFlowNodeDo {


    /**
     * 节点键
     */
    protected String pkey;

    /**
     * 流程ID
     */
    protected Long processId;

    /**
     * 节点名称
     */
    protected String name;

    /**
     * 节点类型：0 开始，1 结束，2 常规，3 并行开始，4 并行结束
     */
    protected Integer nodeType;

    /**
     * 处理类型：0 个人，1 组织
     */
    protected Integer handleType;

    /**
     * 处理主体：个人或组织的键
     */
    protected String handleBy;


    public <T extends LiteFlowNodeDo> T convert(T dom) {
        BeanUtils.copyProperties(this, dom);
        return dom;
    }

}