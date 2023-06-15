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

const SecureTransferFilter = {
    // 请求加密过滤，config需要包含属性：data,params,headers
    // 分别对应请求体，请求参数，请求头
    // 根据请求头中的SECURE_DATA_HEADER头值为SECURE_HEADER_ENABLE时，进行自动加密请求体
    // 如果同时请求头SECURE_PARAMS_HEADER值为SECURE_HEADER_ENABLE时，请求参数也会被加密
    // 也就是说，如果请求头中，这两个请求头不存在，或者值不为SECURE_HEADER_ENABLE，都将不会处理
    // 也就是手动处理，部分参数的情形
    requestFilter(config) {
        if(SecureConfig.enableDebugLog){
            console.log('request:beforeSecureConfig:',ObjectUtils.deepClone(config))
        }
        if (config.headers[SecureConsts.SECURE_URL_HEADER()] === SecureConsts.FLAG_ENABLE()) {
            delete config.headers[SecureConsts.SECURE_URL_HEADER()]
            config.url = SecureConfig.encUrlPath + SecureUtils.encodeEncTrueUrl(config.url)
        }
        if (config.headers[SecureConsts.SECURE_DATA_HEADER()] === SecureConsts.FLAG_ENABLE()) {
            delete config.headers[SecureConsts.SECURE_DATA_HEADER()]
            let aesKey = SecureTransfer.aesKeyGen16();
            let requestHeader = SecureHeader.newObj()
            requestHeader.nonce = new Date().getTime().toString(16) + '' + Math.floor(Math.random() * 0x0fff).toString(16);
            requestHeader.randomKey = SecureTransfer.getRequestSecureHeader(aesKey)
            requestHeader.rsaSign = SecureTransfer.getRsaSign()
            if (config.data) {
                let method=config.method
                if(method=='put' || method=='post'){
                    let contentType=null
                    Object.keys(config.headers).forEach((key)=>{
                        let lkey=key.toLowerCase()
                        if(lkey=='content-type'){
                            contentType=config.headers[key]
                        }
                    })
                    let methodHeader=config.headers[method]
                    if(contentType==null){
                        Object.keys(methodHeader).forEach((key)=>{
                            let lkey=key.toLowerCase()
                            if(lkey=='content-type'){
                                contentType=methodHeader[key]
                            }
                        })
                    }
                    if(!StringUtils.isEmpty(contentType)){
                        contentType=contentType.toLowerCase()
                        if(contentType.indexOf("application/x-www-form-urlencoded")>=0){
                            config.data=qs.stringify(config.data)
                        }
                    }

                }
                config.data = SecureTransfer.encrypt(config.data, aesKey);
            }

            requestHeader.sign = SecureUtils.makeSecureSign(config.data, requestHeader)
            if(SecureConfig.enableDebugLog){
                console.log('request:secureHeader:',ObjectUtils.deepClone(requestHeader))
            }
            config.headers[SecureConfig.headerName] = SecureUtils.encodeSecureHeader(requestHeader, SecureConfig.headerSeparator)

            if (config.headers[SecureConsts.SECURE_PARAMS_HEADER()] === SecureConsts.FLAG_ENABLE()) {
                delete config.headers[SecureConsts.SECURE_PARAMS_HEADER()]
                if (config.params) {
                    for (const propName of Object.keys(config.params)) {
                        const value = config.params[propName];
                        if (value !== null && typeof (value) !== "undefined") {
                            if (typeof value === 'object') {
                                for (const key of Object.keys(value)) {
                                    value[key] = SecureTransfer.encrypt(value[key], aesKey);
                                }
                            } else {
                                config.params[propName] = SecureTransfer.encrypt(config.params[propName], aesKey);
                            }
                        }
                    }
                }
            }
        }
        if(SecureConfig.enableDebugLog){
            console.log('request:afterSecureConfig:',ObjectUtils.deepClone(config))
        }
        return config;
    },
    // 响应解密过滤，res需要包含headers和data
    // 分别表示响应头和响应体
    // 当响应头中存在SECURE_DATA_HEADER时，将会自动解密响应体
    responseFilter(res) {
        if(SecureConfig.enableDebugLog){
            console.log('response:beforeSecureRes:',ObjectUtils.deepClone(res))
        }
        let skeyHeader = res.headers[SecureConsts.SECURE_DYNAMIC_KEY_HEADER()];
        if (!StringUtils.isEmpty(skeyHeader)) {
            if(SecureConfig.enableDebugLog){
                console.log('response:updateRsaPubKey:',skeyHeader)
            }
            SecureTransfer.saveRsaPubKey(skeyHeader);
        }

        let headerValue = res.headers[SecureConfig.headerName]
        if (StringUtils.isEmpty(headerValue)) {
            return
        }
        let responseHeader = SecureUtils.parseSecureHeader(SecureConfig.headerName, SecureConfig.headerSeparator, res)

        let aesKey = SecureTransfer.getResponseSecureHeader(responseHeader.randomKey);

        res.data = SecureTransfer.decrypt(res.data, aesKey);
        if(SecureConfig.enableDebugLog){
            console.log('response:afterSecureRes:',ObjectUtils.deepClone(res))
        }
        return res;
    },
}


export default SecureTransferFilter;
