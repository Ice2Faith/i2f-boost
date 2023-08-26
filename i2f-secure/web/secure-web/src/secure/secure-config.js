/**
 * 主配置
 */
import SecureConsts from './consts/secure-consts'

const SecureConfig = {
  enable: true,
  // Asymm秘钥长度，默认1024，可选1024,2048
  asymKeySize: SecureConsts.RSA_KEY_SIZE_1024(),
  // Symm秘钥长度，默认128，可选128,192,256
  symmKeySize: SecureConsts.AES_KEY_SIZE_128(),
  // 用于存储安全头的请求头名称，默认sswh
  headerName: SecureConsts.DEFAULT_SECURE_HEADER_NAME(),
  // 动态刷新Asym秘钥的响应头，默认skey
  dynamicKeyHeaderName: SecureConsts.SECURE_DYNAMIC_KEY_HEADER(),
  clientKeyHeaderName: SecureConsts.SECURE_CLIENT_KEY_HEADER(),
  clientAsymSignName: SecureConsts.DEFAULT_SECURE_CLIENT_ASYM_SIGN_NAME(),
  // 安全头格式的分隔符，默认;
  headerSeparator: SecureConsts.DEFAULT_HEADER_SEPARATOR(),
  // 指定在使用编码URL转发时的转发路径
  encUrlPath: SecureConsts.ENC_URL_PATH(),
  // 安全URL参数的参数名称
  parameterName: SecureConsts.DEFAULT_SECURE_PARAMETER_NAME(),
  // 客户端秘钥对的获取策略，是否是本地生成交换策略
  enableSwapAsymKey: SecureConsts.DEFAULT_SECURE_SWAP_ASYM_KEY(),
  // 是否开启详细日志
  // 在正式环境中，请禁用
  enableDebugLog: process.env.NODE_ENV != 'prod',
  // 支持两种模式：1 字符串模式，完全匹配；2 正则表达式模式，需要是正则表达式
  // 加密配置的白名单url
  whileList: ['/secure/key', '/secure/clientKey', '/secure/swapKey'],
  // 加密URL的URL白名单
  encWhiteList: ['/login', '/logout', /^\/download\/.*$/]
}

export default SecureConfig
