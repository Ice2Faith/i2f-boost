package i2f.core.graphics.d2.projection.impl;

import i2f.core.graphics.d2.Point;
import i2f.core.graphics.d2.projection.IProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/19 15:13
 * @desc 数学中央坐标系 x向右，y向上
 */
@Data
@NoArgsConstructor
public class MathCenterProjection implements IProjection {
    public double width;
    public double height;

    public MathCenterProjection(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public Point projection(Point p) {
        double dx=p.x+width/2;
        double dy=height/2-p.y;
        return new Point(dx,dy);
    }

    @Override
    public void onSizeChange(int width, int height) {
        this.width=width;
        this.height=height;
    }
}
