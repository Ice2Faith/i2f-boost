package i2f.core.graphics.d2.tranform.impl;

import i2f.core.graphics.d2.Point;

/**
 * @author ltb
 * @date 2022/6/21 15:00
 * @desc 2D错切变换
 */
public class MiscutTransform extends AbstractMatrixTransform{
    protected double bx;
    protected double cy;

    public MiscutTransform(boolean enableMatrix, double bx, double cy) {
        super(enableMatrix);
        this.bx = bx;
        this.cy = cy;
    }

    @Override
    public double[][] matrix() {
        return new double[][]{
                {1, bx, 0},
                {cy, 1, 0},
                {0, 0, 1}
        };
    }

    @Override
    public Point trans(Point p) {
        double x =p.x + cy*p.y;
        double y =bx*p.x + p.y;
        return new Point(x,y);
    }
}
