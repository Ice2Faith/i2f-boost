package i2f.core.graphics.d3.transform.impl;

import i2f.core.graphics.d3.D3Point;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/18 21:27
 * @desc 旋转变换，坐标依次绕z轴旋转sz角度（弧度制）,相对于轴右手向旋转
 */
@Data
@NoArgsConstructor
public class SpinZTransform extends AbstractMatrixTransform{
    protected double sz;

    public SpinZTransform(boolean enableMatrix, double sz) {
        super(enableMatrix);
        this.sz = sz;
    }

    @Override
    public double[][] matrix() {
        return new double[][]{
                { Math.cos(sz), Math.sin(sz), 0, 0 },
                { -Math.sin(sz), Math.cos(sz), 0, 0 },
                { 0, 0, 1, 0 },
                { 0, 0, 0, 1 }
        };

    }

    @Override
    public D3Point trans(D3Point p) {
        double x=p.x;
        double y=p.y;
        double z=p.z;

        // 按z旋转
        x = x*Math.cos(sz) - y*Math.sin(sz);
        y = x*Math.sin(sz) + y*Math.cos(sz);
        return new D3Point(x,y,z);
    }
}
