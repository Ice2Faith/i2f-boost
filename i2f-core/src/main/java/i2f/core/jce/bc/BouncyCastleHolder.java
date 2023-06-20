package i2f.core.jce.bc;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Provider;
import java.security.Security;

/**
 * @author Ice2Faith
 * @date 2023/6/20 10:44
 * @desc
 */
public class BouncyCastleHolder {
    public static BouncyCastleProvider provider = new BouncyCastleProvider();
    public static String PROVIDER_NAME = BouncyCastleProvider.PROVIDER_NAME;

    public static void registry() {
        Provider provider = Security.getProvider(PROVIDER_NAME);
        if (provider == null) {
            Security.addProvider(BouncyCastleHolder.provider);
        }
    }
}
