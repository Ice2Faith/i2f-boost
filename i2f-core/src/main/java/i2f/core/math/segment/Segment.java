package i2f.core.math.segment;

import lombok.Data;

@Data
public abstract class Segment<T extends Comparable<T>, D> {
    public T begin;
    public T end;

    public Segment() {
    }

    public Segment(T begin, T end) {
        this.begin = begin;
        this.end = end;
    }

    public abstract D distance();

    public boolean isOverlap(Segment<T, D> segment) {
        T min1 = begin.compareTo(end) < 0 ? begin : end;
        T max1 = begin.compareTo(end) < 0 ? end : begin;

        T min2 = segment.begin.compareTo(segment.end) < 0 ? segment.begin : segment.end;
        T max2 = segment.begin.compareTo(segment.end) < 0 ? segment.end : segment.begin;

        T min = min1.compareTo(min2) < 0 ? min1 : min2;
        T max = max1.compareTo(max2) < 0 ? max2 : max1;

        D dis1 = this.distance();
        D dis2 = segment.distance();

        D dis = instance(min, max).distance();

        // (dis1+dis2)>dis==>dis1+dis2-dis>0==>dis1-dis+dis2>0 避免加法溢出
        if (compareDistance(dis, addDistance(dis1, dis2)) < 0) {
            return true;
        }
        return false;
    }

    public abstract Segment<T, D> instance(T min, T max);

    public abstract D addDistance(D d1, D d2);

    public abstract int compareDistance(D d1, D d2);
}
