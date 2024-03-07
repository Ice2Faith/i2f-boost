package i2f.stream.window;

import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/3/7 15:41
 * @desc
 */
public class ViewWindowInfo extends WindowInfo {
    protected int beforeCount;
    protected int afterCount;

    public int getBeforeCount() {
        return beforeCount;
    }

    public void setBeforeCount(int beforeCount) {
        this.beforeCount = beforeCount;
    }

    public int getAfterCount() {
        return afterCount;
    }

    public void setAfterCount(int afterCount) {
        this.afterCount = afterCount;
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
        ViewWindowInfo that = (ViewWindowInfo) o;
        return beforeCount == that.beforeCount &&
                afterCount == that.afterCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), beforeCount, afterCount);
    }

    @Override
    public String toString() {
        return "ViewWindowInfo{" +
                "beforeCount=" + beforeCount +
                ", afterCount=" + afterCount +
                ", elementCount=" + elementCount +
                ", windowCount=" + windowCount +
                ", submitWindowCount=" + submitWindowCount +
                '}';
    }
}
