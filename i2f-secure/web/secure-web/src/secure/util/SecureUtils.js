/**
 * 安全工具
 */
import SecureConfig from '../SecureConfig'
import Base64Obfuscator from './Base64Obfuscator'
import Base64Util from './Base64Util'
import SecureHeader from '../data/SecureHeader'
import StringUtils from './StringUtils'
import SecureErrorCode from '../consts/SecureErrorCode'
import SecureException from '../excception/SecureException'
import SignatureUtil from '../crypto/SignatureUtil'

const SecureUtils = {
  typeOf(data) {
    return Object.prototype.toString.call(data).slice(8, -1).toLowerCase()
  },
  parseSecureHeader(key, separator, res) {
    const value = this.getPossibleValue(res)
    return this.decodeSecureHeader(value, separator)
  },
  getPossibleValue (res) {
    return res.headers[SecureConfig.headerName]
  },
  decodeSecureHeader (str, separator) {
    if (StringUtils.isEmpty(str)) {
      throw SecureException.newObj(SecureErrorCode.SECURE_HEADER_EMPTY(), '空安全头')
    }
    str = Base64Util.decrypt(Base64Obfuscator.decode(str))
    const arr = str.split(separator)
    if (arr.length < 5) {
      throw SecureException.newObj(SecureErrorCode.SECURE_HEADER_STRUCTURE(), '不正确的安全头结构')
    }
    const ret = SecureHeader.newObj()
    ret.sign = arr[0]
    ret.nonce = arr[1]
    ret.randomKey = arr[2]
    ret.serverAsymSign = arr[3]
    ret.digital = arr[4]
    if (StringUtils.isEmpty(ret.sign)) {
      throw SecureException.newObj(SecureErrorCode.SECURE_HEADER_SIGN_EMPTY(), '空安全头签名')
    }
    if (StringUtils.isEmpty(ret.nonce)) {
      throw SecureException.newObj(SecureErrorCode.SECURE_HEADER_NONCE_EMPTY(), '空安全头一次性标记')
    }
    if (StringUtils.isEmpty(ret.randomKey)) {
      throw SecureException.newObj(SecureErrorCode.SECURE_HEADER_RANDOM_KEY_EMPTY(), '空安全头随机秘钥')
    }
    if (StringUtils.isEmpty(ret.serverAsymSign)) {
      throw SecureException.newObj(SecureErrorCode.SECURE_HEADER_ASYM_SIGN_EMPTY(), '空安全头秘钥签名')
    }
    if (StringUtils.isEmpty(ret.digital)) {
      throw SecureException.newObj(SecureErrorCode.SECURE_HEADER_DIGITAL_EMPTY(), '空安全头数字签名')
    }
    return ret
  },
  encodeSecureHeader (header, separator) {
    let str = ''
    str += header.sign
    str += separator
    str += header.nonce
    str += separator
    str += header.randomKey
    str += separator
    str += header.serverAsymSign
    str += separator
    str += header.digital
    return Base64Obfuscator.encode(Base64Util.encrypt(str), true)
  },
  makeSecureSign (body, header) {
    if (StringUtils.isEmpty(body)) {
      body = ''
    }
    let text = ''
    text += header.nonce
    text += header.randomKey
    text += header.serverAsymSign
    text += header.clientAsymSign
    text += body
    const sign = SignatureUtil.sign(text)
    return sign
  },
  verifySecureHeader (body, header) {
    const sign = this.makeSecureSign(body, header)
    return sign == header.sign
  },
  decodeEncTrueUrl (encodeUrl) {
    let text = Base64Util.decrypt(text)
    text = Base64Obfuscator.decode(encodeUrl)
    const arr = text.split(';')
    if (arr.length != 2) {
      throw SecureException.newObj(SecureErrorCode.BAD_SECURE_REQUEST(), '不正确的URL请求')
    }
    const sign = arr[0]
    const url = arr[1]
    const usign = SignatureUtil.sign(url)
    if (usign != sign) {
      throw SecureException.newObj(SecureErrorCode.BAD_SIGN(), '签名验证失败')
    }
    const trueUrl = Base64Util.decrypt(Base64Obfuscator.decode(url))
    return trueUrl
  },
  encodeEncTrueUrl (trueUrl) {
    const url = Base64Obfuscator.encode(Base64Util.encrypt(trueUrl), false)
    const sign = SignatureUtil.sign(url)
    let text = Base64Obfuscator.encode(sign + ';' + url, false)
    text = Base64Util.encrypt(text)
    return encodeURIComponent(text)
  }
}

export default SecureUtils
