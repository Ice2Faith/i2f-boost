package i2f.core.border.impl.dim2.circle;


import i2f.core.border.BorderInDecider;
import i2f.core.graphics.d2.Point;
import i2f.core.graphics.d2.polygon.PolygonLocationTool;
import i2f.core.graphics.d2.shape.Circle;

/**
 * @author Ice2Faith
 * @date 2023/2/6 9:55
 * @desc
 */
public class PositionInCircleDecider implements BorderInDecider<Point, Circle> {
    @Override
    public boolean isInRange(Point elem, Circle range) {
        return PolygonLocationTool.windInCircle(elem, range.center, range.radius);
    }
}
