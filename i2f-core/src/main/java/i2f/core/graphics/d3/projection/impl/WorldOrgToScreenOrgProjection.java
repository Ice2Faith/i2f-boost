package i2f.core.graphics.d3.projection.impl;

import i2f.core.graphics.d2.Point;
import i2f.core.graphics.d3.D3Point;
import i2f.core.graphics.d3.D3VaryUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/18 20:18
 * @desc 世界坐标系转屏幕坐标系，视径R,视距d，z轴角度a,y轴角度b,xoy
 */
@Data
@NoArgsConstructor
public class WorldOrgToScreenOrgProjection extends AbstractMatrixProjection {
    protected double r;
    protected double d;
    protected double aAngle;
    protected double bAngle;

    public WorldOrgToScreenOrgProjection(boolean enableMatrix, double r, double d, double aAngle, double bAngle) {
        super(enableMatrix);
        this.r = r;
        this.d = d;
        this.aAngle = aAngle;
        this.bAngle = bAngle;
    }

    @Override
    public double[][] matrix() {
        return new double[][]{
                { Math.cos(aAngle), -Math.cos(bAngle)*Math.sin(aAngle), 0, -Math.sin(bAngle)*Math.sin(aAngle) / d },
                { 0, Math.sin(bAngle), 0, -Math.cos(bAngle) / d },
                { -Math.sin(aAngle), -Math.cos(bAngle)*Math.cos(aAngle), 0, -Math.sin(bAngle)*Math.cos(aAngle) / d },
                { 0, 0, 0, r / d } };
    }

    @Override
    public Point proj(D3Point point) {
        D3Point p=D3VaryUtil.projWorldOrgToViewOrg(enableMatrix,point, r, aAngle, bAngle);
        Point ret=D3VaryUtil.projViewOrgToScreenOrg(enableMatrix,p,d);
        return ret;
    }


}
