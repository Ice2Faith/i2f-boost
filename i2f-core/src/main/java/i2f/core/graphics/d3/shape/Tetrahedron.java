package i2f.core.graphics.d3.shape;

import i2f.core.graphics.d3.D3Point;
import i2f.core.graphics.d3.data.D3Model;
import i2f.core.graphics.d3.data.D3ModelFlat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/**
 * @author ltb
 * @date 2022/6/18 22:27
 * @desc 四面体
 */
@Data
@NoArgsConstructor
public class Tetrahedron {

    public D3Model makeModel(double a){
        D3Model ret=new D3Model();
        ret.points=new ArrayList<>();
        ret.flats=new ArrayList<>();

        double[][] points={
                {a,a,a},
                {a,-a,-a},
                {-a,-a,a},
                {-a,a,-a}
        };
        int[][] flats={
                {1,2,3},
                {0,3,2},
                {0,1,3},
                {0,2,1}
        };

        for (int i = 0; i < points.length; i++) {
            ret.points.add(new D3Point(points[i][0],points[i][1],points[i][2]));
        }

        for (int i = 0; i < flats.length; i++) {
            ret.flats.add(new D3ModelFlat(flats[i][0],flats[i][1],flats[i][2]));
        }

        return ret;
    }
}
