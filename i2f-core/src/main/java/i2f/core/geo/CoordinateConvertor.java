package i2f.core.geo;


/**
 * @author ltb
 * @date 2022/7/30 20:47
 * @desc 地理坐标转换类
 * WGS84 大地坐标系，标准的国际GPS坐标
 * GCJ02 火星坐标系，国家测绘局针对中国地区进行偏移变换的加密后坐标系，高德坐标系
 * BD09/BD09LL 百度坐标系，百度在GCJ02基础上，再次变换之后的坐标系
 * BD09MC 百度坐标系的以米为单位的墨卡托投影坐标系
 *
 * 转换自 CoordinateConvertor.go
 * conversion between WGS-84, GCJ-02, BD-09-LL, BD-09-MC
 * conversion structure
 *
 * BD09LL --- GCJ-02 ----------------- WGS84
 *   			|						  |
 * 		(BaiduMercartorConvert)	 (WebMercartorConvert)
 *   			|						  |
 * 			BD09MC					WebMercator
 *
 *
 * referered documents:
 * http://blog.csdn.net/yu412346928/article/details/24419845
 * http://ju.outofmemory.cn/entry/309208
 * https://github.com/everalan/convertMC2LL
 * http://blog.csdn.net/sinat_34719507/article/details/60904361
 */
public class CoordinateConvertor {

    public static final RuntimeException INVALID_GPS_EXCEPTION = new RuntimeException("invalid gps!");

    public static final double X_PI = 3.14159265358979324 * 3000.0 / 180.0;
    public static final double PI = 3.1415926535897932384626;
    public static final double a = 6378245.0;
    public static final double ee = 0.00669342162296594323;
    public static final double earthHalfCir = 20037508.34;

    public static boolean isInChina(double lng, double lat) {
        // 纬度3.86~53.55,经度73.66~135.05
        return lng > 73.66 && lng < 135.05 && lat > 3.86 && lat < 53.55;
    }

    public static boolean isInvalidGps(double lng, double lat) {
        return !(lng >= -180 && lng <= 180 && lat >= -90 && lat <= 90);
    }

    public static double transLat(double lng, double lat) {
        double ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * PI) + 40.0 * Math.sin(lat / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lat / 12.0 * PI) + 320 * Math.sin(lat * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    public static double transLng(double lng, double lat) {
        double ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lng * PI) + 40.0 * Math.sin(lng / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lng / 12.0 * PI) + 300.0 * Math.sin(lng / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }

    public static GeoPoint GCJ02ToWGS84(double lng, double lat) {
        if (isInvalidGps(lng, lat)) {
            throw INVALID_GPS_EXCEPTION;
        }

        if (!isInChina(lng, lat)) {
            return new GeoPoint(lng, lat);
        }

        double dlat = transLat(lng - 105.0, lat - 35.0);
        double dlng = transLng(lng - 105.0, lat - 35.0);
        double radlat = lat / 180.0 * PI;
        double magic = Math.sin(radlat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * PI);
        dlng = (dlng * 180.0) / (a / sqrtMagic * Math.cos(radlat) * PI);
        double mglat = lat + dlat;
        double mglng = lng + dlng;
        return new GeoPoint(lng * 2 - mglng, lat * 2 - mglat);
    }

    public static GeoPoint WGS84ToGCJ02(double lng, double lat) {
        if (isInvalidGps(lng, lat)) {
            throw INVALID_GPS_EXCEPTION;
        }

        if (!isInChina(lng, lat)) {
            return new GeoPoint(lng, lat);
        }

        double dlat = transLat(lng - 105.0, lat - 35.0);
        double dlng = transLng(lng - 105.0, lat - 35.0);
        double radlat = lat / 180.0 * PI;
        double magic = Math.sin(radlat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * PI);
        dlng = (dlng * 180.0) / (a / sqrtMagic * Math.cos(radlat) * PI);
        return new GeoPoint(lng + dlng, lat + dlat);
    }

    public static GeoPoint GCJ02ToBD09(double lng, double lat) {
        if (isInvalidGps(lng, lat)) {
            throw INVALID_GPS_EXCEPTION;
        }
        double z = Math.sqrt(lng * lng + lat * lat) + 0.00002 * Math.sin(lat * X_PI);
        double theta = Math.atan2(lat, lng) + 0.000003 * Math.cos(lng * X_PI);
        return new GeoPoint(z * Math.cos(theta) + 0.0065, z * Math.sin(theta) + 0.006);
    }

    public static GeoPoint BD09ToGCJ02(double lng, double lat) {
        if (isInvalidGps(lng, lat)) {
            throw INVALID_GPS_EXCEPTION;
        }

        double x = lng - 0.0065;
        double y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * X_PI);
        return new GeoPoint(z * Math.cos(theta), z * Math.sin(theta));
    }

