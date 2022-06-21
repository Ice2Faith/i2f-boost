package i2f.core.graphics.d2.tranform.impl;

import i2f.core.graphics.d2.Point;

/**
 * @author ltb
 * @date 2022/6/21 15:00
 * @desc 2D平移变换
 */
public class MoveTransform extends AbstractMatrixTransform{
    protected double dx;
    protected double dy;

    public MoveTransform(boolean enableMatrix, double dx, double dy) {
        super(enableMatrix);
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public double[][] matrix() {
        return new double[][]{
                {1, 0, 0},
                {0, 1, 0},
                {dx, dy, 1}
        };
    }

    @Override
    public Point trans(Point p) {
        double x =p.x + dx;
        double y =p.y + dy;
        return new Point(x,y);
    }
}
