package i2f.core.border.impl.dim2;

import i2f.core.border.MiddleElementSplitor;
import i2f.core.graphics.d2.Point;

/**
 * @author Ice2Faith
 * @date 2023/2/6 10:02
 * @desc
 */
public class PositionMiddleElementSplitor implements MiddleElementSplitor<Point> {
    @Override
    public Point middle(Point elem1, Point elem2) {
        return new Point((elem2.getX() + elem1.getX()) / 2, (elem2.getY() + elem1.getY()) / 2);
    }
}
