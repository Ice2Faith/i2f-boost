package i2f.core.graphics.d3;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/18 19:22
 * @desc 三维球坐标系点
 */
@Data
@NoArgsConstructor
public class D3SphericalPoint {
    public double radius;
    public double aAngle;
    public double bAngle;

    public D3SphericalPoint(double radius, double aAngle, double bAngle) {
        this.radius = radius;
        this.aAngle = aAngle;
        this.bAngle = bAngle;
    }

    public static D3Point spherical2point(D3SphericalPoint p){
        D3Point ret=new D3Point();
        ret.y=p.radius*Math.sin(p.aAngle)*Math.sin(p.bAngle);
        ret.z=p.radius*Math.cos(p.aAngle);
        ret.x=p.radius*Math.sin(p.aAngle)*Math.cos(p.bAngle);
        return ret;
    }

    public D3Point point(){
        return spherical2point(this);
    }
}
