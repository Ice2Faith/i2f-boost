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
 * @desc 十二面体
 */
@Data
@NoArgsConstructor
public class Dodecahedron {

    public D3Model makeModel(double a){
        D3Model ret=new D3Model();
        ret.points=new ArrayList<>();
        ret.flats=new ArrayList<>();
        double b = a*0.61828;
        double[][] points={
                {a,      a,       a},
                {a + b,  0,       b},
                {a,      -a,      a},
                {0,      -b,      a + b},
                {0,      b,       a + b},
                {a + b,  0,       -b},
                {a,      a,       -a},
                {b,      a + b,   0},
                {-b,     a + b,   0},
                {-a,     a,       -a},
                {0,       b,       -a - b},
                {a,       -a,      -a},
                {b,       -a - b,  0},
                {-b,      -a - b,  0},
                {-a - b,  0,       b},
                {-a,      a,       a},
                {-a,      -a,      -a},
                {0,       -b,      -a - b},
                {-a,      -a,      a},
                {-a - b,  0,       -b}
        };
        int[][] flats={
                { 0,  7,  8},
                { 0,  8,  15},
                { 0,  15,  4},
                { 6,  10,  9},
                { 6,  9,  8},
                { 6,  8,  7},
                { 1,  5,  6},
                { 1,  6,  7},
                { 1,  7,  0},
                { 1,  2,  12},
                { 1,  12,  11},
                { 1,  11,  5},
                { 11,  12,  13},
                { 11,  13,  16},
                { 11,  16,  17},
                { 2,  3,  18},
                { 2,  18,  13},
                { 2,  13,  12},
                { 0,  4,  3},
                { 0,  3,  2},
                { 0,  2,  1},
                { 3,  4,  15},
                { 3,  15,  14},
                { 3,  14,  18},
                { 5,  11,  17},
                { 5,  17,  10},
                { 5,  10,  6},
                { 9,  10,  17},
                { 9,  17,  16},
                { 9,  16,  19},
                { 8,  9,  19},
                { 8,  19,  14},
                { 8,  14,  15},
                { 13,  18,  14},
                { 13,  14,  19},
                { 13,  19,  16}
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
