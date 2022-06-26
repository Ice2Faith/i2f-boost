package i2f.core.graphics.d3.light;

import i2f.core.graphics.d3.D3Point;
import i2f.core.graphics.d3.D3Vector;

/**
 * @author ltb
 * @date 2022/6/26 18:52
 * @desc 光照系统
 */
public class LightAlgorithm {
    public static D3Color light(D3Point viewPoint, D3Light light, D3Point point, D3Vector normalLine,Material material,D3Color ambi){
        D3Color retC =new D3Color();
        D3Vector lightVec=new D3Vector(point, light.point);
        double distance = lightVec.length();
        lightVec=lightVec.unitization();
        D3Vector normalVec = normalLine;
        normalVec=normalVec.unitization();
        double cosLightNormal = lightVec.cosRadian(normalLine);
        if (cosLightNormal<0) {
            cosLightNormal = 0;
        }
        //加入漫反射光
        retC.r += light.diff.r*material.diff.r*cosLightNormal;
        retC.g += light.diff.g*material.diff.g*cosLightNormal;
        retC.b += light.diff.b*material.diff.b*cosLightNormal;
        //加入镜面反射光
        D3Vector viewVec=new D3Vector(point, viewPoint);
        viewVec=viewVec.unitization();
        D3Vector halfVec=lightVec.add(viewVec);
        halfVec=halfVec.unitization();

        double cosHalfNormal = halfVec.cosRadian(normalVec);
        if (cosHalfNormal<0) {
            cosHalfNormal = 0;
        }
        retC.r += light.spec.r*material.spec.r*Math.pow(cosHalfNormal, material.heigN);
        retC.g += light.spec.g*material.spec.g*Math.pow(cosHalfNormal, material.heigN);
        retC.b += light.spec.b*material.spec.b*Math.pow(cosHalfNormal, material.heigN);

        //光强衰弱
        double func = (1.0 / (light.c0 + light.c1*distance + light.c2*distance*distance));
        if (func>1.0) {
            func = 1.0;
        }
        retC.r *= func;
        retC.g *= func;
        retC.b *= func;
        //加入环境光
        retC.r += ambi.r*material.ambi.r;
        retC.g += ambi.g*material.ambi.g;
        retC.b += ambi.b*material.ambi.b;
        //颜色归一
        D3Vector tp=new D3Vector(retC.r,retC.g,retC.b);
        tp=tp.unitization();
        //颜色映射
        retC.r = tp.x;
        retC.g = tp.y;
        retC.b = tp.z;
        return retC;
    }
}
