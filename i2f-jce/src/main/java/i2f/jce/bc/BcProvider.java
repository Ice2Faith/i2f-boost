package i2f.jce.bc;

import java.security.Provider;
import java.security.Security;

/**
 * @author Ice2Faith
 * @date 2024/3/28 11:26
 * @desc
 */
public class BcProvider {
    public static final String PROVIDER_NAME = "BC";
    public static final String PROVIDER_CLASS_NAME = "org.bouncycastle.jce.provider.BouncyCastleProvider";
    public static final String MAVEN_DEPENDENCY = "" +
            "<dependency>\n" +
            "    <groupId>org.bouncycastle</groupId>\n" +
            "    <artifactId>bcprov-jdk15to18</artifactId>\n" +
            "    <version>1.69</version>\n" +
            "</dependency>";
    private volatile static Provider BC_PROVIDER = null;

    static {
        registryProvider();
    }

    public static void registryProvider() {
        if (BC_PROVIDER != null) {
            return;
        }
        if(BC_PROVIDER==null){
            synchronized (BcProvider.class){
                try {
                    BC_PROVIDER = Security.getProvider(PROVIDER_NAME);
                    if (BC_PROVIDER != null) {
                        return;

                    }
                } catch (Exception e) {

                }
            }
        }

        synchronized (BcProvider.class){
            try {
                Class<?> clazz = Class.forName(PROVIDER_CLASS_NAME);
                Object provider = clazz.newInstance();
                Security.addProvider((Provider) provider);
                BC_PROVIDER=(Provider) provider;
            } catch (Exception e) {

            }
        }
    }
}
