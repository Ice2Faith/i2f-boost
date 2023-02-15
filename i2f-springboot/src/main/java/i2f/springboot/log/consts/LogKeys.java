package i2f.springboot.log.consts;

/**
 * @author Ice2Faith
 * @date 2023/2/7 14:15
 * @desc
 */
public enum LogKeys {
    APP_STARTUP("app.startup", "系统启动"),
    APP_INITIALED("app.initialed", "系统初始化完成"),
    APP_CONTEXT_STARTED("app.context.started", "上下文启动"),
    APP_CONTEXT_STOPPED("app.context.stopped", "上下文关闭"),
    APP_CONTEXT_REFRESH("app.env.refresh", "上下文刷新"),
    SYS_JVM_MEMORY("sys.jvm.memory", "系统JVM内存"),
    SYS_JVM_STATUS("sys.jvm.status", "系统JVM状态"),
    SYS_CMD_FREE("sys.cmd.free", "系统命令free"),
    SYS_CMD_TOP("sys.cmd.top", "系统命令top"),
    API_NAME("api.name", "接口名称");

    private String key;
    private String desc;

    LogKeys(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public String key() {
        return key;
    }

    public String desc() {
        return desc;
    }
}
