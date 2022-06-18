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
 * @desc 六面体
 */
@Data
@NoArgsConstructor
public class Hexahedron {

    public D3Model makeModel(double a){
        D3Model ret=new D3Model();
        ret.points=new ArrayList<>();
        ret.flats=new ArrayList<>();

        double[][] points={
                {a,-a,-a},
                {a,a,-a},
                {-a,a,-a},
                {-a,-a,-a},
                {a,-a,a},
                {a,a,a},
                {-a,a,a},
                {-a,-a,a}
        };
        int[][] flats={
                {0,1,2},
                {0,2,3},
                {0,1,5},
                {0,5,4},
                {0,3,7},
                {0,7,4},
                {6,5,1},
                {6,1,2},
                {6,7,4},
                {6,4,5},
                {6,2,3},
                {6,3,7}
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
