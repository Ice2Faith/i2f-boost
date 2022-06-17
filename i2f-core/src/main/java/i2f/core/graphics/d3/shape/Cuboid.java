package i2f.core.graphics.d3.shape;

import i2f.core.graphics.d3.D3Point;
import i2f.core.graphics.d3.D3Scope;
import i2f.core.graphics.d3.D3Size;
import lombok.Data;

/**
 * @author ltb
 * @date 2022/6/18 0:11
 * @desc 长方体
 */
@Data
public class Cuboid extends D3Scope {
    public Cuboid() {
    }

    public Cuboid(D3Point point, D3Size size) {
        super(point, size);
    }
}
