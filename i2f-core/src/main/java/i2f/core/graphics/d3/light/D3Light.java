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

    public D3Point point=new D3Point(500,500,500);

    // 是否开启灯光
    public boolean enable=true;

    public static D3Light gold(D3Point point){
        D3Light ret=new D3Light();
        ret.diff=new D3Color(0.752,0.606,0.226);
        ret.spec=new D3Color(0.628,0.556,0.366);
        ret.c0=0.5;
        ret.c1=0.8;
        ret.c2=0.99;
        ret.point=point;
        return ret;
    }

    public static D3Light silver(D3Point point){
        D3Light ret=new D3Light();
        ret.diff=new D3Color(0.508,0.508,0.508);
        ret.spec=new D3Color(0.508,0.508,0.508);
        ret.c0=0.5;
        ret.c1=0.8;
        ret.c2=0.99;
        ret.point=point;
        return ret;
    }

    public static D3Light redGemstone(D3Point point){
        D3Light ret=new D3Light();
        ret.diff=new D3Color(0.614,0.041,0.041);
        ret.spec=new D3Color(0.728,0.527,0.527);
        ret.c0=0.5;
        ret.c1=0.8;
        ret.c2=0.99;
        ret.point=point;
        return ret;
    }

    public static D3Light greenGemstone(D3Point point){
        D3Light ret=new D3Light();
        ret.diff=new D3Color(0.076,0.614,0.075);
        ret.spec=new D3Color(0.633,0.728,0.633);
        ret.c0=0.5;
        ret.c1=0.8;
        ret.c2=0.99;
        ret.point=point;
        return ret;
    }

    public static D3Light blueGemstone(D3Point point){
        D3Light ret=new D3Light();
        ret.diff=new D3Color(0.076,0.075,0.614);
        ret.spec=new D3Color(0.633,0.633,0.728);
        ret.c0=0.5;
        ret.c1=0.8;
        ret.c2=0.99;
        ret.point=point;
        return ret;
    }

    public static D3Light purpleGemstone(D3Point point){
        D3Light ret=new D3Light();
        ret.diff=new D3Color(0.514,0.075,0.614);
        ret.spec=new D3Color(0.628,0.533,0.728);
        ret.c0=0.5;
        ret.c1=0.8;
        ret.c2=0.99;
        ret.point=point;
        return ret;
    }

    public static D3Light moon(D3Point point){
        D3Light ret=new D3Light();
        ret.diff=new D3Color(0.93,0.94,0.95);
        ret.spec=new D3Color(0.999,0.98,0.97);
        ret.c0=0.5;
        ret.c1=0.8;
        ret.c2=0.99;
        ret.point=point;
        return ret;
    }

    public static D3Light sun(D3Point point){
        D3Light ret=new D3Light();
        ret.diff=new D3Color(0.92,0.92,0.85);
        ret.spec=new D3Color(0.99,0.99,0.50);
        ret.c0=0.8;
        ret.c1=0.9;
        ret.c2=0.99;
        ret.point=point;
        return ret;
    }

    public static D3Light white(D3Point point){
        D3Light ret=new D3Light();
        ret.diff=new D3Color(0.8,0.8,0.8);
        ret.spec=new D3Color(0.2,0.2,0.2);
        ret.c0=0.5;
        ret.c1=0.8;
        ret.c2=0.99;
        ret.point=point;
        return ret;
    }
}
