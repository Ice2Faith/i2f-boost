package i2f.core.border;


import i2f.core.border.impl.dim1.NumberInDecider;
import i2f.core.border.impl.dim1.NumberLimitDecider;
import i2f.core.border.impl.dim1.NumberMiddleElementSplitor;
import i2f.core.border.impl.dim1.NumberRange;
import i2f.core.border.impl.dim2.PositionLimitDecider;
import i2f.core.border.impl.dim2.PositionMiddleElementSplitor;
import i2f.core.border.impl.dim2.circle.PositionInCircleDecider;
import i2f.core.border.impl.dim2.polygon.PositionInPolygonDecider;
import i2f.core.graphics.d2.Point;
import i2f.core.graphics.d2.shape.Circle;
import i2f.core.graphics.d2.shape.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Ice2Faith
 * @date 2023/2/6 8:38
 * @desc 边界近似算法
 * -------------------------
 * 采用二分差值的思想，确定两个状态的边界值
 */
public class BorderLimitUtil {
    public static void main(String[] args) {
        Random random = new Random();
        int cnt = random.nextInt(90) + 10;
        for (int i = 0; i < cnt; i++) {
            System.out.println("=============================================");
            int num1 = random.nextInt(100);
            int num2 = random.nextInt(100) + num1;

            int num3 = random.nextInt(100);
            int num4 = random.nextInt(100) + num3;

            Double fnum = findNumberBorder((double) num1, (double) num2, new NumberRange((double) num3, (double) num4));
            System.out.println("find=" + fnum);

        }

        List<Point> polygon = new ArrayList<>();
        polygon.add(new Point(119.416421, 26.023969));
        polygon.add(new Point(119.417018, 26.023887));
        polygon.add(new Point(119.416857, 26.022865));
        polygon.add(new Point(119.416246, 26.022966));

        System.out.println("========================================================");
        Point bd = findPolygonBorder(new Point(119.415954, 26.023319),
                new Point(119.416771, 26.023628),
                new Polygon(polygon));
        System.out.println("polygon border=" + bd);

    }

    /**
     * 确定在二维体系下的圆形内外边界
     * 也就是查找给定两点穿过圆弧的交点
     *
     * @param p1
     * @param p2
     * @param range
     * @return
     */
    public static Point findPolygonBorder(Point p1, Point p2, Circle range) {
        return findBorder(p1, p2, range,
                new PositionInCircleDecider(),
                new PositionMiddleElementSplitor(),
                new PositionLimitDecider());
    }


    /**
     * 确定在二维体系下的多边形内外边界
     * 也就是查找给定两点穿过多边形边的交点
     *
     * @param p1
     * @param p2
     * @param range
     * @return
     */
    public static Point findPolygonBorder(Point p1, Point p2, Polygon range) {
        return findBorder(p1, p2, range,
                new PositionInPolygonDecider(),
                new PositionMiddleElementSplitor(),
                new PositionLimitDecider());
    }

    /**
     * 确定在一纬体系下的区间边界
     * 也就是查找给定两个值包含的区间的区间边界点
     *
     * @param num1
     * @param num2
     * @param range
     * @return
     */
    public static Double findNumberBorder(Double num1, Double num2, NumberRange range) {
        return findBorder(num1, num2, range,
                new NumberInDecider(),
                new NumberMiddleElementSplitor(),
                new NumberLimitDecider());
    }

    /**
     * 给定两个元素elem1和elem2相对于一个区间range的不同状态值
     * 来确定这两个元素某种关系上（连线）与此区间的交点元素
     *
     * @param elem1
     * @param elem2
     * @param range
     * @param inDecider
     * @param middleSplitor
     * @param limitDecider
     * @param <E>
     * @param <R>
     * @return
     */
    public static <E, R> E findBorder(E elem1, E elem2, R range
            , BorderInDecider<E, R> inDecider
            , MiddleElementSplitor<E> middleSplitor
            , BorderLimitDecider<E> limitDecider) {
        E mid = null;
        boolean bin = inDecider.isInRange(elem1, range);
        boolean ein = inDecider.isInRange(elem2, range);

        boolean iter = (bin || ein) && !(bin && ein);

        if (!bin && !ein) {
            int level = 12;
            E felem = findInRangeElem(level, elem1, elem2, range, inDecider, middleSplitor);
            if (felem != null) {
                E fd = findBorder(elem1, felem, range, inDecider, middleSplitor, limitDecider);
                if (fd != null) {
                    return fd;
                }
                fd = findBorder(felem, elem2, range, inDecider, middleSplitor, limitDecider);
                if (fd != null) {
                    return fd;
                }
            }
        }

        int cnt = 0;
        while (iter) {
            mid = middleSplitor.middle(elem1, elem2);
            cnt++;

            bin = inDecider.isInRange(elem1, range);
            boolean min = inDecider.isInRange(mid, range);
            ein = inDecider.isInRange(elem2, range);

            if ((bin || min) && !(bin && min)) {
                elem2 = mid;
            } else if ((min || ein) && !(min && ein)) {
                elem1 = mid;
            } else {
                elem1 = mid;
            }

            if (limitDecider.isLimit(elem1, elem2)) {
                break;
            }
        }
        return mid;
    }

    private static <E, R> E findInRangeElem(int level, E elem1, E elem2, R range,
                                            BorderInDecider<E, R> inDecider,
                                            MiddleElementSplitor<E> middleSplitor) {
        if (level < 0) {
            return null;
        }
        E mid = middleSplitor.middle(elem1, elem2);
        if (inDecider.isInRange(mid, range)) {
            return mid;
        }
        E ret = findInRangeElem(level - 1, elem1, mid, range,
                inDecider, middleSplitor);
        if (ret != null) {
            return ret;
        }
        return findInRangeElem(level - 1, mid, elem2, range,
                inDecider, middleSplitor);
    }
}
