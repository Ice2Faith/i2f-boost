package i2f.liteflow.data.dom;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @author Ice2Faith
 * @date 2023-06-07 14:21:53
 * @desc lite_flow_process 流程表
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class LiteFlowProcessDo {


    /**
     * 流程ID
     */
    protected Long id;

    /**
     * 流程键
     */
    protected String pkey;

    /**
     * 版本
     */
    protected Integer version;

    /**
     * 是否激活：0 否，1 是
     */
    protected Integer active;

    /**
     * 归属目录
     */
    protected String folder;

    /**
     * 名称
     */
    protected String name;

    /**
     * 备注
     */
    protected String remark;

    /**
     * 图-图片
     */
    protected String graphImg;

    /**
     * 图-原始文件
     */
    protected String graphFile;

    /**
     * 创建时间
     */
    protected Date createTime;

    /**
     * 创建人
     */
    protected String createBy;

    /**
     * 更新时间
     */
    protected Date updateTime;

    /**
     * 更新人
     */
    protected String updateBy;


    public <T extends LiteFlowProcessDo> T convert(T dom) {
        BeanUtils.copyProperties(this, dom);
        return dom;
    }

}