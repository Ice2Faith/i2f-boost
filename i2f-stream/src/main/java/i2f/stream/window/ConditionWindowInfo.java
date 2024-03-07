package i2f.stream.window;

import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/3/7 15:41
 * @desc
 */
public class ConditionWindowInfo<E> extends WindowInfo {
    private E condition;

    public E getCondition() {
        return condition;
    }

    public void setCondition(E condition) {
        this.condition = condition;
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
        ConditionWindowInfo<?> that = (ConditionWindowInfo<?>) o;
        return Objects.equals(condition, that.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), condition);
    }

    @Override
    public String toString() {
        return "ConditionWindowInfo{" +
                "condition=" + condition +
                ", elementCount=" + elementCount +
                ", windowCount=" + windowCount +
                ", submitWindowCount=" + submitWindowCount +
                '}';
    }
}
