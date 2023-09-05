package i2f.secure;

import i2f.core.security.jce.encrypt.std.asymmetric.AsymmetricEncryptor;
import i2f.secure.crypto.Sm2CryptoEncryptor;
import i2f.secure.crypto.Sm2CryptoUtils;
import i2f.springboot.application.WarBootApplication;
import i2f.springboot.cors.EnableCorsConfig;
import i2f.springboot.secure.EnableSecureConfig;
import i2f.springboot.secure.crypto.SecureProvider;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.KeyPair;


@EnableCorsConfig
@EnableSecureConfig
@SpringBootApplication
public class I2fSecureApplication extends WarBootApplication {

    public static void main(String[] args) {
        SecureProvider.asymmetricEncryptorSupplier = () -> new Sm2CryptoEncryptor(Sm2CryptoUtils.MODE_C1C2C3);


        SecureProvider.asymmetricKeyPairSupplier = (len) -> {
            try {
                KeyPair keyPair = Sm2CryptoUtils.genKeyPair();

                AsymmetricEncryptor encryptor = SecureProvider.asymmetricEncryptorSupplier.get();
                return encryptor.encodeKeyPair(keyPair);
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        };


        startup(I2fSecureApplication.class, args);
    }

}
