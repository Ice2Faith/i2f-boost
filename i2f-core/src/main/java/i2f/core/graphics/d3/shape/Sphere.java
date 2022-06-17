package i2f.core.graphics.d3.shape;

import i2f.core.graphics.d3.D3Point;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/18 0:11
 * @desc 球体
 */
@Data
@NoArgsConstructor
public class Sphere{
    public D3Point center;
    public double radius;

    public Sphere(D3Point center, double radius) {
        this.center = center;
        this.radius = radius;
    }
}
