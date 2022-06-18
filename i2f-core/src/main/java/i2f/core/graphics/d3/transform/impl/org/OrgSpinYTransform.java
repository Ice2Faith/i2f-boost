package i2f.core.graphics.d3.transform.impl.org;

import i2f.core.graphics.d3.D3Point;
import i2f.core.graphics.d3.transform.impl.AbstractMatrixTransform;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/18 21:27
 * @desc 坐标系旋转变换，坐标系依次绕x,y,z轴旋转sx,sy,sz角度（弧度制）后的位置
 */
@Data
@NoArgsConstructor
public class OrgSpinYTransform extends AbstractMatrixTransform {
    protected double sy;

    public OrgSpinYTransform(boolean enableMatrix, double sy) {
        super(enableMatrix);
        this.sy = sy;
    }

    @Override
    public double[][] matrix() {
        return new double[][]{
                { Math.cos(sy), 0, Math.sin(sy), 0 },
                { 0, 1, 0, 0 },
                { -Math.sin(sy), 0, Math.cos(sy), 0 },
                { 0, 0, 0, 1 }
        };

    }

    @Override
    public D3Point trans(D3Point p) {
        double x=p.x;
        double y=p.y;
        double z=p.z;
        // 按y旋转
        x = p.x*Math.cos(sy) - p.z*Math.sin(sy);
        z = p.z*Math.cos(sy) + p.x*Math.sin(sy);
        return new D3Point(x,y,z);
    }
}
