package i2f.core.graphics.d3.transform.impl;

import i2f.core.graphics.d3.D3Point;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/18 21:27
 * @desc 反射变换，坐标依次对xoz平面反射
 */
@Data
@NoArgsConstructor
public class ReflectFlatXoZTransform extends AbstractMatrixTransform{

    public ReflectFlatXoZTransform(boolean enableMatrix) {
        super(enableMatrix);
    }

    @Override
    public double[][] matrix() {
        return new double[][]{
                { 1, 0, 0, 0 },
                { 0, -1, 0, 0 },
                { 0, 0, 1, 0 },
                { 0, 0, 0, 1 }
        };

    }

    @Override
    public D3Point trans(D3Point p) {
        return new D3Point(p.x,-p.y,p.z);
    }
}
