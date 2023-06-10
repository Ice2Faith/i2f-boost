package i2f.liteflow.data.dom;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

/**
 * @author Ice2Faith
 * @date 2023-06-10 16:22:38
 * @desc lite_flow_active 流程激活表
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class LiteFlowActiveDo {


    /**
     * 有向边ID
     */
    protected Long id;

    /**
     * 实例ID
     */
    protected Long instanceId;

    /**
     * 开始节点ID
     */
    protected String beginNodePkey;

    /**
     * 结束节点ID
     */
    protected String endNodePkey;


    public <T extends LiteFlowActiveDo> T convert(T dom) {
        BeanUtils.copyProperties(this, dom);
        return dom;
    }

}