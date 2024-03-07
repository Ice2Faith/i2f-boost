package i2f.stream.window;

import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/3/7 15:41
 * @desc
 */
public class TimeWindowInfo extends WindowInfo {
    private long windowBeginTime;
    private long windowEndTime;
    private long realEndTime;

    public long getWindowBeginTime() {
        return windowBeginTime;
    }

    public void setWindowBeginTime(long windowBeginTime) {
        this.windowBeginTime = windowBeginTime;
    }

    public long getWindowEndTime() {
        return windowEndTime;
    }

    public void setWindowEndTime(long windowEndTime) {
        this.windowEndTime = windowEndTime;
    }

    public long getRealEndTime() {
        return realEndTime;
    }

    public void setRealEndTime(long realEndTime) {
        this.realEndTime = realEndTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        TimeWindowInfo that = (TimeWindowInfo) o;
        return windowBeginTime == that.windowBeginTime &&
                windowEndTime == that.windowEndTime &&
                realEndTime == that.realEndTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), windowBeginTime, windowEndTime, realEndTime);
    }

    @Override
    public String toString() {
        return "TimeWindowInfo{" +
                "windowBeginTime=" + windowBeginTime +
                ", windowEndTime=" + windowEndTime +
                ", realEndTime=" + realEndTime +
                ", elementCount=" + elementCount +
                ", windowCount=" + windowCount +
                ", submitWindowCount=" + submitWindowCount +
                '}';
    }
}
