package i2f.springboot.perf.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2023/6/8 8:45
 * @desc
 */
@Data
@NoArgsConstructor
public class PerfIndex {
    private String name;
    private double value;

    public PerfIndex(String name, double value) {
        this.name = name;
        this.value = value;
    }
}
