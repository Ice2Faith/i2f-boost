package i2f.core.graphics.d2.tranform.impl;

import i2f.core.graphics.d2.Point;

/**
 * @author ltb
 * @date 2022/6/21 15:00
 * @desc X轴反射变换
 */
public class ReflectAxisXTransform extends AbstractMatrixTransform{

    public ReflectAxisXTransform(boolean enableMatrix) {
        super(enableMatrix);
    }

    @Override
    public double[][] matrix() {
        return new double[][]{
                {1,0, 0},
                {0,-1, 0},
                {0, 0, 1}
        };
    }

    @Override
    public Point trans(Point p) {
        double x =p.x;
        double y =-p.y;
        return new Point(x,y);
    }
}
