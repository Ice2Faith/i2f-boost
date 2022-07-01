package i2f.core.graphics.color;

import i2f.core.graphics.math.Calc;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/17 23:13
 * @desc 定义RGBA色彩
 */
@Data
@NoArgsConstructor
public class Rgba {
    public int r;
    public int g;
    public int b;
    public int a=255;
    public Rgba(int r,int g,int b){
        this.r=r;
        this.g=g;
        this.b=b;
    }
    public Rgba(int r,int g,int b,int a){
        this.r=r;
        this.g=g;
        this.b=b;
        this.a=a;
    }
    public static int stdRgba(int val){
        return (int)Calc.between(val,0,255);
    }
    public static Rgba rgb(int r,int g,int b){
        return new Rgba(r,g,b,255);
    }
    public static Rgba rgba(int r,int g,int b,int a){
        return new Rgba(r,g,b,a);
    }
    public static Rgba rgba(int c){
        int r=(c>>>24)&0x0ff;
        int g=(c>>16)&0x0ff;
        int b=(c>>8)&0x0ff;
        int a=(c&0x0ff);
        return new Rgba(r,g,b,a);
    }
    public static Rgba argb(int c){
        int a=(c>>>24)&0x0ff;
        int r=(c>>16)&0x0ff;
        int g=(c>>8)&0x0ff;
        int b=c&0x0ff;
        return new Rgba(r,g,b,a);
    }
    public int rgba(){
        int c=((r&0x0ff)<<24)|((g&0x0ff)<<16)|((b&0x0ff)<<8)|(a&0x0ff);
        return c;
    }
    public int argb(){
        int c=((a&0x0ff)<<24)|((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff);
        return c;
    }
    public Hsl hsl(){
        return Hsl.rgb2hsl(this);
    }
    public int gray(){
        double gray=Math.pow((Math.pow(r,2.2) * 0.2973 + Math.pow(g,2.2) * 0.6274 + Math.pow(b,2.2) * 0.0753),(1/2.2));
        gray=Calc.between(gray,0,255);
        return (int)gray;
    }
    public static double diff(Rgba c1,Rgba c2){
        double rate = (Math.abs(c1.r - c2.r) + Math.abs(c1.g - c2.g) + Math.abs(c1.b - c2.b) + Math.abs(c1.a - c2.a)) / 4.0 / 256.0;
        return rate;
    }

    /**
     * 颜色平滑渐变
     * @param rate 渐变率
     * @param c1 开始颜色
     * @param c2 结束颜色
     * @return
     */
    public static Rgba smooth(double rate,Rgba c1,Rgba c2){
        int r=Calc.smooth(rate,c1.r,c2.r);
        int g=Calc.smooth(rate,c1.g,c2.g);
        int b=Calc.smooth(rate,c1.b,c2.b);
        int a=Calc.smooth(rate,c1.a,c2.a);
        return Rgba.rgba(r,g,b,a);
    }

    public static Rgba black(){
        return rgb(0,0,0);
    }
    public static Rgba white(){
        return rgb(255,255,255);
    }
    public static Rgba red(){
        return rgb(255,0,0);
    }
    public static Rgba green(){
        return rgb(0,255,0);
    }
    public static Rgba blue(){
        return rgb(0,0,255);
    }
    public static Rgba transparent(){
        return rgba(0,0,0,0);
    }
    public static Rgba gray(double rate){
        int gy=(int)(255*rate);
        return rgb(gy,gy,gy);
    }
    public static Rgba gray(int val){
        return rgb(val,val,val);
    }
    public static Rgba yellow(){
        return rgb(255,255,0);
    }
    public static Rgba pink(){
        return rgb(255,0,255);
    }
    public static Rgba cyan(){
        return rgb(0,255,255);
    }
}
