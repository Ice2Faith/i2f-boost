package i2f.core.graphics.d3.shape;

import i2f.core.graphics.d3.D3Point;
import i2f.core.graphics.d3.data.D3Model;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/**
 * @author ltb
 * @date 2022/6/18 0:11
 * @desc 长方体
 */
@Data
@NoArgsConstructor
public class Cuboid {
    public double dx;
    public double dy;
    public double dz;

    public Cuboid(double dx, double dy, double dz) {
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }

    public D3Model makeModel(int xcnt, int ycnt, int zcnt){
        D3Model ret=new D3Model();
        ret.points=new ArrayList<>();
        ret.flats=new ArrayList<>();

        for(int i=0;i<=dx;i+=(dx/xcnt)){
            for(int j=0;j<=dy;j+=(dy/ycnt)){
                ret.points.add(new D3Point(i,j,0));
                ret.points.add(new D3Point(i,j,dz));
            }
        }

        for(int i=0;i<dx;i+=(dx/xcnt)){
            for(int j=0;j<dz;j+=(dz/zcnt)){
                ret.points.add(new D3Point(i,0,j));
                ret.points.add(new D3Point(i,dy,j));
            }
        }

        for(int i=0;i<dy;i+=(dy/ycnt)){
            for(int j=0;j<dz;j+=(dz/zcnt)){
                ret.points.add(new D3Point(0,i,j));
                ret.points.add(new D3Point(dx,i,j));
            }
        }

        return ret;
    }
}
