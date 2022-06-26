package i2f.core.graphics.d3.visible;

import i2f.core.graphics.d3.D3Flat;
import i2f.core.graphics.d3.D3Point;
import i2f.core.graphics.d3.D3SphericalPoint;
import i2f.core.graphics.d3.D3Vector;

/**
 * @author ltb
 * @date 2022/6/26 17:18
 * @desc 背面剔除算法
 * 计算面法向量与视向量之间的叉积，若大于0则可见，小于0则不可见
 * 一般情况下，视向量应该与透视投影的投影向量一致
 */
public class BlankingAlgorithm {
    public static double visible(D3SphericalPoint viewPoint, D3Flat flat){
        D3Vector v1=new D3Vector(flat.p1, flat.p2);
        D3Vector v2=new D3Vector(flat.p2, flat.p3);
        D3Vector nl = v1.normalLine(v2);
        D3Point sp=viewPoint.point();
        D3Vector sv=new D3Vector(flat.p1, sp);
        return nl.cosRadian(sv);
    }
}
