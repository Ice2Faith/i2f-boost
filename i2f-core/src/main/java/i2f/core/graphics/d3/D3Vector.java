package i2f.core.graphics.d3;

import i2f.core.graphics.math.Calc;
import i2f.core.graphics.std.ILenght;
import lombok.Data;

/**
 * @author ltb
 * @date 2022/6/18 19:35
 * @desc 三维向量
 */
@Data
public class D3Vector extends D3Point implements ILenght {
    public D3Vector() {
    }

    public D3Vector(double x, double y, double z) {
        super(x, y, z);
    }

    public D3Vector(D3Point p1,D3Point p2){
        super(p2.x-p1.x,p2.y-p1.y,p2.z-p1.z);
    }

    public static D3Vector vector(D3Point p1,D3Point p2){
        D3Vector ret=new D3Vector();
        ret.x=p2.x-p1.x;
        ret.y=p2.y-p1.y;
        ret.z=p2.z-p1.z;
        return ret;
    }

    // 获取向量模长
    @Override
    public double length() {
        return length(this);
    }

    public D3Vector unitization(){
        return unitization(this);
    }

    public D3Vector add(D3Vector v){
        return add(this,v);
    }

    public double mul(D3Vector v){
        return mul(this,v);
    }

    public D3Vector mulCross(D3Vector v){
        return mulCross(this,v);
    }

    public double cosRadian(D3Vector v){
        return cosRadian(this,v);
    }

    public D3Vector normalLine(D3Vector v){
        return normalLine(this,v);
    }

    // 获取向量模长
    public static double length(D3Vector d){
        return Math.sqrt(Calc.squareSum(d.x,d.y,d.z));
    }

    // 单位化
    public static D3Vector unitization(D3Vector d){
        double len=length(d);
        D3Vector ret=new D3Vector();
        ret.x=d.x/len;
        ret.y=d.y/len;
        ret.z=d.z/len;
        return ret;
    }

    // 向量加法
    public static D3Vector add(D3Vector d1,D3Vector d2){
        D3Vector ret=new D3Vector();
        ret.x=d1.x+d2.x;
        ret.y=d1.y+d2.y;
        ret.z=d1.z+d2.z;
        return ret;
    }

    // 向量点乘
    public static double mul(D3Vector d1,D3Vector d2){
        return d1.x*d2.x+d1.y*d2.y+d1.z*d2.z;
    }

    // 向量叉乘
    public static D3Vector mulCross(D3Vector d1,D3Vector d2){
        D3Vector ret=new D3Vector();
        ret.x=d2.z*d1.y-d2.y*d1.z;
        ret.y=d2.x*d1.z-d2.z*d1.x;
        ret.z=d2.y*d1.x-d2.x*d1.y;
        return ret;
    }

    // 向量夹角
    public static double cosRadian(D3Vector d1,D3Vector d2){
        return mul(d1,d2)/(length(d1)*length(d2));
    }

    // 法向量
    public static D3Vector normalLine(D3Vector d1,D3Vector d2){
        return mulCross(d1,d2);
    }
}
