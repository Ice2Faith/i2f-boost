package i2f.core.graphics.d3.shape;

import i2f.core.graphics.d3.D3Point;
import i2f.core.graphics.d3.data.D3Model;
import i2f.core.graphics.math.Calc;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/**
 * @author ltb
 * @date 2022/6/18 0:11
 * @desc 环体
 */
@Data
@NoArgsConstructor
public class Torus {
    public double r1;
    public double r2;

    public Torus(double r1, double r2) {
        this.r1 = r1;
        this.r2 = r2;
    }

    public D3Model makeModel(int aAngleCount,int bAngleCount){
        D3Model ret=new D3Model();
        ret.points=new ArrayList<>();
        ret.flats=new ArrayList<>();

        for (int i = 0; i < aAngleCount; i++)
        {
            for (int j = 0; j < bAngleCount; j++)
            {
                double x = (r1 + r2*Math.sin(2 * Calc.PI / bAngleCount*j))*Math.sin(2 * Calc.PI / aAngleCount*i);
                double y= (r1 + r2*Math.sin(2 * Calc.PI / bAngleCount*j))*Math.cos(2 * Calc.PI / aAngleCount*i);
                double z = r2*Math.cos(2 * Calc.PI / bAngleCount*j);
                ret.points.add(new D3Point(x,y,z));
            }
        }

        return ret;
    }

}
