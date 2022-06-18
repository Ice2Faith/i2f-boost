package i2f.core.graphics.d3.shape;

import i2f.core.graphics.d2.Bezier;
import i2f.core.graphics.d2.Point;
import i2f.core.graphics.d3.data.D3Model;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ltb
 * @date 2022/6/18 0:11
 * @desc 旋转体
 */
@Data
@NoArgsConstructor
public class SpinBezierCube {
    public List<Point> points;

    public SpinBezierCube(List<Point> points) {
        this.points = points;
    }

    public D3Model makeModelSpinX(int sampleCount,int angleCount){

        List<Point> samplePoints=Bezier.resamples(points,sampleCount);

        return new SpinCube(samplePoints).makeModelSpinX(angleCount);
    }

    public D3Model makeModelSpinY(int sampleCount,int angleCount){

        List<Point> samplePoints=Bezier.resamples(points,sampleCount);

        return new SpinCube(samplePoints).makeModelSpinX(angleCount);
    }
}
