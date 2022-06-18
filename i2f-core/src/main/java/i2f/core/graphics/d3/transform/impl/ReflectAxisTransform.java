package i2f.core.graphics.d3.transform.impl;

import i2f.core.graphics.d3.D3Point;
import i2f.core.graphics.d3.transform.ITransform;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/18 21:27
 * @desc 反射变换，坐标依次对x,y,z轴反射，如果对应的bx,by,bz值为真，否则跳过
 */
@Data
@NoArgsConstructor
public class ReflectAxisTransform implements ITransform {
    protected boolean enableMatrix=false;
    protected boolean rx;
    protected boolean ry;
    protected boolean rz;

    public ReflectAxisTransform(boolean enableMatrix, boolean rx, boolean ry, boolean rz) {
        this.enableMatrix = enableMatrix;
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
    }

    @Override
    public D3Point transform(D3Point point) {
        D3Point p=new D3Point(point.x,point.y,point.z);
        if(rx){
            ITransform trans=new ReflectAxisXTransform(enableMatrix);
            p=trans.transform(p);
        }
        if(ry){
            ITransform trans=new ReflectAxisYTransform(enableMatrix);
            p=trans.transform(p);
        }
        if(rz){
            ITransform trans=new ReflectAxisZTransform(enableMatrix);
            p=trans.transform(p);
        }
        return p;
    }
}
