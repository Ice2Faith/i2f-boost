package i2f.core.security.secret.impl.ram.api;

import i2f.core.security.secret.api.code.ICoder;
import i2f.core.security.secret.util.SecretUtil;

/**
 * @author ltb
 * @date 2022/10/19 16:41
 * @desc
 */
public class CharsCoder implements ICoder {
    public int len = 16;

    public CharsCoder() {
    }

    public CharsCoder(int len) {
        this.len = len;
    }

    @Override
    public byte[] code() {
        String str = codeStr();
        return SecretUtil.str2utf8(str);
    }

    public String codeStr() {
        String ret = "";
        for (int i = 0; i < len; i++) {
            ret += SecretUtil.randAlphabet();
        }
        return ret;
    }
}
