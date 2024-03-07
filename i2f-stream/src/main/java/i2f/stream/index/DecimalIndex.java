package i2f.stream.index;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/3/7 17:22
 * @desc
 */
public class DecimalIndex {
    private BigDecimal sum;
    private BigDecimal count;
    private BigDecimal min;
    private BigDecimal avg;
    private BigDecimal max;

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }

    public BigDecimal getMin() {
        return min;
    }

    public void setMin(BigDecimal min) {
        this.min = min;
    }

    public BigDecimal getAvg() {
        return avg;
    }

    public void setAvg(BigDecimal avg) {
        this.avg = avg;
    }

    public BigDecimal getMax() {
        return max;
    }

    public void setMax(BigDecimal max) {
        this.max = max;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DecimalIndex that = (DecimalIndex) o;
        return Objects.equals(sum, that.sum) &&
                Objects.equals(count, that.count) &&
                Objects.equals(min, that.min) &&
                Objects.equals(avg, that.avg) &&
                Objects.equals(max, that.max);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sum, count, min, avg, max);
    }

    @Override
    public String toString() {
        return "DecimalIndex{" +
                "sum=" + sum +
                ", count=" + count +
                ", min=" + min +
                ", avg=" + avg +
                ", max=" + max +
                '}';
    }
}
