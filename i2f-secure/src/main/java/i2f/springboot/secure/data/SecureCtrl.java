package i2f.springboot.secure.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2023/6/13 9:54
 * @desc
 */
@Data
@NoArgsConstructor
public class SecureCtrl {
    public boolean in = false;
    public boolean out = false;

    public SecureCtrl(boolean in, boolean out) {
        this.in = in;
        this.out = out;
    }
}
