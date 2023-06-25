package i2f.core.lang.math;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;

public class Maths {
    public static SecureRandom random = new SecureRandom();

    public static int randInt() {
        return random.nextInt();
    }

    public static int randInt(int bound) {
        return random.nextInt(bound);
    }

    public static int randInt(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    public static double randDouble() {
        return random.nextDouble();
    }

    public static boolean randBoolean() {
        return random.nextBoolean();
    }

    public static double trunc(double num, int prec) {
        int aprec = Math.abs(prec);
        long pm = 1;
        for (int i = 0; i < aprec; i++) {
            pm *= 10;
        }
        if (prec >= 0) {
            BigDecimal bv = BigDecimal.valueOf(num);
            return bv.divide(BigDecimal.ONE, prec, RoundingMode.HALF_UP).doubleValue();
        } else {
            return ((long) (num / pm)) * pm;
        }
    }

    /**
     * 差值，在[begin-end]区间用比例rate[0.0-1.0]进行差值，得到结果并返回
     *
     * @param begin
     * @param end
     * @param rate
     * @return
     */
    public static double smoothValue(double begin, double end, double rate) {
        return begin + (end - begin) * rate;
    }

    public static long smoothValue(long begin, long end, double rate) {
        return (long) smoothValue((double) begin, (double) end, rate);
    }

    public static int smoothValue(int begin, int end, double rate) {
        return (int) smoothValue((double) begin, (double) end, rate);
    }

    /**
     * 计算中间点ref在区间[begin-end]上的差值比例
     *
     * @param begin
     * @param end
     * @param ref
     * @return
     */
    public static double smoothRate(double begin, double end, double ref) {
        double rate = (ref - begin) / (end - begin);
        if (Double.isNaN(rate)) {
            return 0;
        }
        if (Double.isInfinite(rate)) {
            return 1;
        }
        return rate;
    }

    public static double smoothRate(long begin, long end, long ref) {
        return smoothRate((double) begin, (double) end, (double) ref);
    }

    public static double smoothRate(int begin, int end, int ref) {
        return smoothRate((double) begin, (double) end, (double) ref);
    }
}
