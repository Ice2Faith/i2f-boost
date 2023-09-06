package i2f.secure;

import i2f.springboot.application.WarBootApplication;
import i2f.springboot.cors.EnableCorsConfig;
import i2f.springboot.secure.EnableSecureConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@EnableCorsConfig
@EnableSecureConfig
@SpringBootApplication
public class I2fSecureApplication extends WarBootApplication {

    public static void main(String[] args) {
//        SecureProvider.asymmetricEncryptorSupplier = SecureProviderPresets.asymmetricEncryptorSupplier_SM2;
//
//        SecureProvider.asymmetricKeyPairSupplier = SecureProviderPresets.asymmetricKeyPairSupplier_SM2;
//
//        SecureProvider.symmetricEncryptorSupplier=SecureProviderPresets.symmetricEncryptorSupplier_SM4;
//
//        SecureProvider.symmetricKeySupplier=SecureProviderPresets.symmetricKeySupplier_SM4;
//
//        SecureProvider.messageDigesterSupplier=SecureProviderPresets.messageDigesterSupplier_SM3;

        startup(I2fSecureApplication.class, args);
    }

}
