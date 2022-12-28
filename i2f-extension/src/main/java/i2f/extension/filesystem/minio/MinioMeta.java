package i2f.extension.filesystem.minio;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/7/8 9:10
 * @desc
 */
@Data
@NoArgsConstructor
public class MinioMeta {
    // 连接URL，也就是endpoint
    private String url;
    // 访问秘钥
    private String accessKey;
    // 秘钥
    private String secretKey;
}
