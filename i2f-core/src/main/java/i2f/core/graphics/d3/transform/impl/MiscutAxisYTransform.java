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
public class MiscutAxisYTransform extends AbstractMatrixTransform{

    protected double yhz;
    protected double ybx;

    public MiscutAxisYTransform(boolean enableMatrix, double yhz, double ybx) {
        super(enableMatrix);
        this.yhz = yhz;
        this.ybx = ybx;
    }

    @Override
    public double[][] matrix() {
        return new double[][]{
                { 1, ybx, 0, 0 },
                { 0, 1, 0, 0 },
                { 0, yhz, 1, 0 },
                { 0, 0, 0, 1 }
        };

    }

    @Override
    public D3Point trans(D3Point p) {
        double y= ybx*p.x + p.y + p.z*yhz;
        return new D3Point(p.x,y,p.z);
    }
}
