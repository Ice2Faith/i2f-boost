package i2f.core.graphics.d2;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/17 22:49
 * @desc 点
 */
@Data
@NoArgsConstructor
public class Point {
    public double x;
    public double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point move2(double length,double direction){
        return D2Calc.directionMove(this,length,direction);
    }


    public static Point add(Point p1, Point p2) {
        double px = p1.x + p2.x;
        double py = p1.y + p2.y;
        return new Point(px, py);
    }

    public static Point sub(Point p1, Point p2) {
        double px = p1.x - p2.x;
        double py = p1.y - p2.y;
        return new Point(px, py);
    }

    public static double dotMul(Point p1, Point p2) {
        double v1 = p1.x * p2.x;
        double v2 = p1.y * p2.y;
        return v1 + v2;
    }

    public static double crossMul(Point p1, Point p2) {
        double v1 = p1.x * p2.y;
        double v2 = p1.y * p2.x;
        return v1 - v2;
    }

    public static Point mul(Point p1, double val) {
        double v1 = p1.x * val;
        double v2 = p1.y * val;
        return new Point(v1, v2);
    }

}
