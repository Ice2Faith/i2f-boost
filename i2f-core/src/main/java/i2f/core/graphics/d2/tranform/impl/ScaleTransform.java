package i2f.core.graphics.d2.tranform.impl;

import i2f.core.graphics.d2.Point;

/**
 * @author ltb
 * @date 2022/6/21 15:00
 * @desc 2D缩放变换
 */
public class ScaleTransform extends AbstractMatrixTransform{
    protected double sx;
    protected double sy;

    public ScaleTransform(boolean enableMatrix, double sx, double sy) {
        super(enableMatrix);
        this.sx = sx;
        this.sy = sy;
    }

    @Override
    public double[][] matrix() {
        return new double[][]{
                {sx, 0, 0},
                {0, sy, 0},
                {0, 0, 1}
        };
    }

    @Override
    public Point trans(Point p) {
        double x =p.x * sx;
        double y =p.y * sy;
        return new Point(x,y);
    }
}
