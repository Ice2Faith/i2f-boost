package i2f.core.graphics.d3.transform;

import i2f.core.graphics.d3.D3Point;

/**
 * @author ltb
 * @date 2022/6/18 0:28
 * @desc 定义变换的标准接口
 */
public interface ID3Transform {
    D3Point transform(D3Point point);
}
