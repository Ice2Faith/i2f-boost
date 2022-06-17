package i2f.core.graphics.d3.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ltb
 * @date 2022/6/18 0:20
 * @desc 三维模型的面
 */
@Data
@NoArgsConstructor
public class D3ModelFlat {
    public List<Integer> indexes;

    public D3ModelFlat(List<Integer> indexes) {
        this.indexes = indexes;
    }
}
