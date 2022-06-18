package i2f.core.graphics.d3;

import i2f.core.graphics.d2.Point;
import i2f.core.graphics.d2.shape.Polygon;
import i2f.core.graphics.d3.projection.IProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


/**
 * @author ltb
 * @date 2022/6/17 22:49
 * @desc 平面
 */
@Data
@NoArgsConstructor
public class D3Flat {
    public D3Point loc1;
    public D3Point loc2;
    public D3Point loc3;

    public D3Flat(D3Point loc1, D3Point loc2, D3Point loc3) {
        this.loc1 = loc1;
        this.loc2 = loc2;
        this.loc3 = loc3;
    }

    public Polygon projection(IProjection proj){
        List<Point> points=new ArrayList<>();
        points.add(loc1.projection(proj));
        points.add(loc2.projection(proj));
        points.add(loc3.projection(proj));
        return new Polygon(points);
    }
}
