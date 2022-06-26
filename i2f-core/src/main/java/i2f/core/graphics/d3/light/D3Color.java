package i2f.core.graphics.d3.light;

import i2f.core.graphics.color.Rgba;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/26 18:46
 * @desc 图形学颜色[0-1]
 */
@NoArgsConstructor
public class D3Color {
    public double r;
    public double g;
    public double b;

    public D3Color(double r, double g, double b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Rgba rgba(){
        return rgba(this);
    }

    public static D3Color of(Rgba c){
        double r=c.r*1.0/255;
        double g=c.g*1.0/255;
        double b=c.b*1.0/255;
        return new D3Color(r,g,b);
    }

    public static Rgba rgba(D3Color c){
        double r=c.r*255;
        double g=c.g*255;
        double b=c.b*255;
        return Rgba.rgb((int)r,(int)g,(int)b);
    }
}
