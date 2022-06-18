package i2f.core.graphics.d3.transform.impl;

import i2f.core.graphics.d3.D3Point;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/18 21:27
 * @desc 旋转变换，坐标依次绕y轴旋转sy角度（弧度制）,相对于轴右手向旋转
 */
@Data
@NoArgsConstructor
public class SpinYTransform extends AbstractMatrixTransform{
    protected double sy;

    public SpinYTransform(boolean enableMatrix,  double sy) {
        super(enableMatrix);
        this.sy = sy;
    }

    @Override
    public double[][] matrix() {
        return new double[][]{
                { Math.cos(sy), 0, -Math.sin(sy), 0 },
                { 0, 1, 0, 0 },
                { Math.sin(sy), 0, Math.cos(sy), 0 },
                { 0, 0, 0, 1 } };


    }

    @Override
    public D3Point trans(D3Point p) {
        double x=p.x;
        double y=p.y;
        double z=p.z;
        // 按y旋转
        x = z*Math.sin(sy) + x*Math.cos(sy);
        z = z*Math.cos(sy) - x*Math.sin(sy);
        return new D3Point(x,y,z);
    }
}
