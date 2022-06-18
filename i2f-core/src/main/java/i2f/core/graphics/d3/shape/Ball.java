package i2f.core.graphics.d3.shape;

import i2f.core.graphics.d3.D3Point;
import i2f.core.graphics.d3.data.D3Model;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/18 0:11
 * @desc 球体
 */
@Data
@NoArgsConstructor
public class Ball {
    public double radius;

    public Ball(D3Point center, double radius) {
        this.radius = radius;
    }

    public D3Model makeModel(int aAngleCnt,int bAngleCnt){
        return null;
    }

    public D3Model makeModel(int level){
        return null;
    }
}
