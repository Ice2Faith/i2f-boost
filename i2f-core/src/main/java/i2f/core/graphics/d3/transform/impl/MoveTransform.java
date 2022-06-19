package i2f.core.graphics.d3.transform.impl;

import i2f.core.graphics.d3.D3Point;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/18 21:27
 * @desc 平移变换，x,y,z分别平移mx,my,mz距离
 */
@Data
@NoArgsConstructor
public class MoveTransform extends AbstractMatrixTransform{
    public double mx;
    public double my;
    public double mz;

    public MoveTransform(boolean enableMatrix, double mx, double my, double mz) {
        super(enableMatrix);
        this.mx = mx;
        this.my = my;
        this.mz = mz;
    }

    @Override
    public double[][] matrix() {
        return new double[][]{
                { 1, 0, 0, 0 },
                { 0, 1, 0, 0 },
                { 0, 0, 1, 0 },
                { mx, my, mz, 1 }
        };
    }

    @Override
    public D3Point trans(D3Point p) {
        double x =p.x + mx;
        double y =p.y + my;
        double z =p.z + mz;
        return new D3Point(x,y,z);
    }
}
