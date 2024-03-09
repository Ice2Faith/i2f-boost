package i2f.packet.rule;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/3/9 17:26
 * @desc
 */
public class RuleByte {
    protected byte data;
    protected long dataIndex;

    protected boolean isHead;

    protected int currentIndex;
    protected int currentCount;

    public byte getData() {
        return data;
    }

    public void setData(byte data) {
        this.data = data;
    }

    public long getDataIndex() {
        return dataIndex;
    }

    public void setDataIndex(long dataIndex) {
        this.dataIndex = dataIndex;
    }

    public boolean isHead() {
        return isHead;
    }

    public void setHead(boolean head) {
        isHead = head;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

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
        RuleByte ruleByte = (RuleByte) o;
        return data == ruleByte.data &&
                dataIndex == ruleByte.dataIndex &&
                isHead == ruleByte.isHead &&
                currentIndex == ruleByte.currentIndex &&
                currentCount == ruleByte.currentCount;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(data, dataIndex, isHead, currentIndex, currentCount);
        return result;
    }

    @Override
    public String toString() {
        return "RuleByte{" +
                "data=" + data +
                ", dataIndex=" + dataIndex +
                ", isHead=" + isHead +
                ", currentIndex=" + currentIndex +
                ", currentCount=" + currentCount +
                '}';
    }
}
