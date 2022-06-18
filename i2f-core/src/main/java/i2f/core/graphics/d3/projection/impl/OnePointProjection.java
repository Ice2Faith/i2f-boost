package i2f.core.graphics.d3.projection.impl;

import i2f.core.graphics.d2.Point;
import i2f.core.graphics.d3.D3Point;
import i2f.core.graphics.d3.D3VaryUtil;
import i2f.core.graphics.math.Calc;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/18 20:18
 * @desc 一点透视,xoy
 */
@Data
@NoArgsConstructor
public class OnePointProjection extends AbstractMatrixProjection {
    protected double r;
    protected double d;

    public OnePointProjection(boolean enableMatrix, double r, double d) {
        super(enableMatrix);
        this.r = r;
        this.d = d;
    }

    @Override
    public double[][] matrix() {
        return new double[][]{
                { 1, 0, 0, 0 },
                { 0, 1, 0, 0 },
                { 0, 0, 0, -1.0 / d },
                { 0, 0, 0, r / d }
        };

    }

    @Override
    public Point proj(D3Point point) {
        D3Point p=D3VaryUtil.projWorldOrgToViewOrg(enableMatrix,point, r, Calc.angle2radian(0), Calc.angle2radian(90));
        Point ret=D3VaryUtil.projViewOrgToScreenOrg(enableMatrix,p,d);
        return ret;
    }


}
