package i2f.spring.chain;

/**
 * @author ltb
 * @date 2022/9/20 16:59
 * @desc
 */
public enum ChainAction {
    RUN, STOP, PAUSE, CONTINUE,
    BEFORE, AFTER, RETURNING, THROWING,
    ADD, DELETE, UPDATE, QUERY, SAVE;
}
