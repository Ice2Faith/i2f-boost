package i2f.core.geo;

/**
 * @author ltb
 * @date 2022/7/19 15:44
 * @desc 地理坐标工具类
 */
public class GeoUtil {
    /**
     * 内容来源： https://blog.csdn.net/Dust_Evc/article/details/102847870
     * 原文内容：
     * 地球半径：6371000M
     * 地球周长：2 * 6371000M * π = 40030173
     * 纬度38°地球周长：40030173 * cos38 = 31544206m
     * 任意地球经度周长：40030173m
     * <p>
     * <p>
     * 经度（东西方向）1米实际度：360°/31544206m = 1.141255544679108e-5 = 0.00001141°
     * 纬度（南北方向）1米实际度：360°/40030173m = 8.993216192195822e-6 = 0.00000899°
     * <p>
     * 地球表面上同一经线圈上相差1°两点间的距离约为πR/180 = 111194.926 644 558 737m       【小数部分每三位加了一空格】
     * 地球表面上同一纬线圈上相差1°两点间的距离约为πR×cos(纬度)/180 = 111194.926644m×cos(纬度)
     * <p>
     * 地球表面上同一经线圈上相差1"两点间的距离约为πR/180/3600 = 30.887 479 623 488m     【小数部分每三位加了一空格】
     * 地球表面上同一纬线圈上相差1"两点间的距离约为πR×cos(纬度)/180/3600 = 30.887m×cos(纬度)
     */

    public static final double COS_38 = angle2radian(38);

    /**
     * 地球半径（米）
     */
    public static final double EARTH_RADIUS_METER = 6371000;

    /**
     * 地球周长（米）
     */
    public static final double EARTH_LENGTH_METER = 40030173;

    /**
     * 北纬38度地球周长（米）
     * =EARTH_LENGTH_METER*cos38度
     */
    public static final double LAT38_EARTH_LENGTH_METER = 31544206;

    /**
     * 经度地球周长（米）
     */
    public static final double LNG_EARTH_LENGTH_METER = EARTH_LENGTH_METER;

    /**
     * 1米等于多少经度
     */
    public static final double ONE_METER_EQUAL_LNG = 0.00001141;

    /**
     * 1米等于多少纬度（基于北纬38度）
     */
    public static final double ONE_METER_EQUAL_LAT38 = 0.00000899;

    /**
     * 1经度等于多少米
     */
    public static final double ONE_LNG_EQ_METER = 111194.926644558737;

    public static final double ONE_LAT38_EQ_METER = 111194.926644 * COS_38;

    /**
     * 角度转弧度
     *
     * @param angle
     * @return
     */
    public static double angle2radian(double angle) {
        return angle / 180.0 * Math.PI;
    }


    /**
     * 弧度转角度
     *
     * @param radian
     * @return
     */
    public static double radian2angle(double radian) {
        return radian / Math.PI * 180.0;
    }

    /**
     * 在一定范围限度内，可以忽略经纬度之间的差异，使用其中一个进行计算即可
     * 0.000001度经纬度误差=0.03744
     */


    public static double meter2lng(double meter) {
        return meter * ONE_METER_EQUAL_LNG;
    }

    public static double meter2lat38(double meter) {
        return meter * ONE_METER_EQUAL_LAT38;
    }

    public static double lng2meter(double lng) {
        return lng * ONE_LNG_EQ_METER;
    }

    public static double lat382meter(double lat38) {
        return lat38 * ONE_LAT38_EQ_METER;
    }

}
