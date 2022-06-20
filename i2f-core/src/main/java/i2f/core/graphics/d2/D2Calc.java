package i2f.core.graphics.d2;

import i2f.core.graphics.math.Calc;

/**
 * @author ltb
 * @date 2022/6/20 8:32
 * @desc 2D 运算
 */
public class D2Calc {
    /**
     * 计算两点构成的角度
     * @param startPoint
     * @param endPoint
     * @return
     */
    public static double lineDirection(Point startPoint, Point endPoint)
    {
        Line line=new Line(startPoint,endPoint);
        return Math.atan2(line.dy()*1.0,line.dx()*1.0);
    }

    /**
     * 将一个点按照direction方向移动length距离
     * @param startPoint
     * @param length
     * @param direction
     * @return
     */
    public static Point directionMove(Point startPoint, double length, double direction)
    {
        return new Point(startPoint.x+length*Math.cos(direction),startPoint.y+length*Math.sin(direction));
    }

    /**
     * 分别在x/y方向移动指定的距离
     * @param startPoint
     * @param dx
     * @param dy
     * @return
     */
    public static Point offsetMove(Point startPoint, double dx, double dy){
        return new Point(startPoint.x+dx,startPoint.y+dy);
    }


    /**
     * 将直线以起点为中心，偏转指定的角度
     * @param line
     * @param direction
     * @return
     */
    public static Line lineSpin(Line line, double direction){
        double length=line.length();
        double ndirect=line.direction()+direction;
        Point begin=line.begin;
        Point end=line.end.move2(length,ndirect);
        return new Line(begin,end);
    }

    /**
     * 计算反射角（弧度计算）
     * @param inRadian 入射弧度
     * @param flatRadian 反射平面弧度
     * @return 反射弧度
     */
    public static double reflectRadian(double inRadian, double flatRadian)
    {
        double i = Calc.regularRadian(inRadian);
        double mi = Calc.regularRadian(flatRadian);
        //double a=i-mi+2*PI;
        //return getRegularRadian((i+PI)+2*(PI/2-a));
        return Calc.regularRadian(-i + 2 * mi);
    }

}