    public static GeoPoint BD09ToWGS84(double lng, double lat) {
        GeoPoint gp = BD09ToGCJ02(lng, lat);
        return GCJ02ToWGS84(gp.lng, gp.lat);
    }

    public static GeoPoint WGS84ToBD09(double lng, double lat) {
        GeoPoint tp = WGS84ToGCJ02(lng, lat);
        return GCJ02ToBD09(tp.lng, tp.lat);
    }

    public static GeoPoint WebMCToWGS84(double mercartorX, double mercartorY) {
        if (!(mercartorX >= -earthHalfCir && mercartorX <= earthHalfCir)) {
            throw INVALID_GPS_EXCEPTION;
        }

        double lng = mercartorX / earthHalfCir * 180;
        double lat = mercartorY / earthHalfCir * 180;
        lat = 180 / PI * (2 * Math.atan(Math.exp(lat * PI / 180)) - PI / 2);
        return new GeoPoint(lng, lat);
    }

    public static GeoPoint WGS84ToWebMC(double lng, double lat) {
        if (isInvalidGps(lng, lat)) {
            throw INVALID_GPS_EXCEPTION;
        }
        double x = lng * earthHalfCir / 180;
        double y = Math.log(Math.tan((90 + lat) * PI / 360)) / (PI / 180);
        y = y * earthHalfCir / 180;
        return new GeoPoint(x, y);
    }

    // constants for baidu mercartor conversion
    public static final double[] mcband = {12890594.86, 8362377.87, 5591021, 3481989.83, 1678043.12, 0};
    public static final double[][] mc2ll = {
            {1.410526172116255e-8, 0.00000898305509648872, -1.9939833816331, 200.9824383106796, -187.2403703815547, 91.6087516669843, -23.38765649603339, 2.57121317296198, -0.03801003308653, 17337981.2},
            {-7.435856389565537e-9, 0.000008983055097726239, -0.78625201886289, 96.32687599759846, -1.85204757529826, -59.36935905485877, 47.40033549296737, -16.50741931063887, 2.28786674699375, 10260144.86},
            {-3.030883460898826e-8, 0.00000898305509983578, 0.30071316287616, 59.74293618442277, 7.357984074871, -25.38371002664745, 13.45380521110908, -3.29883767235584, 0.32710905363475, 6856817.37},
            {-1.981981304930552e-8, 0.000008983055099779535, 0.03278182852591, 40.31678527705744, 0.65659298677277, -4.44255534477492, 0.85341911805263, 0.12923347998204, -0.04625736007561, 4482777.06},
            {3.09191371068437e-9, 0.000008983055096812155, 0.00006995724062, 23.10934304144901, -0.00023663490511, -0.6321817810242, -0.00663494467273, 0.03430082397953, -0.00466043876332, 2555164.4},
            {2.890871144776878e-9, 0.000008983055095805407, -3.068298e-8, 7.47137025468032, -0.00000353937994, -0.02145144861037, -0.00001234426596, 0.00010322952773, -0.00000323890364, 826088.5},
    };
    public static double[] llband = {75, 60, 45, 30, 15, 0};
    // 由于java存在数据精度问题，所有数据下调1000倍
    public static double[][] ll2mc = {
            {-0.0015702102444, 111320.7020616939, 1704480524535203L, -10338987376042340L, 26112667856603880L, -35149669176653700L, 26595700718403920L, -10725012454188240L, 1800819912950474L, 82.5},
            {0.0008277824516172526, 111320.7020463578, 647795574.6671607, -4082003173.641316, 10774905663.51142, -15171875531.51559, 12053065338.62167, -5124939663.577472, 913311935.9512032, 67.5},
            {0.00337398766765, 111320.7020202162, 4481351.045890365, -23393751.19931662, 79682215.47186455, -115964993.2797253, 97236711.15602145, -43661946.33752821, 8477230.501135234, 52.5},
            {0.00220636496208, 111320.7020209128, 51751.86112841131, 3796837.749470245, 992013.7397791013, -1221952.21711287, 1340652.697009075, -620943.6990984312, 144416.9293806241, 37.5},
            {-0.0003441963504368392, 111320.7020576856, 278.2353980772752, 2485758.690035394, 6070.750963243378, 54821.18345352118, 9540.606633304236, -2710.55326746645, 1405.483844121726, 22.5},
            {-0.0003218135878613132, 111320.7020701615, 0.00369383431289, 823725.6402795718, 0.46104986909093, 2351.343141331292, 1.58060784298199, 8.77738589078284, 0.37238884252424, 7.45},
    };

