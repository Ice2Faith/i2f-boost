/**
 * 安全工具
 */
import SecureConfig from "../secure-config";
import Base64Obfuscator from "../util/base64-obfuscator";
import B64 from "../util/base64";
import SecureHeader from "../data/secure-header";
import StringUtils from "../util/string-utils";
import SecureErrorCode from "../consts/secure-error-code";
import StringSignature from "../util/string-signature";
import SecureException from "../excception/secure-exception";
const SecureUtils={
    parseSecureHeader(key,separator,res){
        let value=this.getPossibleValue(res)
        return this.decodeSecureHeader(value,separator)
    },
    getPossibleValue(res){
        return res.headers[SecureConfig.headerName]
    },
    decodeSecureHeader(str,separator){
        if(StringUtils.isEmpty(str)){
            throw SecureException.newObj(SecureErrorCode.SECURE_HEADER_EMPTY(),"空安全头")
        }
        str=B64.decrypt(Base64Obfuscator.decode(str))
        let arr = str.split(separator);
        if(arr.length<4){
            throw SecureException.newObj(SecureErrorCode.SECURE_HEADER_STRUCTURE(),"不正确的安全头结构")
        }
        var ret = SecureHeader.newObj();
        ret.sign=arr[0]
        ret.nonce=arr[1]
        ret.randomKey=arr[2]
        ret.rsaSign=arr[3]
        if(StringUtils.isEmpty(ret.sign)){
            throw SecureException.newObj(SecureErrorCode.SECURE_HEADER_SIGN_EMPTY(),"空安全头签名")
        }
        if(StringUtils.isEmpty(ret.nonce)){
            throw SecureException.newObj(SecureErrorCode.SECURE_HEADER_NONCE_EMPTY(),"空安全头一次性标记")
        }
        if(StringUtils.isEmpty(ret.randomKey)){
            throw SecureException.newObj(SecureErrorCode.SECURE_HEADER_RANDOM_KEY_EMPTY(),"空安全头随机秘钥")
        }
        if(StringUtils.isEmpty(ret.rsaSign)){
            throw SecureException.newObj(SecureErrorCode.SECURE_HEADER_RSA_SIGN_EMPTY(),"空安全头秘钥签名")
        }
        return ret
    },
    encodeSecureHeader(header,separator){
        let str=''
        str+=header.sign
        str+=separator
        str+=header.nonce
        str+=separator
        str+=header.randomKey
        str+=separator
        str+=header.rsaSign
        return Base64Obfuscator.encode(B64.encrypt(str),true)
    },
    makeSecureSign(body,header){
        if(StringUtils.isEmpty(body)){
            body=''
        }
        let text=''
        text+=header.nonce
        text+=header.randomKey
        text+=header.rsaSign
        text+=body
        let sign=StringSignature.sign(text)
        return sign
    },
    verifySecureHeader(body,header){
        let sign=this.makeSecureSign(body,header)
        return sign==header.sign
    },
    decodeEncTrueUrl(encodeUrl){
        let text=Base64Obfuscator.decode(encodeUrl)
        let arr=text.split(';')
        if(arr.length!=2){
            throw SecureException.newObj(SecureErrorCode.BAD_SECURE_REQUEST(),"不正确的URL请求")
        }
        let sign=arr[0]
        let url=arr[1]
        let usign=StringSignature.sign(url)
        if(usign!=sign){
            throw SecureException.newObj(SecureErrorCode.BAD_SIGN(),"签名验证失败")
        }
        let trueUrl=B64.decrypt(Base64Obfuscator.decode(url))
        return trueUrl
    },
    encodeEncTrueUrl(trueUrl){
        let url=Base64Obfuscator.encode(B64.encrypt(trueUrl),false)
        let sign=StringSignature.sign(url)
        let text=Base64Obfuscator.encode(sign+';'+url,false)
        return encodeURIComponent(text)
    }
}

export default SecureUtils