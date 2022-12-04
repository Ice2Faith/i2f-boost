package i2f.core.math.segment;

import i2f.core.safe.Safes;

public class DoubleSegment extends Segment<Double, Double> {
    public DoubleSegment() {
    }

    public DoubleSegment(Double begin, Double end) {
        super(begin, end);
    }

    @Override
    public Double distance() {
        return Math.abs(end - begin);
    }

    @Override
    public Segment<Double, Double> instance(Double min, Double max) {
        return new DoubleSegment(min, max);
    }

    @Override
    public Double addDistance(Double d1, Double d2) {
        return d1 + d2;
    }

    @Override
    public int compareDistance(Double d1, Double d2) {
        return Safes.compare(d1, d2);
    }
}
