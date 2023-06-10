package i2f.liteflow.consts;

/**
 * @author Ice2Faith
 * @date 2023/6/7 11:16
 * @desc
 */
public enum LiteFlowErrorCode {
    NOT_AUTH(401),
    NOT_PERM(403),
    NOT_FOUND(404),

    ERROR(500),

    MISS_PROCESS_PKEY(601),
    MISS_NODE(602),
    MISS_DAG(603),
    MISS_NODE_PKEY(604),
    MISS_DAG_BEGIN_PKEY(605),
    MISS_DAG_END_PKEY(606),
    MISS_NODE_TYPE(607),
    MISS_NODE_HANDLE_TYPE(608),
    MISS_NODE_HANDLE_BY(609),
    MISS_PROCESS_NAME(610),
    MISS_OPER(611),

    BAD_NODE_TYPE(701),
    BAD_NODE_HANDLE_TYPE(702),
    BAD_DAG_BEGIN_PKEY(703),
    BAD_DAG_END_PKEY(704),

    JSON_PARSE(801),
    JSON_STRINGIFY(802),

    INS_FINISHED(901),
    INS_STOPPED(902),
    INS_RUNNING(903),

    LOG_COMPLETED(1001),
    LOG_NOT_PREPARED(1002)
    ;
    private int code;

    private LiteFlowErrorCode(int code) {
        this.code = code;
    }

    public int code() {
        return this.code;
    }
}
