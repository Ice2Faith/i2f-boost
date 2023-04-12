package i2f.extension.zookeeper;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2023/4/12 13:51
 * @desc
 */
@Data
@NoArgsConstructor
public class ZookeeperConfig {
    private String connectString = "localhost:2181";
    private int sessionTimeout = -1;
}
