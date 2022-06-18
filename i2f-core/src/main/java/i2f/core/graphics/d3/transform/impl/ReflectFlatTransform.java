package i2f.core.graphics.d3.transform.impl;

import i2f.core.graphics.d3.D3Point;
import i2f.core.graphics.d3.transform.ITransform;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/18 21:27
 * @desc 反射变换，坐标依次对xoy,yoz,xoz平面反射，如果对应的xoy,yoz,xoz值为真，否则跳过
 */
@Data
@NoArgsConstructor
public class ReflectFlatTransform implements ITransform {
    protected boolean enableMatrix=false;
    protected boolean xoy;
    protected boolean yoz;
    protected boolean xoz;

    public ReflectFlatTransform(boolean enableMatrix, boolean xoy, boolean yoz, boolean xoz) {
        this.enableMatrix = enableMatrix;
        this.xoy = xoy;
        this.yoz = yoz;
        this.xoz = xoz;
    }

    @Override
    public D3Point transform(D3Point point) {
        D3Point p=new D3Point(point.x,point.y,point.z);
        if(xoy){
            ITransform trans=new ReflectFlatXoYTransform(enableMatrix);
            p=trans.transform(p);
        }
        if(yoz){
            ITransform trans=new ReflectFlatYoZTransform(enableMatrix);
            p=trans.transform(p);
        }
        if(xoz){
            ITransform trans=new ReflectFlatXoZTransform(enableMatrix);
            p=trans.transform(p);
        }
        return p;
    }
}
