package i2f.core.graphics.d2.shape;

import i2f.core.graphics.d2.Point;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/17 23:26
 * @desc 圆形
 */
@Data
@NoArgsConstructor
public class Circle {
    public Point center;
    public double radius;

    public Circle(Point center, double radius){
        this.center=center;
        this.radius=radius;
    }
}
