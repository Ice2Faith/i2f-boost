package i2f.core.graphics.d2.polygon;

import i2f.core.data.Pair;
import i2f.core.geo.GeoUtil;
import i2f.core.graphics.d2.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/7/11 14:00
 * @desc 判断点是否在多边形内工具类
 * 可以是任意多边形，可以使凹多边形
 * 唯一要求，根据Y轴方向，使用对应的判断方法
 */
public class PolygonLocationTool {
    /**
     * 屏幕坐标系，左上角为原点，Y轴向下
     * 0: p 在线段上
     * <0: p 在线段右侧
     * >0: p 在线段左侧
     * @param target
     * @param begin
     * @param end
     * @return
     */
    public static double isLeftOnDownAxisY(Point target, Point begin, Point end) {
        return (end.getX() - begin.getX()) * (target.getY() - begin.getY()) - (target.getX() - begin.getX()) * (end.getY() - begin.getY());
    }

    /**
     * 数学坐标系，左下角为原点，Y轴向上
     * 适用于地图经纬度，经纬度对应关系：经度=lng=x,纬度=lat=y
     * 0: p 在线段上
     * <0: p 在线段右侧
     * >0: p 在线段左侧
     *
     * @param target
     * @param begin
     * @param end
     * @return
     */
    public static double isLeftOnUpAxisY(Point target, Point end, Point begin) {
        return (end.getX() - begin.getX()) * (target.getY() - begin.getY()) - (target.getX() - begin.getX()) * (end.getY() - begin.getY());
    }

    /**
     * 计算wind值，wind=0,则在多边形之外，>0 则在多边形内
     * Y轴向下时
     * @param target
     * @param points
     * @return
     */
    public static int windOnDownAxisY(Point target, Point[] points) {
        int wn = 0;
        for (int i = 0; i < points.length; i++) {
            Point p1 = points[i];
            Point p2 = i + 1 == points.length ? points[0] : points[i + 1];

            if (target.getY() >= p1.getY()) {
                if (target.getY() < p2.getY() && isLeftOnDownAxisY(target, p1, p2) > 0) {
                    wn++;
                }
            } else if (target.getY() >= p2.getY() && isLeftOnDownAxisY(target, p1, p2) < 0) {
                wn--;
            }
        }
        return wn;
    }

    /**
     * 计算wind值，wind=0,则在多边形之外，>0 则在多边形内
     * Y轴向上时
     * @param target
     * @param points
     * @return
     */
    public static int windOnUpAxisY(Point target, Point[] points) {
        int wn = 0;
        for (int i = 0; i < points.length; i++) {
            Point p1 = points[i];
            Point p2 = i + 1 == points.length ? points[0] : points[i + 1];

            if (target.getY() >= p1.getY()) {
                if (target.getY() < p2.getY() && isLeftOnUpAxisY(target, p1, p2) > 0) {
                    wn++;
                }
            } else if (target.getY() >= p2.getY() && isLeftOnUpAxisY(target, p1, p2) < 0) {
                wn--;
            }
        }
        return wn;
    }

    /**
     * 点是否在多边形内部
     * Y轴向上时
     * @param target
     * @param points
     * @return
     */
    public static boolean windInPolygonOnUpAxisY(Point target, Point[] points) {
        return windOnUpAxisY(target, points) > 0;
    }

    /**
     * 点是否在多边形内部
     * Y轴向下时
     * @param target
     * @param points
     * @return
     */
    public static boolean windInPolygonOnDownAxisY(Point target, Point[] points) {
        return windOnDownAxisY(target, points) > 0;
    }

    public static Pair<Point, List<Point>> normalize(Point target, List<Point> points) {
        Point retPoint = new Point(target.getX(), target.getY());

        List<Point> retList = new ArrayList<>(points.size());
        for (Point item : points) {
            Point p = new Point(item.getX(), item.getY());
            retList.add(p);
        }
        retPoint.setX(retPoint.getX());
        retPoint.setY(retPoint.getY());

        for (Point item : retList) {
            item.setX(item.getX());
            item.setY(item.getY());

            item.setX(item.getX());
            item.setY(item.getY());
        }

        return new Pair<>(retPoint, retList);
    }

    /**
     * 判断点在多边形内部
     * Y轴向上时
     * @param target
     * @param points
     * @return
     */
    public static boolean windInPolygonOnUpAxisY(Point target, List<Point> points) {
        Pair<Point, List<Point>> normalizePair = normalize(target, points);
        Point nlPoint = normalizePair.getKey();
        List<Point> nlList = normalizePair.getVal();

        Point[] arr = new Point[nlList.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = nlList.get(i);
        }
        return windInPolygonOnUpAxisY(nlPoint, arr);
    }

    /**
     * 判断点在多边形内部
     * Y轴向下时
     * @param target
     * @param points
     * @return
     */
    public static boolean windInPolygonOnDownAxisY(Point target, List<Point> points) {
        Pair<Point, List<Point>> normalizePair = normalize(target, points);
        Point nlPoint = normalizePair.getKey();
        List<Point> nlList = normalizePair.getVal();

        Point[] arr = new Point[nlList.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = nlList.get(i);
        }
        return windInPolygonOnDownAxisY(nlPoint, arr);
    }


    public static boolean windInPolygon(Point target, List<Point> points) {
        Pair<Point, List<Point>> normalizePair = normalize(target, points);
        Point nlPoint = normalizePair.getKey();
        List<Point> nlList = normalizePair.getVal();

        Point[] arr = new Point[nlList.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = nlList.get(i);
        }
        return windInPolygonOnUpAxisY(nlPoint, arr);
    }

    public static boolean windInCircle(Point target, Point center, double radius) {
        double disMeter = geoDistance(target, center);
        return disMeter <= radius;
    }

    public static double geoDistance(Point begin, Point end) {
        // 采用等比例缩放方式，减少浮点数经度问题
        double scale = 1000;
        Point dbegin = new Point(begin.getX() * scale, begin.getY() * scale);
        Point dend = new Point(end.getX() * scale, end.getY() * scale);
        double dis = distance(dbegin, dend);
        dis = dis / scale;
        return GeoUtil.lat382meter(dis);
    }


    public static double distance(Point begin, Point end) {
        return Math.sqrt(Math.pow(end.getX() - begin.getX(), 2.0) + Math.pow(end.getY() - begin.getY(), 2.0));
    }

}
