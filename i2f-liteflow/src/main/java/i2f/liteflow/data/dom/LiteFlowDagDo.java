package i2f.liteflow.data.dom;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

/**
 * @author Ice2Faith
 * @date 2023-06-07 14:23:55
 * @desc lite_flow_dag 流程有向图表
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class LiteFlowDagDo {


    /**
     * 有向边ID
     */
    protected Long id;

    /**
     * 流程ID
     */
    protected Long processId;

    /**
     * 开始节点ID
     */
    protected String beginNodePkey;

    /**
     * 结束节点ID
     */
    protected String endNodePkey;

    /**
     * 流转条件
     */
    protected String flowCondition;


    public <T extends LiteFlowDagDo> T convert(T dom) {
        BeanUtils.copyProperties(this, dom);
        return dom;
    }

}