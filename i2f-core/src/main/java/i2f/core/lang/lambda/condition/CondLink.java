package i2f.core.lang.lambda.condition;

/**
 * @author ltb
 * @date 2022/9/1 8:51
 * @desc
 */
public enum CondLink implements IText {
    AND("and"),
    OR("or");

    private String text;

    private CondLink(String text) {
        this.text = text;
    }

    @Override
    public String text() {
        return text;
    }
}
