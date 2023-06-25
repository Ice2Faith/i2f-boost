package i2f.core.lang.math.segment;

import i2f.core.safe.Safes;

public class IntegerSegment extends Segment<Integer, Integer> {
    public IntegerSegment() {
    }

    public IntegerSegment(Integer begin, Integer end) {
        super(begin, end);
    }

    @Override
    public Integer distance() {
        return Math.abs(end - begin);
    }

    @Override
    public Segment<Integer, Integer> instance(Integer min, Integer max) {
        return new IntegerSegment(min, max);
    }

    @Override
    public Integer addDistance(Integer d1, Integer d2) {
        return d1 + d2;
    }

    @Override
    public int compareDistance(Integer d1, Integer d2) {
        return Safes.compare(d1, d2);
    }
}
