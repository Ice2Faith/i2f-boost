package i2f.core.graphics.d3;

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
    public D3Point loc1;
    public D3Point loc2;
    public D3Point loc3;

    public D3Flat(D3Point loc1, D3Point loc2, D3Point loc3) {
        this.loc1 = loc1;
        this.loc2 = loc2;
        this.loc3 = loc3;
    }
}
