package i2f.core.graphics.d2.tranform.impl;

import i2f.core.graphics.d2.Point;
import i2f.core.graphics.d2.tranform.ITransform;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/6/26 18:03
 * @desc 相对于任意参考点进行变换
 * 其实就是先变换回原点
 * 变换完毕之后
 * 重新恢复到原来的点
 */
public class RelativeAnyPointTransform implements ITransform {
    protected boolean enableMatrix=false;
    protected Point relPoint;
    protected List<ITransform> transforms=new ArrayList<>();
    public RelativeAnyPointTransform(boolean enableMatrix,Point relPoint){
        this.enableMatrix=enableMatrix;
        this.relPoint=relPoint;
    }

    public RelativeAnyPointTransform addTransform(ITransform trans){
        this.transforms.add(trans);
        return this;
    }

    @Override
    public Point transform(Point p) {
        // 任意参考点回归参考原点
        ITransform trans=new MoveTransform(enableMatrix,-relPoint.x,-relPoint.y);
        Point np=trans.transform(p);

        // 按照原点进行任意变换
        for (int i = 0; i < transforms.size(); i++) {
            np=transforms.get(i).transform(np);
        }

        // 重新还原到任意参考点
        trans=new MoveTransform(enableMatrix,relPoint.x,relPoint.y);
        return trans.transform(np);
    }
}
