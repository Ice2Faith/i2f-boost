package i2f.core.graphics.d2;

import i2f.core.graphics.math.Calc;
import i2f.core.graphics.std.ILenght;
import lombok.Data;

/**
 * @author ltb
 * @date 2022/6/18 19:35
 * @desc 二维向量
 */
@Data
public class Vector extends Point implements ILenght {
    public Vector() {
    }

    public Vector(double x, double y) {
        super(x, y);
    }

    public Vector(Point p1, Point p2){
        super(p2.x-p1.x,p2.y-p1.y);
    }

    public static Vector vector(Point p1, Point p2){
        Vector ret=new Vector();
        ret.x=p2.x-p1.x;
        ret.y=p2.y-p1.y;
        return ret;
    }

    // 获取向量模长
    @Override
    public double length() {
        return length(this);
    }

    public Vector unitization(){
        return unitization(this);
    }

    public Vector add(Vector v){
        return add(this,v);
    }

    public double mul(Vector v){
        return mul(this,v);
    }

    public double cosRadian(Vector v){
        return cosRadian(this,v);
    }

    // 获取向量模长
    public static double length(Vector d){
        return Math.sqrt(Calc.squareSum(d.x,d.y));
    }

    // 单位化
    public static Vector unitization(Vector d){
        double len=length(d);
        Vector ret=new Vector();
        ret.x=d.x/len;
        ret.y=d.y/len;
        return ret;
    }

    // 向量加法
    public static Vector add(Vector d1, Vector d2){
        Vector ret=new Vector();
        ret.x=d1.x+d2.x;
        ret.y=d1.y+d2.y;
        return ret;
    }

    // 向量点乘
    public static double mul(Vector d1, Vector d2){
        return d1.x*d2.x+d1.y*d2.y;
    }


    // 向量夹角
    public static double cosRadian(Vector d1, Vector d2){
        return mul(d1,d2)/(length(d1)*length(d2));
    }

}
