/**
 * 主配置
 */
import SecureConsts from "./consts/secure-consts";

const SecureConfig={
   // 随机秘钥生成的随机数的最大值，默认8192
   randomKeyBound: 8192,
   // 用于存储安全头的请求头名称，默认sswh
   headerName: SecureConsts.DEFAULT_SECURE_HEADER_NAME(),
   // 动态刷新RSA秘钥的响应头，默认skey
   dynamicKeyHeaderName:SecureConsts.SECURE_DYNAMIC_KEY_HEADER(),
   // 安全头格式的分隔符，默认;
   headerSeparator:SecureConsts.DEFAULT_HEADER_SEPARATOR(),
   // 指定在使用编码URL转发时的转发路径
   encUrlPath:SecureConsts.ENC_URL_PATH(),

   parameterName:SecureConsts.DEFAULT_SECURE_PARAMETER_NAME(),
   // 是否开启详细日志
   // 在正式环境中，请禁用
   enableDebugLog: true,

   whileList:['/secure/key','/login'],

   encWhiteList:['/secure/key','/login']
}

export default SecureConfig