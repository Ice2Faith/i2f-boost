package i2f.core.border.impl.dim1;


import i2f.core.border.MiddleElementSplitor;

/**
 * @author Ice2Faith
 * @date 2023/2/6 9:14
 * @desc
 */
public class NumberMiddleElementSplitor implements MiddleElementSplitor<Double> {
    @Override
    public Double middle(Double elem1, Double elem2) {
        return (elem1 + elem2) / 2;
    }
}
