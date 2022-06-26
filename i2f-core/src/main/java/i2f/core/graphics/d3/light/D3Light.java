package i2f.core.graphics.d3.light;

import i2f.core.graphics.d3.D3Point;

/**
 * @author ltb
 * @date 2022/6/26 18:49
 * @desc 灯光
 */
public class D3Light {
    // 漫反射光
    public D3Color diff;
    // 镜面反射光
    public D3Color spec;

    // 衰减因子
    public double c0;
    public double c1;
    public double c2;

    public D3Point point;

    public static D3Light gold(){
        D3Light ret=new D3Light();
        ret.diff=new D3Color(0.752,0.606,0.226);
        ret.spec=new D3Color(0.628,0.556,0.366);
        ret.c0=0.5;
        ret.c1=0.8;
        ret.c2=0.99;
        ret.point=new D3Point(500,500,500);
        return ret;
    }

    public static D3Light silver(){
        D3Light ret=new D3Light();
        ret.diff=new D3Color(0.508,0.508,0.508);
        ret.spec=new D3Color(0.508,0.508,0.508);
        ret.c0=0.5;
        ret.c1=0.8;
        ret.c2=0.99;
        ret.point=new D3Point(500,500,500);
        return ret;
    }

    public static D3Light moon(){
        D3Light ret=new D3Light();
        ret.diff=new D3Color(0.93,0.94,0.95);
        ret.spec=new D3Color(0.999,0.98,0.97);
        ret.c0=0.5;
        ret.c1=0.8;
        ret.c2=0.99;
        ret.point=new D3Point(500,500,5000);
        return ret;
    }

    public static D3Light sun(){
        D3Light ret=new D3Light();
        ret.diff=new D3Color(0.92,0.92,0.85);
        ret.spec=new D3Color(0.99,0.99,0.50);
        ret.c0=0.8;
        ret.c1=0.9;
        ret.c2=0.99;
        ret.point=new D3Point(500,500,5000);
        return ret;
    }

    public static D3Light white(){
        D3Light ret=new D3Light();
        ret.diff=new D3Color(0.75,0.75,0.75);
        ret.spec=new D3Color(0.99,0.99,0.99);
        ret.c0=0.3;
        ret.c1=0.6;
        ret.c2=0.99;
        ret.point=new D3Point(500,500,5000);
        return ret;
    }
}
