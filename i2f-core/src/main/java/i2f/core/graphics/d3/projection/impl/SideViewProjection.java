package i2f.core.graphics.d3.projection.impl;

import i2f.core.graphics.d2.Point;
import i2f.core.graphics.d3.D3Point;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/18 20:18
 * @desc 侧视图,yoz,转换到xoy(投影到xoy平面)
 */
@Data
@NoArgsConstructor
public class SideViewProjection extends AbstractMatrixProjection {
    public SideViewProjection(boolean enableMatrix) {
        super(enableMatrix);
    }

    @Override
    public double[][] matrix() {
        return new double[][]{
                { 0, 0, -1, 0 },
                { 0, 1, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 1 }
        };
    }

    @Override
    public Point proj(D3Point point) {
        return new Point(point.z,0-point.x);
    }

    @Override
    public D3Point beforeMatrixReturn(D3Point p) {
        return swapXZ(p);
    }
}
