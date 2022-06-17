package i2f.core.graphics.d3;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/17 22:49
 * @desc 尺寸
 */
@Data
@NoArgsConstructor
public class D3Size {
    public double dx;
    public double dy;
    public double dz;

    public D3Size(double dx, double dy,double dz){
        this.dx=dx;
        this.dy=dy;
        this.dz=dz;
    }

}
