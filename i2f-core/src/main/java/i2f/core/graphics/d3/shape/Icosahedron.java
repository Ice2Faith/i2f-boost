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
 * @desc 二十面体
 */
@Data
@NoArgsConstructor
public class Icosahedron {

    public D3Model makeModel(double a){
        D3Model ret=new D3Model();
        ret.points=new ArrayList<>();
        ret.flats=new ArrayList<>();
        double b = a*0.61828;
        double[][] points={
                { 0,  a,  b},
                { 0,  b,  -b},
                { a,  b,  0},
                { a,  -b,  0},
                { 0,  -a,  -b},
                { 0,  -a,  b},
                { b,  0,  a},
                { -b,  0,  a},
                { b,  0,  -a},
                { -b,  0,  -a},
                { -a,  b,  0},
                { -a,  -b,  0}
        };
        int[][] flats={
                { 0,  6,  2},
                { 2,  6,  3},
                { 3,  6,  5},
                { 5,  6,  7},
                { 0,  7,  6},
                { 2,  3,  8},
                { 1,  2,  8},
                { 0,  2,  1},

                { 0,  1,  10},
                { 1,  9,  10},
                { 1,  8,  9},
                { 3,  4,  8},
                { 3,  5,  4},
                { 4,  5,  11},
                { 7,  10,  11},
                { 0,  10,  7},

                { 4,  11,  9},
                { 4,  9,  8},
                { 5,  7,  11},
                { 9,  11,  10}
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
