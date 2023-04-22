package i2f.core.border.impl.dim1;

import lombok.ToString;

/**
 * @author Ice2Faith
 * @date 2023/2/6 9:08
 * @desc
 */
@ToString
public class NumberRange {
    public Double begin;
    public Double end;

    public NumberRange() {
    }

    public NumberRange(Double begin, Double end) {
        this.begin = begin;
        this.end = end;
    }
}
