package i2f.core.graphics.d3.transform.impl;

import i2f.core.graphics.d3.D3Point;
import i2f.core.graphics.d3.transform.ID3Transform;

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
public class RelativeAnyPointTransform implements ID3Transform {
    protected boolean enableMatrix=false;
    protected D3Point relPoint;
    protected List<ID3Transform> transforms=new ArrayList<>();
    public RelativeAnyPointTransform(boolean enableMatrix,D3Point relPoint){
        this.enableMatrix=enableMatrix;
        this.relPoint=relPoint;
    }

    public RelativeAnyPointTransform addTransform(ID3Transform trans){
        this.transforms.add(trans);
        return this;
    }

    @Override
    public D3Point transform(D3Point p) {
        // 任意参考点回归参考原点
        ID3Transform trans=new MoveTransform(enableMatrix,-relPoint.x,-relPoint.y,-relPoint.z);
        D3Point np=trans.transform(p);

        // 按照原点进行任意变换
        for (int i = 0; i < transforms.size(); i++) {
            np=transforms.get(i).transform(np);
        }

        // 重新还原到任意参考点
        trans=new MoveTransform(enableMatrix,relPoint.x,relPoint.y,relPoint.z);
        return trans.transform(np);
    }
}
