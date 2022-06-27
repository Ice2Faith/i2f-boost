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

    // 金
    public static Material gold(){
        Material ret=new Material();
        ret.diff=new D3Color(0.752,0.606,0.226);
        ret.spec=new D3Color(0.628,0.556,0.366);
        ret.ambi=new D3Color(0.247,0.200,0.075);
        ret.heigN=50;
        return ret;
    }

    // 银
    public static Material silver(){
        Material ret=new Material();
        ret.diff=new D3Color(0.508,0.508,0.508);
        ret.spec=new D3Color(0.508,0.508,0.508);
        ret.ambi=new D3Color(0.192,0.192,0.192);
        ret.heigN=50;
        return ret;
    }

    // 红宝石
    public static Material redGemstone(){
        Material ret=new Material();
        ret.diff=new D3Color(0.614,0.041,0.041);
        ret.spec=new D3Color(0.728,0.527,0.527);
        ret.ambi=new D3Color(0.175,0.012,0.012);
        ret.heigN=30;
        return ret;
    }

    // 绿宝石
    public static Material greenGemstone(){
        Material ret=new Material();
        ret.diff=new D3Color(0.076,0.614,0.075);
        ret.spec=new D3Color(0.633,0.728,0.633);
        ret.ambi=new D3Color(0.022,0.175,0.023);
        ret.heigN=30;
        return ret;
    }

    // 蓝宝石
    public static Material blueGemstone(){
        Material ret=new Material();
        ret.diff=new D3Color(0.076,0.075,0.614);
        ret.spec=new D3Color(0.633,0.633,0.728);
        ret.ambi=new D3Color(0.022,0.023,0.175);
        ret.heigN=20;
        return ret;
    }

    // 紫水晶
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
        ret.diff=new D3Color(0.65,0.65,0.65);
        ret.spec=new D3Color(0.90,0.90,0.90);
        ret.ambi=new D3Color(0,0,0);
        ret.heigN=0.25*128;
        return ret;
    }


    ///////////////////
    // http://devernay.free.fr/cours/opengl/materials.html
    // 翠
    public static Material emerald(){
        Material ret=new Material();
        ret.diff=new D3Color(0.07568,0.61424,0.07568);
        ret.spec=new D3Color(0.633,0.727811,0.633);
        ret.ambi=new D3Color(0.0215,0.1745,0.0215);
        ret.heigN=0.6*128;
        return ret;
    }

    // 玉
    public static Material jade(){
        Material ret=new Material();
        ret.diff=new D3Color(0.54,0.89,0.63);
        ret.spec=new D3Color(0.316228,0.316228,0.316228);
        ret.ambi=new D3Color(0.135,0.2225,0.1575);
        ret.heigN=0.1*128;
        return ret;
    }

    // 黑曜石
    public static Material obsidian(){
        Material ret=new Material();
        ret.diff=new D3Color(0.18275,0.17,0.22525);
        ret.spec=new D3Color(0.332741,0.328634,0.346435);
        ret.ambi=new D3Color(0.05375,0.05,0.06625);
        ret.heigN=0.3*128;
        return ret;
    }

    // 珍珠
    public static Material pearl(){
        Material ret=new Material();
        ret.diff=new D3Color(1,0.829,0.829);
        ret.spec=new D3Color(0.296648,0.296648,0.296648);
        ret.ambi=new D3Color(0.25,0.20725,0.20725);
        ret.heigN=0.088*128;
        return ret;
    }

    // 红宝石
    public static Material ruby(){
        Material ret=new Material();
        ret.diff=new D3Color(0.61424,0.04136,0.04136);
        ret.spec=new D3Color(0.727811,0.626959,0.626959);
        ret.ambi=new D3Color(0.1745,0.01175,0.01175);
        ret.heigN=0.6*128;
        return ret;
    }

    // 绿松石
    public static Material turquoise(){
        Material ret=new Material();
        ret.diff=new D3Color(0.396,0.74151,0.69102);
        ret.spec=new D3Color(0.297254,0.30829,0.306678);
        ret.ambi=new D3Color(0.1,0.18725,0.1745);
        ret.heigN=0.1*128;
        return ret;
    }

    // 黄铜
    public static Material brass(){
        Material ret=new Material();
        ret.diff=new D3Color(0.780392,0.568627,0.113725);
        ret.spec=new D3Color(0.992157,0.941176,0.807843);
        ret.ambi=new D3Color(0.329412,0.223529,0.02745);
        ret.heigN=0.21794872*128;
        return ret;
    }

    // 青铜
    public static Material bronze(){
        Material ret=new Material();
        ret.diff=new D3Color(0.714,0.4284,0.18144);
        ret.spec=new D3Color(0.393548,0.271906,0.166721);
        ret.ambi=new D3Color(0.2125,0.1275,0.054);
        ret.heigN=0.2*128;
        return ret;
    }

    // 铬合金
    public static Material chrome(){
        Material ret=new Material();
        ret.diff=new D3Color(0.4,0.4,0.4);
        ret.spec=new D3Color(0.774597,0.774597,0.774597);
        ret.ambi=new D3Color(0.25,0.25,0.25);
        ret.heigN=0.6*128;
        return ret;
    }

    // 铜
    public static Material copper(){
        Material ret=new Material();
        ret.diff=new D3Color(0.7038,0.27048,0.0828);
        ret.spec=new D3Color(0.256777,0.137622,0.086014);
        ret.ambi=new D3Color(0.19125,0.0735,0.0225);
        ret.heigN=0.1*128;
        return ret;
    }

    // 金子
    public static Material glGold(){
        Material ret=new Material();
        ret.diff=new D3Color(0.75164,0.60648,0.22648);
        ret.spec=new D3Color(0.628281,0.555802,0.366065);
        ret.ambi=new D3Color(0.24725,0.1995,0.0745);
        ret.heigN=0.4*128;
        return ret;
    }

    // 银
    public static Material glSilver(){
        Material ret=new Material();
        ret.diff=new D3Color(0.50754,0.50754,0.50754);
        ret.spec=new D3Color(0.508273,0.508273,0.508273);
        ret.ambi=new D3Color(0.19225,0.19225,0.19225);
        ret.heigN=0.4*128;
        return ret;
    }

    // 黑色塑料
    public static Material blackPlastic(){
        Material ret=new Material();
        ret.diff=new D3Color(0.01,0.01,0.01);
        ret.spec=new D3Color(0.50,0.50,0.50);
        ret.ambi=new D3Color(0.0,0.0,0.0);
        ret.heigN=0.25*128;
        return ret;
    }

    // 青色塑料
    public static Material cyanPlastic(){
        Material ret=new Material();
        ret.diff=new D3Color(0.0,0.50980392,0.50980392);
        ret.spec=new D3Color(0.50196078,0.50196078,0.50196078);
        ret.ambi=new D3Color(0.0,0.1,0.06);
        ret.heigN=0.25*128;
        return ret;
    }

    // 绿色塑料
    public static Material greenPlastic(){
        Material ret=new Material();
        ret.diff=new D3Color(0.1,0.35,0.1);
        ret.spec=new D3Color(0.45,0.55,0.45);
        ret.ambi=new D3Color(0.0,0.0,0.0);
        ret.heigN=0.25*128;
        return ret;
    }

    // 红色塑料
    public static Material redPlastic(){
        Material ret=new Material();
        ret.diff=new D3Color(0.5,0.0,0.0);
        ret.spec=new D3Color(0.7,0.6,0.6);
        ret.ambi=new D3Color(0.0,0.0,0.0);
        ret.heigN=0.25*128;
        return ret;
    }

    // 白色塑料
    public static Material whitePlastic(){
        Material ret=new Material();
        ret.diff=new D3Color(0.55,0.55,0.55);
        ret.spec=new D3Color(0.70,0.70,0.70);
        ret.ambi=new D3Color(0.0,0.0,0.0);
        ret.heigN=0.25*128;
        return ret;
    }

    // 黄色塑料
    public static Material yellowPlastic(){
        Material ret=new Material();
        ret.diff=new D3Color(0.5,0.5,0.0);
        ret.spec=new D3Color(0.60,0.60,0.50);
        ret.ambi=new D3Color(0.0,0.0,0.0);
        ret.heigN=0.25*128;
        return ret;
    }

    // 黑色橡胶
    public static Material blackRubber(){
        Material ret=new Material();
        ret.diff=new D3Color(0.01,0.01,0.01);
        ret.spec=new D3Color(0.4,0.4,0.4);
        ret.ambi=new D3Color(0.02,0.02,0.02);
        ret.heigN=0.078125*128;
        return ret;
    }

    // 青色橡胶
    public static Material cyanRubber(){
        Material ret=new Material();
        ret.diff=new D3Color(0.4,0.5,0.5);
        ret.spec=new D3Color(0.04,0.7,0.7);
        ret.ambi=new D3Color(0.0,0.05,0.05);
        ret.heigN=0.078125*128;
        return ret;
    }

    // 绿色橡胶
    public static Material greenRubber(){
        Material ret=new Material();
        ret.diff=new D3Color(0.4,0.5,0.4);
        ret.spec=new D3Color(0.04,0.7,0.04);
        ret.ambi=new D3Color(0.0,0.05,0.0);
        ret.heigN=0.078125*128;
        return ret;
    }

    // 红色橡胶
    public static Material redRubber(){
        Material ret=new Material();
        ret.diff=new D3Color(0.5,0.4,0.4);
        ret.spec=new D3Color(0.7,0.04,0.04);
        ret.ambi=new D3Color(0.05,0.0,0.0);
        ret.heigN=0.078125*128;
        return ret;
    }

    // 白色橡胶
    public static Material whiteRubber(){
        Material ret=new Material();
        ret.diff=new D3Color(0.5,0.5,0.5);
        ret.spec=new D3Color(0.7,0.7,0.7);
        ret.ambi=new D3Color(0.05,0.05,0.05);
        ret.heigN=0.078125*128;
        return ret;
    }

    // 黄色橡胶
    public static Material yellowRubber(){
        Material ret=new Material();
        ret.diff=new D3Color(0.5,0.5,0.4);
        ret.spec=new D3Color(0.7,0.7,0.047);
        ret.ambi=new D3Color(0.05,0.05,0.0);
        ret.heigN=0.078125*128;
        return ret;
    }
}
