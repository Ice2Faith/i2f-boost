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

    public D3Model makeModel(int heightCount,int angleCount,int radiusCount){
        D3Model ret=new D3Model();
        ret.points=new ArrayList<>();
        ret.flats=new ArrayList<>();
        //环形曲面
        for (int i = 0; i < heightCount; i++)
        {
            for (int j = 0; j < angleCount; j++)
            {
                double x = radius*Math.cos(2 * Calc.PI / angleCount* j);
                double y = radius*Math.sin(2 * Calc.PI / angleCount*j);
                double z = height / heightCount*i;
                ret.points.add(new D3Point(x,y,z));
            }
        }
        //下底面
        for (int i = 0; i < radiusCount; i++)
        {
            for (int j = 0; j < angleCount; j++)
            {
                double x = (radius / radiusCount*i)*Math.cos(2 * Calc.PI / angleCount* j);
                double y = (radius / radiusCount*i)*Math.sin(2 * Calc.PI / angleCount*j);
                double z = 0;
                ret.points.add(new D3Point(x,y,z));
            }
        }
        //上底面
        for (int i = 0; i < radiusCount; i++)
        {
            for (int j = 0; j < angleCount; j++)
            {
                double x = (radius / radiusCount*i)*Math.cos(2 * Calc.PI / angleCount* j);
                double y = (radius / radiusCount*i)*Math.sin(2 * Calc.PI / angleCount*j);
                double z = height;
                ret.points.add(new D3Point(x,y,z));
            }
        }
        return ret;
    }

}
