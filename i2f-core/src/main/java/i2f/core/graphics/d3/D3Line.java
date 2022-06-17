package i2f.core.graphics.d3;

import i2f.core.graphics.math.Calc;
import i2f.core.graphics.std.ILenght;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/17 22:49
 * @desc 直线
 */
@Data
@NoArgsConstructor
public class D3Line implements ILenght {
    public D3Point begin;
    public D3Point end;

    public D3Line(D3Point begin,D3Point end){
        this.begin=begin;
        this.end=end;
    }
    @Override
    public double length() {
        return Calc.distance(this.begin.x,this.begin.y,this.begin.z,this.end.x,this.end.y,this.end.z);
    }
}
