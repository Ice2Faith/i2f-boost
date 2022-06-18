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
public class MiscutAxisZTransform extends AbstractMatrixTransform{

    protected double zfy;
    protected double zcx;

    public MiscutAxisZTransform(boolean enableMatrix, double zfy, double zcx) {
        super(enableMatrix);
        this.zfy = zfy;
        this.zcx = zcx;
    }

    @Override
    public double[][] matrix() {
        return new double[][]{
                { 1, 0, zcx, 0 },
                { 0, 1, zfy, 0 },
                { 0, 0, 1, 0 },
                { 0, 0, 0, 1 }
        };

    }

    @Override
    public D3Point trans(D3Point p) {
        double z= zcx*p.x + zfy*p.y + p.z;
        return new D3Point(p.x,p.y,z);
    }
}
