package i2f.core.geo;

/**
 * @author ltb
 * @date 2022/7/30 22:39
 * @desc 地理坐标类
 */
public class GeoPoint {
    public double lng;
    public double lat;

    public GeoPoint() {
    }

    public GeoPoint(double lng, double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    public static GeoPoint parse(String lng,String lat){
        double dlng=Double.parseDouble(lng);
        double dlat=Double.parseDouble(lat);
        return new GeoPoint(dlng,dlat);
    }
}
