package i2f.core.graphics.d3;

import i2f.core.graphics.d2.Point;
import i2f.core.graphics.d3.projection.ID3Projection;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/17 22:49
 * @desc 点
 */
@Data
@NoArgsConstructor
public class D3Point {
    public double x;
    public double y;
    public double z;

    public D3Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point projection(ID3Projection proj){
        return proj.projection(this);
    }

    public static D3SphericalPoint point2spherical(D3Point p){
        D3SphericalPoint ret=new D3SphericalPoint();
        ret.radius=new D3Line(new D3Point(0,0,0),p).length();
        ret.aAngle=Math.acos(p.z/ret.radius);
        ret.bAngle=Math.atan(p.y/p.x);
        return ret;
    }

    public D3SphericalPoint spherical(){
        return point2spherical(this);
    }

}
