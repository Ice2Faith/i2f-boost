package i2f.core.graphics.d2;

/**
 * @author ltb
 * @date 2022/7/11 14:00
 * @desc
 */
public class LocationUtil {
    /**
     * 0: p 在线段上
     * <0: p 在线段右侧
     * >0: p 在线段左侧
     * @param target
     * @param begin
     * @param end
     * @return
     */
    public static double isLeft(Point target, Point begin, Point end) {
        return (end.x - begin.x) * (target.y - begin.y) - (target.x - begin.x) * (end.y - begin.y);
    }

    /**
     * 计算wind值，wind=0,则在多边形之外，>0 则在多边形内
     * @param target
     * @param points
     * @return
     */
    public static int wind(Point target, Point[] points) {
        int wn = 0;
        for (int i = 0; i < points.length; i ++) {
            Point p1 = points[i];
            Point p2 = i + 1 == points.length ? points[0] : points[i + 1];

            if (target.y >= p1.y) {
                if (target.y < p2.y && isLeft(target, p1, p2) > 0){
                    wn++;
                }
            } else if (target.y >= p2.y && isLeft(target, p1, p2) < 0) {
                wn--;
            }
        }
        return wn;
    }

    /**
     * 点是否在多边形内部
     * @param target
     * @param points
     * @return
     */
    public static boolean windInPolygon(Point target,Point[]  points){
        return wind(target,points)>0;
    }
}
