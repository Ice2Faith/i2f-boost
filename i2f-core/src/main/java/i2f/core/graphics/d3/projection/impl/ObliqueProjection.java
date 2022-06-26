package i2f.core.graphics.d3.projection.impl;

import i2f.core.graphics.d2.Point;
import i2f.core.graphics.d3.D3Point;
import i2f.core.graphics.d3.D3SphericalPoint;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/18 20:18
 * @desc 斜投影,投影斜度aAngle,越大越接近正交投影：0-PI/2,投影和x轴夹角bAngle,也是光照方向：0-2*PI,xoy
 */
@Data
@NoArgsConstructor
public class ObliqueProjection extends AbstractMatrixProjection {
    protected double aAngle;
    protected double bAngle;

    public ObliqueProjection(boolean enableMatrix, double aAngle, double bAngle) {
        super(enableMatrix);
        this.aAngle = aAngle;
        this.bAngle = bAngle;
    }

    public ObliqueProjection(boolean enableMatrix, D3SphericalPoint viewPoint){
        super(enableMatrix);
        this.aAngle= viewPoint.aAngle;
        this.bAngle=viewPoint.bAngle;
    }

    @Override
    public double[][] matrix() {
        return new double[][]{
                { 1, 0, 0, 0 },
                { 0, 1, 0, 0 },
                { 1.0 / (-Math.tan(aAngle))*Math.cos(bAngle), 1.0 / (-Math.tan(aAngle))*Math.sin(bAngle), 0, 0 },
                { 0, 0, 0, 1 }
        };
    }

    @Override
    public Point proj(D3Point point) {
        double x = point.x - point.z*(1.0 / (Math.tan(aAngle))*Math.cos(bAngle));
        double y = point.y - point.z*(1.0 / (Math.tan(aAngle))*Math.sin(bAngle));
        return new Point(x,y);
    }
}
