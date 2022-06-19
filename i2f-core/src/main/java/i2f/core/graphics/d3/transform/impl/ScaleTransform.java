package i2f.core.graphics.d3.transform.impl;

import i2f.core.graphics.d3.D3Point;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/18 21:27
 * @desc 比例变换，x,y,z分别乘以sx,sy,sz比例
 */
@Data
@NoArgsConstructor
public class ScaleTransform extends AbstractMatrixTransform{
    public double sx;
    public double sy;
    public double sz;

    public ScaleTransform(boolean enableMatrix, double sx, double sy, double sz) {
        super(enableMatrix);
        this.sx = sx;
        this.sy = sy;
        this.sz = sz;
    }

    @Override
    public double[][] matrix() {
        return new double[][]{
                { sx, 0, 0, 0 },
                { 0, sy, 0, 0 },
                { 0, 0, sz, 0 },
                { 0, 0, 0, 1 }
        };

    }

    @Override
    public D3Point trans(D3Point p) {
        double x =p.x * sx;
        double y =p.y * sy;
        double z =p.z * sz;
        return new D3Point(x,y,z);
    }
}
