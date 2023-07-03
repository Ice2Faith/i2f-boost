/**
 * 安全工具
 */
import SecureConfig from "../secure-config";
import Base64Obfuscator from "../util/base64-obfuscator";
import B64 from "../util/base64";
import SecureHeader from "../data/secure-header";
import StringUtils from "../util/string-utils";
import SecureErrorCode from "../consts/secure-error-code";
import SecureException from "../excception/secure-exception";
import SignatureUtil from "../crypto/SignatureUtil";

const SecureUtils = {
    typeOf(data) {
        return Object.prototype.toString.call(data).slice(8, -1).toLowerCase()
    },
    parseSecureHeader(key, separator, res) {
        let value = this.getPossibleValue(res)
        return this.decodeSecureHeader(value, separator)
    },
    getPossibleValue(res) {
        return res.headers[SecureConfig.headerName]
    },
    decodeSecureHeader(str, separator) {
        if (StringUtils.isEmpty(str)) {
            throw SecureException.newObj(SecureErrorCode.SECURE_HEADER_EMPTY(), "空安全头")
        }
        str = B64.decrypt(Base64Obfuscator.decode(str))
        let arr = str.split(separator);
        if (arr.length < 5) {
            throw SecureException.newObj(SecureErrorCode.SECURE_HEADER_STRUCTURE(), "不正确的安全头结构")
        }
        var ret = SecureHeader.newObj();
        ret.sign = arr[0]
        ret.nonce = arr[1]
        ret.randomKey = arr[2]
        ret.serverAsymSign = arr[3]
        ret.digital = arr[4]
        if (StringUtils.isEmpty(ret.sign)) {
            throw SecureException.newObj(SecureErrorCode.SECURE_HEADER_SIGN_EMPTY(), "空安全头签名")
        }
        if (StringUtils.isEmpty(ret.nonce)) {
            throw SecureException.newObj(SecureErrorCode.SECURE_HEADER_NONCE_EMPTY(), "空安全头一次性标记")
        }
        if (StringUtils.isEmpty(ret.randomKey)) {
            throw SecureException.newObj(SecureErrorCode.SECURE_HEADER_RANDOM_KEY_EMPTY(), "空安全头随机秘钥")
        }
        if (StringUtils.isEmpty(ret.serverAsymSign)) {
            throw SecureException.newObj(SecureErrorCode.SECURE_HEADER_ASYM_SIGN_EMPTY(), "空安全头秘钥签名")
        }
        if (StringUtils.isEmpty(ret.digital)) {
            throw SecureException.newObj(SecureErrorCode.SECURE_HEADER_DIGITAL_EMPTY(), "空安全头数字签名");
        }
        return ret
    },
    encodeSecureHeader(header,separator) {
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
        return Base64Obfuscator.encode(B64.encrypt(str), true)
    },
    makeSecureSign(body,header){
        if(StringUtils.isEmpty(body)){
            body=''
        }
        let text=''
        text+=header.nonce
        text += header.randomKey
        text += header.serverAsymSign
        text += header.clientAsymSign
        text += body
        let sign = SignatureUtil.sign(text)
        return sign
    },
    verifySecureHeader(body,header){
        let sign=this.makeSecureSign(body,header)
        return sign==header.sign
    },
    decodeEncTrueUrl(encodeUrl){
        let text=B64.decrypt(text)
        text=Base64Obfuscator.decode(encodeUrl)
        let arr=text.split(';')
        if(arr.length!=2){
            throw SecureException.newObj(SecureErrorCode.BAD_SECURE_REQUEST(),"不正确的URL请求")
        }
        let sign=arr[0]
        let url = arr[1]
        let usign = SignatureUtil.sign(url)
        if(usign!=sign){
            throw SecureException.newObj(SecureErrorCode.BAD_SIGN(),"签名验证失败")
        }
        let trueUrl=B64.decrypt(Base64Obfuscator.decode(url))
        return trueUrl
    },
    encodeEncTrueUrl(trueUrl){
        let url = Base64Obfuscator.encode(B64.encrypt(trueUrl), false)
        let sign = SignatureUtil.sign(url)
        let text = Base64Obfuscator.encode(sign + ';' + url, false)
        text=B64.encrypt(text)
        return encodeURIComponent(text)
    }
}

export default SecureUtils