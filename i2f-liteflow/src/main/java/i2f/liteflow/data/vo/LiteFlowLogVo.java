package i2f.liteflow.data.vo;


import i2f.liteflow.data.dom.LiteFlowLogDo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Ice2Faith
 * @date 2023-06-07 14:25:56
 * @desc lite_flow_log 流程日志表
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class LiteFlowLogVo extends LiteFlowLogDo {


    public LiteFlowLogDo parent() {
        return (LiteFlowLogDo) this;
    }


    /**
     * 是否已经准备完毕：0 否，1 是
     */
    protected String preparedDesc;

    /**
     * 是否最新节点：0 否，1 是 desc
     */
    protected String newestDesc;

    private LiteFlowInstanceVo instance;

    private String nextHandleBy;
}