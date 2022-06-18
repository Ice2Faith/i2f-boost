package i2f.core.graphics.d2.shape;

import i2f.core.graphics.d2.Point;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ltb
 * @date 2022/6/17 23:26
 * @desc 多边形
 */
@Data
@NoArgsConstructor
public class Polygon {
    public List<Point> points;

    public Polygon(List<Point> points) {
        this.points = points;
    }
}
