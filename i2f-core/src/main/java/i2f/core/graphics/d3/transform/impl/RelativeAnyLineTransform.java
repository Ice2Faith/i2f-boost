package i2f.core.graphics.d3.transform.impl;


import i2f.core.graphics.d3.D3Line;
import i2f.core.graphics.d3.D3Point;
import i2f.core.graphics.d3.D3SphericalPoint;
import i2f.core.graphics.d3.D3Vector;
import i2f.core.graphics.d3.transform.ID3Transform;

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
public class RelativeAnyLineTransform implements ID3Transform {
    protected boolean enableMatrix=false;
    protected D3Line line;
    protected List<ID3Transform> transforms=new ArrayList<>();
    public RelativeAnyLineTransform(boolean enableMatrix,D3Line line){
        this.enableMatrix=enableMatrix;
        this.line=line;
    }

    public RelativeAnyLineTransform addTransform(ID3Transform trans){
        this.transforms.add(trans);
        return this;
    }

    @Override
    public D3Point transform(D3Point p) {
        D3Point begin=line.begin;
        D3Vector vector=new D3Vector(line);
        D3SphericalPoint spherical = vector.spherical();
        // 任意参考点回归参考原点
        ID3Transform trans=new MoveTransform(enableMatrix,-begin.x,-begin.y,-begin.z);
        D3Point np=trans.transform(p);
        // 绕Y轴旋转-aAngle到与YOZ平面重合
        trans=new SpinYTransform(enableMatrix,-spherical.aAngle);
        np=trans.transform(np);
        // 绕X轴旋转-bAngle到与Z轴重合
        trans=new SpinXTransform(enableMatrix,-spherical.bAngle);
        np=trans.transform(np);

        // 按照原点进行任意变换
        for (int i = 0; i < transforms.size(); i++) {
            np=transforms.get(i).transform(np);
        }

        // 旋转回去
        trans=new SpinXTransform(enableMatrix,spherical.bAngle);
        np=trans.transform(np);

        trans=new SpinYTransform(enableMatrix,spherical.aAngle);
        np=trans.transform(np);

        // 重新还原到任意参考点
        trans=new MoveTransform(enableMatrix,begin.x,begin.y,begin.z);
        return trans.transform(np);
    }
}
