package i2f.core.graphics.d3;

import i2f.core.graphics.d2.Point;
import i2f.core.graphics.d3.projection.ID3Projection;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/17 22:49
 * @desc 点
 */
@Data
@NoArgsConstructor
public class D3Point {
    public double x;
    public double y;
    public double z;

    public D3Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point projection(ID3Projection proj){
        return proj.projection(this);
    }
}
