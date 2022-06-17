package i2f.core.graphics.d2;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/17 23:26
 * @desc 描述二维空间范围
 */
@Data
@NoArgsConstructor
public class Scope {
    public Point point;
    public Size size;

    public Scope(Point point, Size size){
        this.point=point;
        this.size=size;
    }
}
