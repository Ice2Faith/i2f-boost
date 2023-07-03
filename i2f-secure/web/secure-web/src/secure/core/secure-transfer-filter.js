/**
 * 自动加解密的核心过滤器
 */
import SecureConfig from "../secure-config";
import SecureTransfer from "./secure-transfer";
import StringUtils from "../util/string-utils";
import SecureUtils from "../util/secure-utils";
import SecureConsts from "../consts/secure-consts";
import SecureHeader from "../data/secure-header";
import ObjectUtils from "../util/ObjectUtils";
import qs from 'qs'
import SecureException from "@/secure/excception/secure-exception";
import SecureErrorCode from "@/secure/consts/secure-error-code";

const SecureTransferFilter = {
    getRequestContentType(config) {
        let contentType = null
        let method = config.method
        if (contentType == null) {
            Object.keys(config.headers).forEach((key) => {
                let lkey = key.toLowerCase()
                if (lkey == 'content-type') {
                    contentType = config.headers[key]
                }
            })
        }
        if (contentType == null) {
            let methodHeader = config.headers[method]
            Object.keys(methodHeader).forEach((key) => {
                let lkey = key.toLowerCase()
                if (lkey == 'content-type') {
                    contentType = methodHeader[key]
                }
            })
        }
        return contentType
    },
    isInWhiteList(url, whiteList) {
        if (whiteList == null || whiteList == undefined) {
            return false
        }
        if (!url.startsWith('/')) {
            url = '/' + url
        }
        for (let i = 0; i < whiteList.length; i++) {
            let wurl = whiteList[i]
            if (!wurl.startsWith('/')) {
                wurl = '/' + wurl
            }
            if (url == wurl) {
                return true
            }
        }
        return false
    },
    // 请求加密过滤，config需要包含属性：data,params,headers
    // 分别对应请求体，请求参数，请求头
    // 根据请求头中的SECURE_DATA_HEADER头值为SECURE_HEADER_ENABLE时，进行自动加密请求体
    // 如果同时请求头SECURE_PARAMS_HEADER值为SECURE_HEADER_ENABLE时，请求参数也会被加密
    // 也就是说，如果请求头中，这两个请求头不存在，或者值不为SECURE_HEADER_ENABLE，都将不会处理
    // 也就是手动处理，部分参数的情形
    requestFilter(config) {
        if (SecureTransfer.existsAsymPriKey()) {
            config.headers[SecureConfig.clientAsymSignName] = SecureTransfer.getAsymPriSign()
        }

        if (config.data) {
            if (SecureUtils.typeOf(config.data) == 'formdata') {
                config.headers['Content-Type'] = 'multipart/form-data'
            }
        }
        let contentType = this.getRequestContentType(config)
        if (StringUtils.isEmpty(contentType)) {
            contentType = ""
        }
        contentType = contentType.toLowerCase()
        if (contentType.indexOf("multipart/form-data") >= 0) {
            return config
        }
        if (this.isInWhiteList(config.url, SecureConfig.whileList)) {
            return config
        }
        if (SecureConfig.enableDebugLog) {
            console.log('request:beforeSecureConfig:', config.url, ObjectUtils.deepClone(config))
        }
        if (config.headers[SecureConsts.SECURE_URL_HEADER()] === SecureConsts.FLAG_ENABLE()) {
            delete config.headers[SecureConsts.SECURE_URL_HEADER()]
            if (!this.isInWhiteList(config.url, SecureConfig.encWhiteList)) {
                config.url = SecureConfig.encUrlPath + SecureUtils.encodeEncTrueUrl(config.url)
            }
        }
        let secureData = false
        let secureParams = false
        if (config.headers[SecureConsts.SECURE_DATA_HEADER()] === SecureConsts.FLAG_ENABLE()) {
            delete config.headers[SecureConsts.SECURE_DATA_HEADER()]
            secureData = true
        }
        if (config.headers[SecureConsts.SECURE_PARAMS_HEADER()] === SecureConsts.FLAG_ENABLE()) {
            delete config.headers[SecureConsts.SECURE_PARAMS_HEADER()]
            secureParams = true
        }
        if (!secureData && !secureParams) {
            return config
        }
        let symmKey = SecureTransfer.symmKeyGen(SecureConfig.symmKeySize / 8);
        let requestHeader = SecureHeader.newObj()
        requestHeader.nonce = new Date().getTime().toString(16) + '' + Math.floor(Math.random() * 0x0fff).toString(16);
        requestHeader.randomKey = SecureTransfer.getRequestSecureHeader(symmKey)
        requestHeader.serverAsymSign = SecureTransfer.getAsymSign()
        let signText = ''
        if (secureData) {
            if (config.data) {
                let method = config.method
                if (method == 'put' || method == 'post') {
                    if (!StringUtils.isEmpty(contentType)) {
                        if (contentType.indexOf("application/x-www-form-urlencoded") >= 0) {
                            config.data = qs.stringify(config.data)
                        }
                    }
                }
                config.data = SecureTransfer.encrypt(config.data, symmKey);
                signText += config.data
            }

        }
        if (secureParams) {
            if (config.params) {
                let paramText = qs.stringify(config.params)
                config.params = {}
                config.params[SecureConfig.parameterName] = SecureTransfer.encrypt(paramText, symmKey)
                signText += config.params[SecureConfig.parameterName]
            }
        }
        requestHeader.clientAsymSign=SecureTransfer.getAsymPriSign()
        requestHeader.sign = SecureUtils.makeSecureSign(signText, requestHeader)
        requestHeader.digital = SecureTransfer.getRequestDigitalHeader(requestHeader.sign)
        if (SecureConfig.enableDebugLog) {
            console.log('request:secureHeader:', config.url, ObjectUtils.deepClone(requestHeader))
        }
        config.headers[SecureConfig.headerName] = SecureUtils.encodeSecureHeader(requestHeader, SecureConfig.headerSeparator)

        if (SecureConfig.enableDebugLog) {
            console.log('request:afterSecureConfig:', config.url, ObjectUtils.deepClone(config))
        }
        return config;
    },
    // 响应解密过滤，res需要包含headers和data
    // 分别表示响应头和响应体
    // 当响应头中存在SECURE_DATA_HEADER时，将会自动解密响应体
    responseFilter(res) {
        if (SecureConfig.enableDebugLog) {
            console.log('response:beforeSecureRes:', res.config.url, ObjectUtils.deepClone(res))
        }
        let skeyHeader = res.headers[SecureConsts.SECURE_DYNAMIC_KEY_HEADER()];
        if (!StringUtils.isEmpty(skeyHeader)) {
            if (SecureConfig.enableDebugLog) {
                console.log('response:updateAsymPubKey:', res.config.url, skeyHeader)
            }
            SecureTransfer.saveAsymPubKey(skeyHeader);
        }

        let headerValue = res.headers[SecureConfig.headerName]
        if (StringUtils.isEmpty(headerValue)) {
            return res
        }
        let responseHeader = SecureUtils.parseSecureHeader(SecureConfig.headerName, SecureConfig.headerSeparator, res)
        responseHeader.clientAsymSign=SecureTransfer.getAsymPriSign()
        if (SecureConfig.enableDebugLog) {
            console.log('response:secureHeader:', res.config.url, ObjectUtils.deepClone(responseHeader))
        }
        let ok = SecureUtils.verifySecureHeader(res.data,responseHeader);
        if (!ok) {
            throw SecureException.newObj(SecureErrorCode.BAD_SIGN, "签名验证失败");
        }

        let digital = SecureTransfer.getResponseDigitalHeader(responseHeader.digital);
        if (digital == null) {
            throw SecureException.newObj(SecureErrorCode.BAD_DIGITAL(), "数字签名验证失败，请重试！");
        }
        if (digital != responseHeader.sign) {
            throw SecureException.newObj(SecureErrorCode.BAD_DIGITAL(), "数字签名验证失败，请重试！");
        }

        let symmKey = SecureTransfer.getResponseSecureHeader(responseHeader.randomKey);
        if (symmKey == null) {
            throw SecureException.newObj(SecureErrorCode.BAD_RANDOM_KEY(), "随机秘钥无效或已失效，请重试！");
        }

        res.data = SecureTransfer.decrypt(res.data, symmKey);
        if (SecureConfig.enableDebugLog) {
            console.log('response:afterSecureRes:', res.config.url, ObjectUtils.deepClone(res))
        }
        return res;
    },
}


export default SecureTransferFilter;
