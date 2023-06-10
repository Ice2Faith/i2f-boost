package i2f.liteflow.data.dom;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

/**
 * @author Ice2Faith
 * @date 2023-06-07 14:27:04
 * @desc lite_flow_params 流程参数表
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class LiteFlowParamsDo {


    /**
     * 主键
     */
    protected Long id;

    /**
     * 实例ID
     */
    protected Long instanceId;

    /**
     * 参数键
     */
    protected String entryKey;

    /**
     * 参数类型
     */
    protected String entryType;

    /**
     * 参数内容
     */
    protected String entryValue;


    public <T extends LiteFlowParamsDo> T convert(T dom) {
        BeanUtils.copyProperties(this, dom);
        return dom;
    }

}