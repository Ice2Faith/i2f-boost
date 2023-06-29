package i2f.springboot.secure.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2023/6/13 10:11
 * @desc
 */
@Data
@NoArgsConstructor
public class SecureHeader {
    public String sign;
    public String nonce;
    public String randomKey;
    public String asymSign;
}
