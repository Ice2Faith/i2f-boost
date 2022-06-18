package i2f.core.graphics.d3.transform.impl;

import i2f.core.graphics.d3.D3Point;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/18 21:27
 * @desc 错切变换，坐标依次沿x,y,z轴错切，分别离开z,y轴向x轴移动gz,dy距离，依次类推
 */
@Data
@NoArgsConstructor
public class MiscutAxisXTransform extends AbstractMatrixTransform{

    protected double xgz;
    protected double xdy;

    public MiscutAxisXTransform(boolean enableMatrix, double xgz, double xdy) {
        super(enableMatrix);
        this.xgz = xgz;
        this.xdy = xdy;
    }

    @Override
    public double[][] matrix() {
        return new double[][]{
                { 1, 0, 0, 0 },
                { xdy, 1, 0, 0 },
                { xgz, 0, 1, 0 },
                { 0, 0, 0, 1 }
        };

    }

    @Override
    public D3Point trans(D3Point p) {
        double x= p.x + xdy*p.y + xgz*p.z;
        return new D3Point(x,p.y,p.z);
    }
}
