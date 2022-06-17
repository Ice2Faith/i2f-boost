package i2f.core.graphics.color;

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
    public static Rgba rgba(int c){
        int r=(c>>>24)&0x0ff;
        int g=(c>>16)&0x0ff;
        int b=(c>>8)&0x0ff;
        int a=c&0x0ff;
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
        int c=(r<<16)|(g<<16)|(b<<8)|a;
        return c;
    }
    public int argb(){
        int c=(a<<16)|(r<<16)|(g<<8)|b;
        return c;
    }
}
