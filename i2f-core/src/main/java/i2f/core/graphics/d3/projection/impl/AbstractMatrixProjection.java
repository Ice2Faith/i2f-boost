package i2f.core.graphics.d3.projection.impl;

import i2f.core.graphics.d2.Point;
import i2f.core.graphics.d3.D3Point;
import i2f.core.graphics.d3.D3VaryUtil;
import i2f.core.graphics.d3.projection.ID3Projection;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/18 20:20
 * @desc 使用4*4的变换矩阵进行的投影抽象类
 */
@Data
@NoArgsConstructor
public abstract class AbstractMatrixProjection implements ID3Projection {
    protected boolean enableMatrix;

    public AbstractMatrixProjection(boolean enableMatrix) {
        this.enableMatrix = enableMatrix;
    }

    @Override
    public Point projection(D3Point point) {
        if(enableMatrix){
            D3Point p= D3VaryUtil.vary(point,matrix());
            p= beforeMatrixReturn(p);
            return new Point(p.x,p.y);
        }else{
            D3Point p=new D3Point(point.x,point.y,point.z);
            return proj(point);
        }
    }
    public D3Point beforeMatrixReturn(D3Point p){
        return p;
    }
    // 三视图的结果在yoz平面内，可能需要转换到xoy平面显示，因此交换X,Z轴
    public D3Point swapXZ(D3Point point){
        return new D3Point(point.z,point.y,point.x);
    }
    public abstract double[][] matrix();
    public abstract Point proj(D3Point point);
}
