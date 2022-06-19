package i2f.core.graphics.d2.projection.impl;

import i2f.core.graphics.d2.Point;
import i2f.core.graphics.d2.projection.IProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/19 15:13
 * @desc 数学中央坐标系,相对于屏幕左上角原点的偏移 x向右，y向上
 */
@Data
@NoArgsConstructor
public class MathOffsetProjection implements IProjection {
    public double offX;
    public double offY;

    public MathOffsetProjection(double offX, double offY) {
        this.offX = offX;
        this.offY = offY;
    }

    @Override
    public Point projection(Point p) {
        double dx=p.x+offX;
        double dy=offY-p.y;
        return new Point(dx,dy);
    }

    @Override
    public void onSizeChange(int width, int height) {

    }
}
