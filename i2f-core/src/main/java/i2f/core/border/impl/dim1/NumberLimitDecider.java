package i2f.core.border.impl.dim1;


import i2f.core.border.BorderLimitDecider;

/**
 * @author Ice2Faith
 * @date 2023/2/6 9:12
 * @desc
 */
public class NumberLimitDecider implements BorderLimitDecider<Double> {
    private Double lim = 1e-12;

    public NumberLimitDecider() {
    }

    public NumberLimitDecider(Double lim) {
        this.lim = lim;
    }

    @Override
    public boolean isLimit(Double elem1, Double elem2) {
        return Math.abs(elem2 - elem1) <= lim;
    }
}
