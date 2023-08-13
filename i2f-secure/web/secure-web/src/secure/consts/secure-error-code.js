/**
 * 定义异常的错误码
 */
const SecureErrorCode = {
    SECURE_HEADER_EMPTY: () => 101,
    SECURE_HEADER_STRUCTURE: () => 102,
    SECURE_HEADER_SIGN_EMPTY: () => 103,
    SECURE_HEADER_NONCE_EMPTY: () => 104,
    SECURE_HEADER_RANDOM_KEY_EMPTY: () => 105,
    SECURE_HEADER_ASYM_SIGN_EMPTY: () => 106,
    SECURE_HEADER_DIGITAL_EMPTY: () => 107,

    BAD_SIGN: () => 201,
    BAD_RANDOM_KEY: () => 202,
    BAD_NONCE: () => 203,
    BAD_DIGITAL: () => 204,

    BAD_SECURE_REQUEST: () => 301
}
export default SecureErrorCode
