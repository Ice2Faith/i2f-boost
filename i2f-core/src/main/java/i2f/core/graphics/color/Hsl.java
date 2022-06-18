package i2f.core.graphics.color;

import i2f.core.graphics.math.Calc;

/**
 * @author ltb
 * @date 2022/6/18 14:47
 * @desc 定义Hsl色彩空间
 * 色相（H,hue）：在0~360°的标准色轮上，色相是按位置度量的。在通常的使用中，色相是由颜色名称标识的，比如红、绿或橙色。黑色和白色无色相。
 * 饱和度（S,saturation）：表示色彩的纯度，为0时为灰色。白、黑和其他灰色色彩都没有饱和度的。在最大饱和度时，每一色相具有最纯的色光。取值范围0～100%。
 * 亮度（B,brightness或V,value或L,Lightness）：是色彩的明亮度。为0时即为黑色。最大亮度是色彩最鲜明的状态。取值范围0～100%。
 */
public class Hsl {
    public double h;
    public double s;
    public double l;
    public int a;

    public Hsl() {

    }

    public Hsl(double h, double s, double l, int a) {
        this.h = stdHsl(h);
        this.s = stdHsl(s);
        this.l = stdHsl(l);
        this.a = a;
    }

    public static Hsl hsl(double h, double s, double l){
        return new Hsl(h,s,l,255);
    }
    public static Hsl hsla(double h, double s, double l,int a){
        return new Hsl(h,s,l,a);
    }


    public Rgba rgba(){
        return hsl2rgb(this);
    }

    public static double stdHsl(double val) {
        return Calc.between(val, 0, 1);
    }

    public static Hsl rgb2hsl(Rgba rgb) {
        double h = 0, l = 0, s = 0;
        double r, g, b, max, min, delR, delG, delB, delMax;
        r = rgb.r / 255.0;       //Where RGB values = 0 ÷ 255
        g = rgb.g / 255.0;
        b = rgb.b / 255.0;

        min = Math.min(r, Math.min(g, b));    //min. value of RGB
        max = Math.max(r, Math.max(g, b));    //max. value of RGB
        delMax = max - min;        //Delta RGB value

        l = (max + min) / 2.0;

        if (delMax == 0)           //This is a gray, no chroma...
        {
            //h = 2.0/3.0;          //Windows下S值为0时，H值始终为160（2/3*240）
            h = 0;                  //HSL results = 0 ÷ 1
            s = 0;
        } else                        //Chromatic data...
        {
            if (l < 0.5) {
                s = delMax / (max + min);
            } else {
                s = delMax / (2 - max - min);
            }

            delR = (((max - r) / 6.0) + (delMax / 2.0)) / delMax;
            delG = (((max - g) / 6.0) + (delMax / 2.0)) / delMax;
            delB = (((max - b) / 6.0) + (delMax / 2.0)) / delMax;

            if (r == max) {
                h = delB - delG;
            } else if (g == max) {
                h = (1.0 / 3.0) + delR - delB;
            } else if (b == max) {
                h = (2.0 / 3.0) + delG - delR;
            }

            if (h < 0) {
                h += 1;
            }
            if (h > 1) {
                h -= 1;
            }
        }
        return new Hsl(h, s, l, rgb.a);
    }

    public static Rgba hsl2rgb(Hsl hsl) {
        double h = hsl.h, s = hsl.s, l = hsl.l;
        double r, g, b;
        double var1, var2;
        if (s == 0)                       //HSL values = 0 ÷ 1
        {
            r = l * 255.0;                   //RGB results = 0 ÷ 255
            g = l * 255.0;
            b = l * 255.0;
        } else {
            if (l < 0.5) {
                var2 = l * (1 + s);
            } else {
                var2 = (l + s) - (s * l);
            }

            var1 = 2.0 * l - var2;

            r = 255.0 * hue2rgb(var1, var2, h + (1.0 / 3.0));
            g = 255.0 * hue2rgb(var1, var2, h);
            b = 255.0 * hue2rgb(var1, var2, h - (1.0 / 3.0));
        }
        r = Calc.between(r, 0, 255);
        g = Calc.between(g, 0, 255);
        r = Calc.between(r, 0, 255);
        return Rgba.rgba((int) r, (int) g, (int) b, hsl.a);
    }

    public static double hue2rgb(double v1, double v2, double vH) {
        if (vH < 0) {
            vH += 1;
        }
        if (vH > 1) {
            vH -= 1;
        }
        if (6.0 * vH < 1) {
            return v1 + (v2 - v1) * 6.0 * vH;
        }
        if (2.0 * vH < 1) {
            return v2;
        }
        if (3.0 * vH < 2) {
            return v1 + (v2 - v1) * ((2.0 / 3.0) - vH) * 6.0;
        }
        return (v1);
    }
}
