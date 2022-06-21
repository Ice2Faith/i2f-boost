package i2f.core.graphics.d2.tranform.impl;

import i2f.core.graphics.d2.Point;

/**
 * @author ltb
 * @date 2022/6/21 15:00
 * @desc 2D旋转变换
 */
public class SpinTransform extends AbstractMatrixTransform{
    protected double angle;

    public SpinTransform(boolean enableMatrix, double angle) {
        super(enableMatrix);
        this.angle = angle;
    }

    @Override
    public double[][] matrix() {
        return new double[][]{
                {Math.cos(angle), Math.sin(angle), 0},
                {-Math.sin(angle), Math.cos(angle), 0},
                {0, 0, 1}
        };
    }

    @Override
    public Point trans(Point p) {
        double x =p.x * Math.cos(angle)-p.y*Math.sin(angle);
        double y =p.x*Math.sin(angle)+p.y*Math.cos(angle);
        return new Point(x,y);
    }
}
