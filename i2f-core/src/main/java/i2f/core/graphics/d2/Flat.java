package i2f.core.graphics.d2;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author ltb
 * @date 2022/6/17 22:49
 * @desc 平面
 */
@Data
@NoArgsConstructor
public class Flat {
    public Point p1;
    public Point p2;
    public Point p3;

    public Flat(Point p1, Point p2, Point p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

}
