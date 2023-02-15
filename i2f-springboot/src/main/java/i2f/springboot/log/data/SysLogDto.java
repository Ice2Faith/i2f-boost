package i2f.springboot.log.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Ice2Faith
 * @date 2023/2/7 10:16
 * @desc
 */
@Data
@NoArgsConstructor
public class SysLogDto {
    // ID
    private Long id;
    // 来源系统
    private String srcSystem;
    // 来源模块
    private String srcModule;
    // 来源标签
    private String srcLabel;

    // 日志位置（className）
    private String logLocation;
    // 日志类型：0 登录日志，1 登出日志，2 注册日志，3 注销日志，4 接口日志，5 输出日志，6 服务器状态，7 服务异常，8 调用日志，9 回调日志
    private Integer logType;
    // 日志内容
    private String logContent;
    // 日志级别：0 ERROR，1 WARN，2 INFO，3 DEBUG，4 TRACE
    private Integer logLevel;
    // 操作类型：0 查询，1 新增，2 修改，3 删除，4 申请，5 审批，6 导入，7 导出
    private Integer operateType;

    // 日志键
    private String logKey;
    // 日志值
    private String logVal;

    // 跟踪ID
    private String traceId;
    // 跟踪层次
    private Integer traceLevel;

    // 操作用户账号
    private String userId;
    // 操作用户名称
    private String userName;

    // 客户端IP
    private String clientIp;
    // 请求java方法
    private String javaMethod;

    // 异常分类，0 Exception,1 RuntimeException,2 Error,3 Throwable,4 SQLException
    private Integer exceptType;
    // 异常类
    private String exceptClass;
    // 异常信息
    private String exceptMsg;
    // 异常堆栈
    private String exceptStack;

    // 请求路径
    private String requestUrl;
    // 请求参数
    private String requestParam;
    // 请求类型
    private String requestType;

    // 耗时，毫秒
    private Long costTime;

    // 创建时间
    private Date createTime;
}
