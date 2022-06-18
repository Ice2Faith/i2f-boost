package i2f.core.graphics.d3.transform.impl;

import i2f.core.graphics.d3.D3Point;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/18 21:27
 * @desc 旋转变换，坐标依次绕x轴旋转sx角度（弧度制）,相对于轴右手向旋转
 */
@Data
@NoArgsConstructor
public class SpinXTransform extends AbstractMatrixTransform{
    protected double sx;

    public SpinXTransform(boolean enableMatrix, double sx) {
        super(enableMatrix);
        this.sx = sx;
    }

    @Override
    public double[][] matrix() {
        return new double[][]{
                { 1, 0, 0, 0 },
                { 0, Math.cos(sx), Math.sin(sx), 0 },
                { 0, -Math.sin(sx), Math.cos(sx), 0 },
                { 0, 0, 0, 1 }
        };

    }

    @Override
    public D3Point trans(D3Point p) {
        double x=p.x;
        double y=p.y;
        double z=p.z;
        // 按x旋转
        y = y*Math.cos(sx) - z*Math.sin(sx);
        z = y*Math.sin(sx) + z*Math.cos(sx);

        return new D3Point(x,y,z);
    }
}
