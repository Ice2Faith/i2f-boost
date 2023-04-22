package i2f.core.graphics.d2.polygon;

import i2f.core.graphics.d2.Point;

import java.util.Arrays;
import java.util.List;

/**
 * @author ltb
 * @date 2022/7/31 22:02
 * @desc 多边形面积计算工具
 * 来源：https://blog.csdn.net/yin_zongming/article/details/9567495
 * 已知A(x1,y1)、B(x2,y2)、C(x3,y3)三点的面积公式为
 *            |x1 x2 x3|
 * S(A,B,C) = |y1 y2 y3| * 0.5 = [ (x1-x3) * (y2-y3) - (x2-x3) * (y1-y3) ] * 0.5
 *            |1   1  1|
 * (当三点为逆时针时为正，顺时针则为负的)
 * 也可以不用考虑正负，直接用这个式子：
 * S(A,B,C) = (abs)( [ (x1-x3) * (y2-y3) - (x2-x3) * (y1-y3) ] ) * 0.5
 *
 * 对多边形A1A2A3、、、An(顺或逆时针都可以)，设平面上有任意的一点P，则有：
 * S(A1,A2,A3.........,An) = abs(S(P,A1,A2) + S(P,A2,A3)+.............+S(P,An,A1))
 * P是可以取任意的一点，用(0，0)时就是下面的了：
 *
 * 设点顺序 (x1 y1) (x2 y2) ... (xn yn)
 * 则面积等于
 *            | x1   y1 |     | x2  y2 |           | xn  yn |
 * 0.5 * abs( | x2   y2 |  +  | x3  y3 |+ ...... + | x1  y1 | )
 *
 * 其中
 * | x1  y1 |
 * | x2  y2 | = x1 * y2 - y1 * x2
 * 因此面积公式展开为:
 *         |x1  y1| |x2  y2|      |xn  yn|
 * 0.5*abs(|x2  y2|+|x3  y3|+ ...+|x1  y1|)=0.5*abs(x1*y2 - y1*x2 + x2*y3 - y2*x3 + ...+ xn*y1 - yn * x1)
 */
public class PolygonAreaTool {
    public static void main(String[] args){
        List<Point> triangle= Arrays.asList(
                new Point(0,0),
                new Point(3,0),
                new Point(0,4)
        );
        double triangleArea=getArea(triangle);
        System.out.println("triangleArea:"+triangleArea);

        List<Point> square= Arrays.asList(
                new Point(0,0),
                new Point(5,0),
                new Point(5,5),
                new Point(0,5)
        );
        double squareArea=getArea(square);
        System.out.println("squareArea:"+squareArea);
    }
    /**
     * 计算多边形面积
     * @param points
     * @return -1 无法计算
     */
    public static double getArea(List<Point> points){
        if(points==null){
            return -1;
        }
        int size = points.size();
        if(size<3){
            return -1;
        }
        double sum=0;
        for(int i=0;i<size;i++){
            int j=(i+1)%size;

            sum+=points.get(i).getX()*points.get(j).getY()
                    -points.get(i).getY()*points.get(j).getX();
        }
        return 0.5*Math.abs(sum);
    }
}
