package i2f.core.graphics.d3.shape;

import i2f.core.graphics.d2.Point;
import i2f.core.graphics.d3.D3Point;
import i2f.core.graphics.d3.data.D3Model;
import i2f.core.graphics.math.Calc;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/6/18 0:11
 * @desc 旋转体
 */
@Data
@NoArgsConstructor
public class SpinCube {
    public List<Point> points;

    public SpinCube(List<Point> points) {
        this.points = points;
    }

    public D3Model makeModelSpinX(int angleCount){
        D3Model ret=new D3Model();
        ret.points=new ArrayList<>();
        ret.flats=new ArrayList<>();

        //环形曲面
        for (int i = 0; i < points.size(); i++)
        {
            for (int j = 0; j < angleCount; j++)
            {
                double x = points.get(i).x;
                double y = points.get(i).y*Math.cos(2 * Calc.PI / angleCount* j);
                double z = points.get(i).y*Math.sin(2 * Calc.PI / angleCount* j);
                ret.points.add(new D3Point(x,y,z));
            }
        }


        return ret;
    }

    public D3Model makeModelSpinY(int angleCount){
        D3Model ret=new D3Model();
        ret.points=new ArrayList<>();
        ret.flats=new ArrayList<>();

        //环形曲面
        for (int i = 0; i < points.size(); i++)
        {
            for (int j = 0; j < angleCount; j++)
            {
                double x = points.get(i).x*Math.cos(2 * Calc.PI / angleCount* j);
                double y = points.get(i).y;
                double z = points.get(i).x*Math.sin(2 * Calc.PI / angleCount* j);
                ret.points.add(new D3Point(x,y,z));
            }
        }


        return ret;
    }
}
