package i2f.stream.window;

import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/3/7 15:41
 * @desc
 */
public class ViewTimeWindowInfo extends WindowInfo {
    private long windowBeginTime;
    private long windowEndTime;
    private long realBeginTime;
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

    public long getRealBeginTime() {
        return realBeginTime;
    }

    public void setRealBeginTime(long realBeginTime) {
        this.realBeginTime = realBeginTime;
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
        ViewTimeWindowInfo that = (ViewTimeWindowInfo) o;
        return windowBeginTime == that.windowBeginTime &&
                windowEndTime == that.windowEndTime &&
                realBeginTime == that.realBeginTime &&
                realEndTime == that.realEndTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), windowBeginTime, windowEndTime, realBeginTime, realEndTime);
    }

    @Override
    public String toString() {
        return "ViewTimeWindowInfo{" +
                "windowBeginTime=" + windowBeginTime +
                ", windowEndTime=" + windowEndTime +
                ", realBeginTime=" + realBeginTime +
                ", realEndTime=" + realEndTime +
                ", elementCount=" + elementCount +
                ", windowCount=" + windowCount +
                ", submitWindowCount=" + submitWindowCount +
                '}';
    }
}
