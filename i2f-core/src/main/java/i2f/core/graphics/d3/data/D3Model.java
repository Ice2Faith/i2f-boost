package i2f.core.graphics.d3.data;

import i2f.core.graphics.d3.D3Point;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ltb
 * @date 2022/6/18 0:19
 * @desc 三维模型
 * 一般使用点云和三角片面方式表示一个三维模型
 * 因此点云存放在points中
 * 而三角片面则存放在flats中，每一个flat一般都是三个点的索引下标
 */
@Data
@NoArgsConstructor
public class D3Model {
    public List<D3Point> points;
    public List<D3ModelFlat> flats;

    public D3Model(List<D3Point> points, List<D3ModelFlat> flats) {
        this.points = points;
        this.flats = flats;
    }
}
