package i2f.liteflow.data.dom;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @author Ice2Faith
 * @date 2023-06-07 14:24:49
 * @desc lite_flow_instance 流程实例表
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class LiteFlowInstanceDo {


    /**
     * 主键
     */
    protected Long id;

    /**
     * 流程ID
     */
    protected Long processId;

    /**
     * 业务键
     */
    protected String businessKey;

    /**
     * 实例状态：0 停止，1 运行，2 结束
     */
    protected Integer status;

    /**
     * 创建时间
     */
    protected Date createTime;

    /**
     * 创建人
     */
    protected String createBy;


    public <T extends LiteFlowInstanceDo> T convert(T dom) {
        BeanUtils.copyProperties(this, dom);
        return dom;
    }

}