package i2f.core.graphics.d2;

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
public class Line implements ILenght {
    public Point begin;
    public Point end;

    public Line(Point begin,Point end){
        this.begin=begin;
        this.end=end;
    }
    @Override
    public double length() {
        return Calc.distance(this.begin.x,this.begin.y,this.end.x,this.end.y);
    }

    public double dx(){
        return end.x-begin.x;
    }

    public double dy(){
        return end.y-begin.y;
    }

    public double direction(){
        return D2Calc.lineDirection(begin,end);
    }

    public Line spin(double direction){
        return D2Calc.lineSpin(this,direction);
    }


}
