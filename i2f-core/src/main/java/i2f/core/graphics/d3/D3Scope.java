package i2f.core.graphics.d3;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/17 23:26
 * @desc 描述三维空间范围
 */
@Data
@NoArgsConstructor
public class D3Scope {
    public D3Point point;
    public D3Size size;

    public D3Scope(D3Point point, D3Size size){
        this.point=point;
        this.size=size;
    }
}
