/**
 * 主配置
 */
import SecureConsts from "./consts/secure-consts";

const SecureConfig={
   // Symm秘钥长度，默认128，可选128,192,256
   symmKeySize: SecureConsts.AES_KEY_SIZE_128(),
   // 随机秘钥生成的随机数的最大值，默认8192
   randomKeyBound: 8192,
   // 用于存储安全头的请求头名称，默认sswh
   headerName: SecureConsts.DEFAULT_SECURE_HEADER_NAME(),
   // 动态刷新Asym秘钥的响应头，默认skey
   dynamicKeyHeaderName: SecureConsts.SECURE_DYNAMIC_KEY_HEADER(),
   clientAsymSignName: SecureConsts.DEFAULT_SECURE_CLIENT_ASYM_SIGN_NAME(),
   // 安全头格式的分隔符，默认;
   headerSeparator: SecureConsts.DEFAULT_HEADER_SEPARATOR(),
   // 指定在使用编码URL转发时的转发路径
   encUrlPath: SecureConsts.ENC_URL_PATH(),
   // 安全URL参数的参数名称
   parameterName: SecureConsts.DEFAULT_SECURE_PARAMETER_NAME(),
   // 是否开启详细日志
   // 在正式环境中，请禁用
   enableDebugLog: true,
   // 加密配置的白名单url
   whileList:['/secure/key','/login'],
   // 加密URL的URL白名单
   encWhiteList:['/secure/key','/login']
}

export default SecureConfig