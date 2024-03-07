package i2f.stream.window;

import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/3/7 15:42
 * @desc
 */
public class WindowInfo {
    protected long elementCount;
    protected long windowCount;
    protected long submitWindowCount;

    public long getElementCount() {
        return elementCount;
    }

    public void setElementCount(long elementCount) {
        this.elementCount = elementCount;
    }

    public long getWindowCount() {
        return windowCount;
    }

    public void setWindowCount(long windowCount) {
        this.windowCount = windowCount;
    }

    public long getSubmitWindowCount() {
        return submitWindowCount;
    }

    public void setSubmitWindowCount(long submitWindowCount) {
        this.submitWindowCount = submitWindowCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WindowInfo that = (WindowInfo) o;
        return elementCount == that.elementCount &&
                windowCount == that.windowCount &&
                submitWindowCount == that.submitWindowCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(elementCount, windowCount, submitWindowCount);
    }

    @Override
    public String toString() {
        return "WindowInfo{" +
                "elementCount=" + elementCount +
                ", windowCount=" + windowCount +
                ", submitWindowCount=" + submitWindowCount +
                '}';
    }
}
