package i2f.springboot.secure.consts;

/**
 * @author Ice2Faith
 * @date 2023/6/13 8:51
 * @desc
 */
public interface SecureConsts {
    int RSA_KEY_SIZE_1024 = 1024;
    int RSA_KEY_SIZE_2048 = 2048;

    int AES_KEY_SIZE_128 = 128;
    int AES_KEY_SIZE_192 = 192;
    int AES_KEY_SIZE_256 = 256;

    // secure sign web header
    String DEFAULT_SECURE_HEADER_NAME = "sswh";

    // secure sign web parameter
    String DEFAULT_SECURE_PARAMETER_NAME = "sswp";

    String FILTER_EXCEPTION_ATTR_KEY = "SECURE_FILTER_EXCEPTION";

    String FILTER_DECRYPT_HEADER = "SECURE_FILTER_DECRYPT";

    String FILTER_ENCRYPT_HEADER = "SECURE_FILTER_ENCRYPT";

    String FLAG_ENABLE = "true";

    String FLAG_DISABLE = "false";

    String DEFAULT_HEADER_SEPARATOR = ";";

    String SECURE_HEADER_ATTR_KEY = "SECURE_HEADER";


    // 是否是String返回值类型标记
    String STRING_RETURN_HEADER = "SECURE_RETURN_STRING";

    String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";

    String RSA_KEY_FILE_NAME = "rsa.key";

    // 是否包含动态刷新的RSA公钥签名/公钥头
    String SECURE_DYNAMIC_KEY_HEADER = "skey";

    String SECURE_REQUIRE_RESPONSE = "SECURE_REQUIRE_RESPONSE";

    String DEFAULT_ENC_URL_PATH = "/enc/";

}
