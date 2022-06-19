package i2f.core.graphics.d3.transform.impl.org;

import i2f.core.graphics.d3.D3Point;
import i2f.core.graphics.d3.transform.ID3Transform;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/18 21:27
 * @desc 坐标系旋转变换，坐标系依次绕x,y,z轴旋转sx,sy,sz角度（弧度制）后的位置
 */
@Data
@NoArgsConstructor
public class OrgSpinTransform implements ID3Transform {
    protected boolean enableMatrix=false;
    protected double sx;
    protected double sy;
    protected double sz;


    public OrgSpinTransform(boolean enableMatrix, double sx, double sy, double sz) {
        this.enableMatrix = enableMatrix;
        this.sx = sx;
        this.sy = sy;
        this.sz = sz;
    }

    @Override
    public D3Point transform(D3Point point) {
        ID3Transform trans=new OrgSpinXTransform(enableMatrix,sx);
        D3Point p=trans.transform(point);
        trans=new OrgSpinYTransform(enableMatrix,sy);
        p=trans.transform(p);
        trans=new OrgSpinZTransform(enableMatrix,sz);
        return trans.transform(p);
    }
}
