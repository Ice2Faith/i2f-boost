package i2f.core.graphics.d2.shape;

import i2f.core.graphics.d2.Point;
import i2f.core.graphics.d2.Scope;
import i2f.core.graphics.d2.Size;
import lombok.Data;

/**
 * @author ltb
 * @date 2022/6/18 0:10
 * @desc 椭圆
 */
@Data
public class Ellipse extends Scope {
    public Ellipse() {
    }

    public Ellipse(Point point, Size size) {
        super(point, size);
    }
}
