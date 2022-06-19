package i2f.core.graphics.d3.projection;

import i2f.core.graphics.d2.Point;
import i2f.core.graphics.d3.D3Point;

/**
 * @author ltb
 * @date 2022/6/18 0:27
 * @desc 定义投影的标准接口
 */
public interface ID3Projection {
    Point projection(D3Point point);
}
