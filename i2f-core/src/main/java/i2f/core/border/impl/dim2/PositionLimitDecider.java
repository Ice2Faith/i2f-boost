package i2f.core.border.impl.dim2;


import i2f.core.border.BorderLimitDecider;
import i2f.core.graphics.d2.Point;

/**
 * @author Ice2Faith
 * @date 2023/2/6 9:58
 * @desc
 */
public class PositionLimitDecider implements BorderLimitDecider<Point> {
    private Double lim = 1e-10;

    public PositionLimitDecider() {
    }

    public PositionLimitDecider(Double lim) {
        this.lim = lim;
    }

    @Override
    public boolean isLimit(Point elem1, Point elem2) {
        double dis = Math.sqrt(Math.pow(elem2.getX() - elem1.getX(), 2.0) + Math.pow(elem2.getY() - elem1.getY(), 2.0));
        return dis < lim;
    }
}
