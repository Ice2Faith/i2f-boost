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
public class OrgSpinZTransform extends AbstractMatrixTransform {
    protected double sz;

    public OrgSpinZTransform(boolean enableMatrix, double sz) {
        super(enableMatrix);
        this.sz = sz;
    }

    @Override
    public double[][] matrix() {
        return new double[][]{
                { Math.cos(sz), -Math.sin(sz), 0, 0 },
                { Math.sin(sz), Math.cos(sz), 0, 0 },
                { 0, 0, 1, 0 }, { 0, 0, 0, 1 }
        };

    }

    @Override
    public D3Point trans(D3Point p) {
        double x=p.x;
        double y=p.y;
        double z=p.z;

        // 按z旋转
        x = p.x*Math.cos(sz) + p.y*Math.sin(sz);
        y = p.y*Math.cos(sz) - p.x*Math.sin(sz);
        return new D3Point(x,y,z);
    }
}
