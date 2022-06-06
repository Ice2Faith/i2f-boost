package i2f.springboot.zplugin.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author ltb
 * @date 2022/5/18 14:58
 * @desc
 */
@ApiModel(value = "通用文件表")
@Data
@NoArgsConstructor
public class FileInfo {
    @ApiModelProperty(value = "主键id")
    private String comFileId;  //  主键ID

    @ApiModelProperty(value = "模块分类")
    private String moduleType;

    @ApiModelProperty(value = "命名空间，用来作为不同用户隔离")
    private String nameSpace;

    @ApiModelProperty(value = "状态：0 禁用，1 启用，99 删除")
    private String status;

    @ApiModelProperty(value = "原文件名")
    private String fileName;

    @ApiModelProperty(value = "文件类型")
    private String fileType;

    @ApiModelProperty(value = "文件大小，字节")
    private String fileSize;

    @ApiModelProperty(value = "文件校验和")
    private String fileChecksum;

    @ApiModelProperty(value = "服务器存储文件名,下载使用")
    private String serverName;

    @ApiModelProperty(value = "排序字段")
    private String orderIndex;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人")
    private String createUserId;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "修改人")
    private String modifyUserId;

    @ApiModelProperty(value = "修改时间")
    private String modifyTime;

    ////////////////////

    @ApiModelProperty(value = "状态描述")
    private String statusDesc;

    @ApiModelProperty(value = "创建人姓名")
    private String createUserName;

    @ApiModelProperty(value = "修改人姓名")
    private String modifyUserName;

}
