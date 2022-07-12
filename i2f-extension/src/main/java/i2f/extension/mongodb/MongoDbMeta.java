package i2f.extension.mongodb;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/7/12 19:34
 * @desc
 */
@Data
@NoArgsConstructor
public class MongoDbMeta {
    private String host;
    private int port=27017;
    private String source;
    private String username;
    private String password;
    private int connectTimeout=30000;
}
