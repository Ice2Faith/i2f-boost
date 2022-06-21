package i2f.core.graphics.d2.tranform.impl;

import i2f.core.graphics.d2.Point;
import i2f.core.graphics.d2.tranform.ITransform;

/**
 * @author ltb
 * @date 2022/6/21 15:00
 * @desc 轴反射变换
 */
public class ReflectAxisTransform implements ITransform {

    protected boolean enableMatrix;
    protected boolean dx;
    protected boolean dy;

    public ReflectAxisTransform(boolean enableMatrix, boolean dx, boolean dy) {
        this.enableMatrix=enableMatrix;
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public Point transform(Point p) {
        Point np=new Point(p.x,p.y);
        if(dx){
            np=new ReflectAxisXTransform(enableMatrix).transform(np);
        }
        if(dy){
            np=new ReflectAxisYTransform(enableMatrix).transform(np);
        }
        return np;
    }
}
