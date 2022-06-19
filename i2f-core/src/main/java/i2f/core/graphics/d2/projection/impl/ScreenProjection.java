package i2f.core.graphics.d2.projection.impl;

import i2f.core.graphics.d2.Point;
import i2f.core.graphics.d2.projection.IProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/19 15:13
 * @desc 屏幕左上角坐标系，一般的计算机默认坐标系，x向右，y向下
 */
@Data
@NoArgsConstructor
public class ScreenProjection implements IProjection {
    public double width;
    public double height;

    public ScreenProjection(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public Point projection(Point p) {
        double dx=p.x;
        double dy=p.y;
        return new Point(dx,dy);
    }

    @Override
    public void onSizeChange(int width, int height) {
        this.width=width;
        this.height=height;
    }
}