    public static GeoPoint BDMCToGCJ02(double mercartorX, double mercartorY) {

        mercartorX = Math.abs(mercartorX);
        mercartorY = Math.abs(mercartorY);
        double[] f = new double[0];
        for (int i = 0; i < mcband.length; i++) {
            if (mercartorY >= mcband[i]) {
                f = mc2ll[i];
                break;
            }
        }
        if (f.length == 0) {
            for (int i = 0; i < mcband.length; i++) {
                if (-mercartorY <= -mcband[i]) {
                    f = mc2ll[i];
                    break;
                }
            }
        }
        return convert(mercartorX, mercartorY, f);
    }

    public static GeoPoint convert(double lng, double lat, double[] f) {
        if (f.length == 0) {
            throw INVALID_GPS_EXCEPTION;
        }
        double tlng = f[0] + f[1] * Math.abs(lng);
        double cc = Math.abs(lat) / f[9];

        double tlat = 0;
        for (int i = 0; i <= 6; i++) {
            tlat += (f[i + 2] * Math.pow(cc, (double) (i)));
        }

        if (lng < 0) {
            tlng *= -1;
        }
        if (lat < 0) {
            tlat *= -1;
        }
        return new GeoPoint(tlng, tlat);
    }

    public static GeoPoint GCJ02ToBDMC(double lng, double lat) {
        lng = getLoop(lng, -180, 180);
        lat = getRange(lat, -74, 74);
        double[] f = new double[0];
        for (int i = 0; i < llband.length; i++) {
            if (lat >= llband[i]) {
                f = ll2mc[i];
                break;
            }
        }
        if (f.length > 0) {
            for (int i = llband.length - 1; i >= 0; i--) {
                if (lat <= -llband[i]) {
                    f = ll2mc[i];
                    break;
                }
            }
        }
        return convert(lng, lat, f);
    }

    public static double getLoop(double lng, double min, double max) {
        while (lng > max) {
            lng -= (max - min);
        }
        while (lng < min) {
            lng += (max - min);
        }
        return lng;
    }

    public static double getRange(double lat, double min, double max) {
        if (min != 0) {
            lat = Math.max(lat, min);
        }
        if (max != 0) {
            lat = Math.min(lat, max);
        }
        return lat;
    }

    public static GeoPoint BDMCToWGS84(double mercartorX, double mercartorY) {
        GeoPoint gcjp = BDMCToGCJ02(mercartorX, mercartorY);
        return GCJ02ToWGS84(gcjp.lng, gcjp.lat);
    }

    public static GeoPoint BDMCToBD09(double mercartorX, double mercartorY) {
        GeoPoint gcjp = BDMCToGCJ02(mercartorX, mercartorY);
        return GCJ02ToBD09(gcjp.lng, gcjp.lat);
    }

    public static GeoPoint WGS84ToBDMC(double lng, double lat) {
        GeoPoint gcjp = WGS84ToGCJ02(lng, lat);
        return GCJ02ToBDMC(gcjp.lng, gcjp.lat);
    }


    public static void main(String[] args){
        GeoPoint bd09=new GeoPoint(119.415838,26.023276);
        GeoPoint wgs84=BD09ToWGS84(bd09.lng, bd09.lat);
        GeoPoint rbd09_wgs84=WGS84ToBD09(wgs84.lng, wgs84.lat);

        GeoPoint gcj02=BD09ToGCJ02(bd09.lng, bd09.lat);
        GeoPoint rbd09_gcj02=GCJ02ToBD09(gcj02.lng, gcj02.lat);

        System.out.println("ok");


        wgs84=new GeoPoint(119.4051577,26.0201633);
        bd09=WGS84ToBD09(wgs84.lng, wgs84.lat);
        GeoPoint bdmc=WGS84ToBDMC(wgs84.lng, wgs84.lat);

        System.out.println("ok");
    }
}
