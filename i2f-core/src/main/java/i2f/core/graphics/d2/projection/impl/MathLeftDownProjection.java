package i2f.core.graphics.d2.projection.impl;

import i2f.core.graphics.d2.Point;
import i2f.core.graphics.d2.projection.IProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/19 15:13
 * @desc 数学左下角坐标系，x向右，y向上
 */
@Data
@NoArgsConstructor
public class MathLeftDownProjection implements IProjection {
    public double width;
    public double height;

    public MathLeftDownProjection(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public Point projection(Point p) {
        double dx=p.x;
        double dy=height-p.y;
        return new Point(dx,dy);
    }

    @Override
    public void onSizeChange(int width, int height) {
        this.width=width;
        this.height=height;
    }
}
