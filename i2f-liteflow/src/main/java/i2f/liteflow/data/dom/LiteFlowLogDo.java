package i2f.liteflow.data.dom;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @author Ice2Faith
 * @date 2023-06-07 14:25:56
 * @desc lite_flow_log 流程日志表
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class LiteFlowLogDo {


    /**
     * 主键
     */
    protected Long id;

    /**
     * 实例ID
     */
    protected Long instanceId;

    /**
     * 当前节点键
     */
    protected String nodeKey;

    /**
     * 上一节点ID
     */
    protected Long prevId;

    /**
     * 是否已经准备完毕：0 否，1 是
     */
    protected Integer prepared;

    /**
     * 是否最新节点：0 否，1 是
     */
    protected Integer newest;

    /**
     * 处理时间
     */
    protected Date handleTime;

    /**
     * 处理消息
     */
    protected String handleMsg;

    /**
     * 处理人
     */
    protected String handleBy;

    /**
     * 参数
     */
    protected String param1;

    /**
     * 参数
     */
    protected String param2;

    /**
     * 参数
     */
    protected String param3;

    /**
     * 参数
     */
    protected String param4;

    /**
     * 参数
     */
    protected String param5;


    public <T extends LiteFlowLogDo> T convert(T dom) {
        BeanUtils.copyProperties(this, dom);
        return dom;
    }

}