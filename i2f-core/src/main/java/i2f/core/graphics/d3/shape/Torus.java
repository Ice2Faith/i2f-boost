package i2f.core.graphics.d3.shape;

import i2f.core.graphics.d3.data.D3Model;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/18 0:11
 * @desc 环体
 */
@Data
@NoArgsConstructor
public class Torus {
    public double mainRadius;
    public double subRadius;

    public Torus(double mainRadius, double subRadius) {
        this.mainRadius = mainRadius;
        this.subRadius = subRadius;
    }

    public D3Model makeModel(int mainRadiusCnt,int subRadiusCnt){
        return null;
    }

}
