package i2f.core.graphics.d3;

import i2f.core.graphics.d2.Flat;
import i2f.core.graphics.d3.projection.IProjection;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author ltb
 * @date 2022/6/17 22:49
 * @desc 平面
 */
@Data
@NoArgsConstructor
public class D3Flat {
    public D3Point p1;
    public D3Point p2;
    public D3Point p3;

    public D3Flat(D3Point p1, D3Point p2, D3Point p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public Flat projection(IProjection proj){
        Flat ret=new Flat();
        ret.p1=p1.projection(proj);
        ret.p2=p2.projection(proj);
        ret.p3=p3.projection(proj);
        return ret;
    }

    public D3Vector normalLine(){
        D3Vector v1=D3Vector.vector(p1,p2);
        D3Vector v2=D3Vector.vector(p2,p3);
        return v1.normalLine(v2);
    }

    public D3Vector unitizationNormalLine(){
        D3Vector nl=normalLine();
        return nl.unitization();
    }
}
