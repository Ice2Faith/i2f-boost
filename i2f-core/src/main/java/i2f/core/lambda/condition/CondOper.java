package i2f.core.lambda.condition;

/**
 * @author ltb
 * @date 2022/9/1 8:51
 * @desc
 */
public enum CondOper implements IText {
    EQ("="),
    NEQ("!="),
    GT(">"),
    LT("<"),
    GTE(">="),
    LTE("<="),
    IN("in"),
    LIKE("like"),
    BETWEEN("between"),
    EXISTS("exists"),
    NOT_IN("not in"),
    NOT_LIKE("not like"),
    NOT_EXISTS("not exists");
    private String text;

    private CondOper(String text) {
        this.text = text;
    }

    @Override
    public String text() {
        return text;
    }
}
