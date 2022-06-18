package i2f.core.graphics.d3.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/18 20:14
 * @desc 三角片面
 */
@Data
@NoArgsConstructor
public class D3ModelFlat {
    public int p1;
    public int p2;
    public int p3;

    public D3ModelFlat(int p1, int p2, int p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }
}
