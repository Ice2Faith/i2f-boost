package i2f.core.graphics.d3.transform.impl.org;

import i2f.core.graphics.d3.D3Point;
import i2f.core.graphics.d3.transform.impl.AbstractMatrixTransform;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/18 21:27
 * @desc 坐标系移动变换，x,y,z分别为坐标系平移mx,my,mz后的位置
 */
@Data
@NoArgsConstructor
public class OrgMoveTransform extends AbstractMatrixTransform {
    protected double mx;
    protected double my;
    protected double mz;

    public OrgMoveTransform(boolean enableMatrix, double mx, double my, double mz) {
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
                { -mx, -my, -mz, 1 }
        };

    }

    @Override
    public D3Point trans(D3Point p) {
        double x =p.x - mx;
        double y =p.y - my;
        double z =p.z - mz;
        return new D3Point(x,y,z);
    }
}
