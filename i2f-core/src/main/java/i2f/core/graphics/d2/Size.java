package i2f.core.graphics.d2;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/17 22:49
 * @desc 尺寸
 */
@Data
@NoArgsConstructor
public class Size {
    public double dx;
    public double dy;

    public Size(double dx, double dy){
        this.dx=dx;
        this.dy=dy;
    }

}
