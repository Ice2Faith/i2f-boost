package i2f.core.graphics.d2.tranform.impl;

import i2f.core.graphics.d2.D2VaryUtil;
import i2f.core.graphics.d2.Point;
import i2f.core.graphics.d2.tranform.ITransform;

/**
 * @author ltb
 * @date 2022/6/21 14:53
 * @desc 支持矩阵运算方式的2D转换基础类
 */
public abstract class AbstractMatrixTransform implements ITransform {
    protected boolean enableMatrix;

    public AbstractMatrixTransform(boolean enableMatrix) {
        this.enableMatrix = enableMatrix;
    }

    @Override
    public Point transform(Point point) {
        if(enableMatrix){
            Point p= D2VaryUtil.vary(point,matrix());
            return p;
        }else{
            Point p=new Point(point.x,point.y);
            return trans(p);
        }
    }

    public abstract double[][] matrix();

    public abstract Point trans(Point p);
}
