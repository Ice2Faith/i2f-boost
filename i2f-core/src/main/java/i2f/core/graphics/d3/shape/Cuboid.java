package i2f.core.graphics.d3.shape;

import i2f.core.graphics.d3.data.D3Model;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/18 0:11
 * @desc 长方体
 */
@Data
@NoArgsConstructor
public class Cuboid {
    public double dx;
    public double dy;
    public double dz;

    public Cuboid(double dx, double dy, double dz) {
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }

    public D3Model makeModel(int xcnt, int ycnt, int zcnt){
        return null;
    }
}
