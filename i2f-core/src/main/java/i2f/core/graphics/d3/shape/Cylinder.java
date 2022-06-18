package i2f.core.graphics.d3.shape;

import i2f.core.graphics.d3.data.D3Model;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/18 0:11
 * @desc 圆柱
 */
@Data
@NoArgsConstructor
public class Cylinder {
    public double radius;
    public double height;

    public Cylinder(double radius, double height) {
        this.radius = radius;
        this.height = height;
    }

    public D3Model makeModel(int heightCnt,int angleCnt,int radiusCnt){
        return null;
    }

}
