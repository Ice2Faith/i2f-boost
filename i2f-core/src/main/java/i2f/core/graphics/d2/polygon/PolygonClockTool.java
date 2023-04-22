package i2f.core.graphics.d2.polygon;


import i2f.core.graphics.d2.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author ltb
 * @date 2022/8/14 20:09
 * @desc 凸多边形的时钟方向判定及时钟方向重排序
 */
public class PolygonClockTool {
    public static double lineDirection(double x1, double y1, double x2, double y2) {
        return Math.atan2(y2 - y1, x2 - x1);
    }

    public static Point centerPoint(List<Point> points) {
        double sx = 0;
        double sy = 0;
        int cnt = 0;
        for (Point item : points) {
            sx += item.getX();
            sy += item.getY();
            cnt++;
        }
        return new Point(sx / cnt, sy / cnt);
    }

    public static class Pair<K, V> {
        public K key;
        public V val;

        public Pair() {

        }

        public Pair(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }

    public static <T> List<T> reverseList(List<T> list) {
        int size = list.size();
        for (int i = 0; i < size / 2; i++) {
            T tmp = list.get(i);
            list.set(i, list.get(size - 1 - i));
            list.set(size - 1 - i, tmp);
        }
        return list;
    }

    /**
     * 按照时钟序排序
     *
     * @param points    凸多边形顶点
     * @param yAxisUp   是否Y轴向上
     * @param clockWise 是否顺时针
     * @return
     */
    public static List<Point> sortClock(List<Point> points, boolean yAxisUp, boolean clockWise) {
        int size = points.size();
        // 获取中心点
        Point center = centerPoint(points);
        //计算每个点与中心点的角度
        List<Pair<Double, Point>> list = new ArrayList<>(size);
        for (Point item : points) {
            double direct = lineDirection(center.getX(), center.getY(), item.getX(), item.getY());
            list.add(new Pair<>(direct, item));
        }
        // 按照角度序排序
        list.sort(new Comparator<Pair<Double, Point>>() {
            @Override
            public int compare(Pair<Double, Point> o1, Pair<Double, Point> o2) {
                return o1.key.compareTo(o2.key);
            }
        });
        // 输出排序结果
        List<Point> ret = new ArrayList<>(size);
        for (Pair<Double, Point> item : list) {
            ret.add(item.val);
        }
        // 这里得到的结果是Y轴向上时的逆时针方向

        // 根据参数进行变换
        // Y轴向下，需要逆反容器顺序，逆时针，也需要逆反容器顺序
        if (yAxisUp) {
            if (clockWise) {
                return reverseList(ret);
            } else {
                return ret;
            }
        } else {
            if (clockWise) {
                return reverseList(ret);
            } else {
                return ret;
            }
        }
    }


    public static void main(String[] args) {
        List<Point> list = Arrays.asList(
                new Point(1, 0),
                new Point(0, -1),
                new Point(-1, 0),
                new Point(0, 1)
        );

        list = Arrays.asList(
                new Point(119.809645, 25.504538),
                new Point(119.809596, 25.504432),
                new Point(119.809964, 25.504391),
                new Point(119.809901, 25.504297)
        );

        List<Point> clockList = sortClock(list, true, false);
        System.out.println(clockList);

        clockList = sortClock(list, true, true);
        System.out.println(clockList);

        clockList = sortClock(list, false, false);
        System.out.println(clockList);

        clockList = sortClock(list, false, true);
        System.out.println(clockList);
    }

}
