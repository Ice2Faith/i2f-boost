package i2f.stream.window;

import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/3/7 15:41
 * @desc
 */
public class SlideWindowInfo extends WindowInfo {
    protected int currentCount;

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
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
        SlideWindowInfo that = (SlideWindowInfo) o;
        return currentCount == that.currentCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),  currentCount);
    }

    @Override
    public String toString() {
        return "SlideWindowInfo{" +
                "currentCount=" + currentCount +
                ", elementCount=" + elementCount +
                ", windowCount=" + windowCount +
                ", submitWindowCount=" + submitWindowCount +
                '}';
    }
}
