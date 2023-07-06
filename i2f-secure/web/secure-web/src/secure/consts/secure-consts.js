/**
 * 定义常量
*/
const SecureConsts={
    RSA_KEY_SIZE_1024: () => 1024,
    RSA_KEY_SIZE_2048: () => 2048,

    AES_KEY_SIZE_128: () => 128,
    AES_KEY_SIZE_192: () => 192,
    AES_KEY_SIZE_256: () => 256,

    // 默认安全头
    DEFAULT_SECURE_HEADER_NAME: () => "sswh",

    DEFAULT_SECURE_PARAMETER_NAME: () => "sswp",

    DEFAULT_SECURE_CLIENT_ASYM_SIGN_NAME: () => "sswcas",

    // 安全头的分隔符
    DEFAULT_HEADER_SEPARATOR: () => ";",

    // 动态asym公钥的响应头
    SECURE_DYNAMIC_KEY_HEADER: () => "skey",
    SECURE_CLIENT_KEY_HEADER: () => "wkey",

    // 安全Body加密标记头
    SECURE_DATA_HEADER: () => "secure-data",

    // 安全Param加密标记头
    SECURE_PARAMS_HEADER:()=>"secure-param",

    // 安全URL编码标记头
    SECURE_URL_HEADER:()=>"secure-url",

    // 启用标记位
    FLAG_ENABLE:()=>"true",

    // 禁用标记位
    FLAG_DISABLE:()=>"false",

    // 默认的URL编码请求路径
    ENC_URL_PATH:()=>"/enc/",
}

export default SecureConsts