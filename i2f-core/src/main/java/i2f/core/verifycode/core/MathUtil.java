package i2f.core.verifycode.core;

import java.security.SecureRandom;

/**
 * @author Ice2Faith
 * @date 2023/8/15 14:28
 * @desc
 */
public class MathUtil {
    public static final SecureRandom RANDOM = new SecureRandom();

    public static double distance(double bx, double by, double ex, double ey) {
        return distance(ex - bx, ey - by);
    }

    public static double distance(double dx, double dy) {
        return Math.sqrt(Math.pow(dx, 2.0) + Math.pow(dy, 2.0));
    }

    public static double angle2radian(double val) {
        return val / 180 * Math.PI;
    }

    public static double radian2angle(double val) {
        return val / Math.PI * 180;
    }

    public static double radian(double dx, double dy) {
        return Math.atan2(dy, dx);
    }

    public static double radian(double bx, double by, double ex, double ey) {
        return radian(ex - bx, ey - by);
    }
}
