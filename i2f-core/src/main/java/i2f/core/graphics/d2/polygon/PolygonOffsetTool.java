package i2f.core.graphics.d2.polygon;


import i2f.core.graphics.d2.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/7/19 13:48
 * @desc 多边形外扩内缩算法
 */
public class PolygonOffsetTool {

    /**
     * 多边形外扩内缩算法，仅适用于凸多边形
     *
     * @param points 多边形的点坐标，需要顺时针排列，如果逆时针排列，则offset取相反值
     * @param offset 偏移距离，正值为外扩，负值为内缩
     * @return
     */
    public static List<Point> offset(List<Point> points, double offset) {
        // 计算边的单位化向量
        List<Point> unitizationEdgeVectors = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            int ni = (i == points.size() - 1) ? 0 : i + 1;
            // 计算边向量
            Point vec = Point.sub(points.get(ni), points.get(i));
            // 单位化边向量
            // 计算向量模长
            double val = Point.dotMul(vec, vec);
            double vecLen = Math.sqrt(val);
            // 单位化
            double dval = 1.0 / vecLen;
            unitizationEdgeVectors.add(Point.mul(vec, dval));
        }

        List<Point> resultPoints = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            int si = (i == 0) ? (points.size() - 1) : (i - 1);
            int ei = i;
            // 计算向量夹角
            double sina = Point.crossMul(unitizationEdgeVectors.get(si), unitizationEdgeVectors.get(ei));
            double len = offset / sina;
            Point vec = Point.sub(unitizationEdgeVectors.get(ei), unitizationEdgeVectors.get(si));
            Point p = Point.add(points.get(i), Point.mul(vec, len));
            resultPoints.add(p);
        }

        return resultPoints;
    }


}
