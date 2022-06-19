package i2f.core.graphics.d3.transform.impl;

import i2f.core.graphics.d3.D3Point;
import i2f.core.graphics.d3.D3VaryUtil;
import i2f.core.graphics.d3.transform.ID3Transform;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/18 21:23
 * @desc 使用4*4的变换矩阵进行的变换抽象类
 */
@Data
@NoArgsConstructor
public abstract class AbstractMatrixTransform implements ID3Transform {
    protected boolean enableMatrix;

    public AbstractMatrixTransform(boolean enableMatrix) {
        this.enableMatrix = enableMatrix;
    }

    @Override
    public D3Point transform(D3Point point) {
        if(enableMatrix){
            D3Point p= D3VaryUtil.vary(point,matrix());
            return p;
        }else{
            D3Point p=new D3Point(point.x,point.y,point.z);
            return trans(p);
        }
    }

    public abstract double[][] matrix();

    public abstract D3Point trans(D3Point p);
}
