package i2f.core.graphics.d3.light;

/**
 * @author ltb
 * @date 2022/6/26 18:46
 * @desc 材质
 */
public class Material {
    // 漫反射光
    public D3Color diff;
    // 镜面反射光
    public D3Color spec;
    // 环境光
    public D3Color ambi;
    // 高光指数
    public double heigN;

    public static Material gold(){
        Material ret=new Material();
        ret.diff=new D3Color(0.752,0.606,0.226);
        ret.spec=new D3Color(0.628,0.556,0.366);
        ret.ambi=new D3Color(0.247,0.200,0.075);
        ret.heigN=50;
        return ret;
    }

    public static Material silver(){
        Material ret=new Material();
        ret.diff=new D3Color(0.508,0.508,0.508);
        ret.spec=new D3Color(0.508,0.508,0.508);
        ret.ambi=new D3Color(0.192,0.192,0.192);
        ret.heigN=50;
        return ret;
    }

    public static Material redGemstone(){
        Material ret=new Material();
        ret.diff=new D3Color(0.614,0.041,0.041);
        ret.spec=new D3Color(0.728,0.527,0.527);
        ret.ambi=new D3Color(0.175,0.012,0.012);
        ret.heigN=30;
        return ret;
    }

    public static Material greenGemstone(){
        Material ret=new Material();
        ret.diff=new D3Color(0.076,0.614,0.075);
        ret.spec=new D3Color(0.633,0.728,0.633);
        ret.ambi=new D3Color(0.022,0.175,0.023);
        ret.heigN=30;
        return ret;
    }

    public static Material blueGemstone(){
        Material ret=new Material();
        ret.diff=new D3Color(0.076,0.075,0.614);
        ret.spec=new D3Color(0.633,0.633,0.728);
        ret.ambi=new D3Color(0.022,0.023,0.175);
        ret.heigN=20;
        return ret;
    }

    public static Material purpleGemstone(){
        Material ret=new Material();
        ret.diff=new D3Color(0.514,0.075,0.614);
        ret.spec=new D3Color(0.628,0.533,0.728);
        ret.ambi=new D3Color(0.075,0.023,0.175);
        ret.heigN=5;
        return ret;
    }

    public static Material stone(){
        Material ret=new Material();
        ret.diff=new D3Color(0.58,0.58,0.58);
        ret.spec=new D3Color(0.58,0.83,0.93);
        ret.ambi=new D3Color(0.02,0.02,0.02);
        ret.heigN=10;
        return ret;
    }

    public static Material snow(){
        Material ret=new Material();
        ret.diff=new D3Color(0.9,0.9,0.9);
        ret.spec=new D3Color(0.4,0.4,0.4);
        ret.ambi=new D3Color(0,0,0);
        ret.heigN=8;
        return ret;
    }
}
