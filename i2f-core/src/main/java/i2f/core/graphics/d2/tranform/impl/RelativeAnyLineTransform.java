package i2f.core.graphics.d2.tranform.impl;

import i2f.core.graphics.d2.Point;
import i2f.core.graphics.d2.tranform.ITransform;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/6/26 18:03
 * @desc 相对于任意参考线进行变换
 * 其实就是先变换回x轴
 * 变换完毕之后
 * 重新恢复到原来直线
 * y=kx+b
 */
public class RelativeAnyLineTransform implements ITransform {
    protected boolean enableMatrix=false;
    protected double k;
    protected double b;
    protected List<ITransform> transforms=new ArrayList<>();
    public RelativeAnyLineTransform(boolean enableMatrix,double k,double b){
        this.enableMatrix=enableMatrix;
        this.k=k;
        this.b=b;
    }

    public RelativeAnyLineTransform addTransform(ITransform trans){
        this.transforms.add(trans);
        return this;
    }

    @Override
    public Point transform(Point p) {
        // 任意参考点回归参考原点
        ITransform trans=new MoveTransform(enableMatrix,0,-b);
        Point np=trans.transform(p);
        // 旋转到X轴
        trans=new SpinTransform(enableMatrix,-Math.atan(k));
        np=trans.transform(np);

        // 按照原点进行任意变换
        for (int i = 0; i < transforms.size(); i++) {
            np=transforms.get(i).transform(np);
        }

        // 旋转回去
        trans=new SpinTransform(enableMatrix,Math.atan(k));
        np=trans.transform(np);

        // 重新还原到任意参考点
        trans=new MoveTransform(enableMatrix,0,b);
        return trans.transform(np);
    }
}
