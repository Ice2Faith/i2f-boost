package i2f.core.lang.math;

import i2f.core.lang.math.segment.DateSegment;
import i2f.core.lang.math.segment.DoubleSegment;
import i2f.core.lang.math.segment.IntegerSegment;
import i2f.core.lang.math.segment.LongSegment;

import java.util.Date;

public class Segments {
    public static DateSegment of(Date begin, Date end) {
        return new DateSegment(begin, end);
    }

    public static IntegerSegment of(Integer begin, Integer end) {
        return new IntegerSegment(begin, end);
    }

    public static LongSegment of(Long begin, Long end) {
        return new LongSegment(begin, end);
    }

    public static DoubleSegment of(Double begin, Double end) {
        return new DoubleSegment(begin, end);
    }
}
