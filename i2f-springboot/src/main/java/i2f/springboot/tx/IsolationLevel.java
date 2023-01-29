package i2f.springboot.tx;

import org.springframework.transaction.TransactionDefinition;

public enum IsolationLevel {

    DEFAULT(TransactionDefinition.ISOLATION_DEFAULT),
    READ_UNCOMMITTED(TransactionDefinition.ISOLATION_READ_UNCOMMITTED),
    READ_COMMITTED(TransactionDefinition.ISOLATION_READ_COMMITTED),
    REPEATABLE_READ(TransactionDefinition.ISOLATION_REPEATABLE_READ),
    SERIALIZABLE(TransactionDefinition.ISOLATION_SERIALIZABLE);

    private int code;

    private IsolationLevel(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}
