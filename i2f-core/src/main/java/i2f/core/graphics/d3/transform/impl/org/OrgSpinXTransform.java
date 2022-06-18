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
public class OrgSpinXTransform extends AbstractMatrixTransform {
    protected double sx;

    public OrgSpinXTransform(boolean enableMatrix, double sx) {
        super(enableMatrix);
        this.sx = sx;
    }

    @Override
    public double[][] matrix() {
        return new double[][]{
                { 1, 0, 0, 0 },
                { 0, Math.cos(sx), -Math.sin(sx), 0 },
                { 0, Math.sin(sx), Math.cos(sx), 0 },
                { 0, 0, 0, 1 }
        };

    }

    @Override
    public D3Point trans(D3Point p) {
        double x=p.x;
        double y=p.y;
        double z=p.z;
        // 按x旋转
        y = p.y*Math.cos(sx) + p.z*Math.sin(sx);
        z = p.z*Math.cos(sx) - p.y*Math.sin(sx);

        return new D3Point(x,y,z);
    }
}
