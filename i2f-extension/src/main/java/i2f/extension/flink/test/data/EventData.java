package i2f.extension.flink.test.data;

import java.sql.Timestamp;

/**
 * @author ltb
 * @date 2022/11/14 13:52
 * @desc
 */
public class EventData {
    public long timestamp;
    public String data;

    public EventData() {
    }

    public EventData(long timestamp, String data) {
        this.timestamp = timestamp;
        this.data = data;
    }

    @Override
    public String toString() {
        return "EventData{" + "\n" +
                "timestamp=" + new Timestamp(timestamp) + "\n" +
                ", data='" + data + '\'' + "\n" +
                '}' + "\n";
    }
}
