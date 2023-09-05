/**
 * 自动加解密的核心过滤器
 */
import SecureConfig from '../secure-config'
import SecureTransfer from './secure-transfer'
import StringUtils from '../util/string-utils'
import SecureUtils from '../util/secure-utils'
import SecureConsts from '../consts/secure-consts'
import SecureHeader from '../data/secure-header'
import ObjectUtils from '../util/ObjectUtils'
import qs from 'qs'
import SecureException from '../excception/secure-exception'
import SecureErrorCode from '../consts/secure-error-code'
import SecureCallback from './secure-callback'

const SecureTransferFilter = {
  getRequestContentType (config) {
    let contentType = null
    const method = config.method
    if (contentType == null) {
      Object.keys(config.headers).forEach((key) => {
        const lkey = key.toLowerCase()
        if (lkey == 'content-type') {
          contentType = config.headers[key]
        }
      })
    }
    if (contentType == null) {
      const methodHeader = config.headers[method]
      Object.keys(methodHeader).forEach((key) => {
        const lkey = key.toLowerCase()
        if (lkey == 'content-type') {
          contentType = methodHeader[key]
        }
      })
    }
    return contentType
  },
  isInWhiteList (url, whiteList) {
    if (whiteList == null || whiteList == undefined) {
      return false
    }
    if (!url.startsWith('/')) {
      url = '/' + url
    }
    for (let i = 0; i < whiteList.length; i++) {
      let wurl = whiteList[i]
      if (wurl.test) {
        if (wurl.test(url)) {
          return true
        }
      } else {
        if (!wurl.startsWith('/')) {
          wurl = '/' + wurl
        }
        if (url == wurl) {
          return true
        }
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
  requestFilter (config) {
    if (SecureConfig.enable == false) {
      return config
    }
    if (config.url.indexOf('://') >= 0) {
      if (config.baseURL && config.baseURL != '') {
        if (config.url.startsWith(config.baseURL)) {
          config.url = config.url.substring(config.baseURL.length)
        }
      }
      let idx = config.url.indexOf('://')
      let purl = config.url
      if (idx >= 0) {
        purl = purl.substring(idx + '://'.length)
        idx = purl.indexOf('/')
        if (idx >= 0) {
          purl = purl.substring(idx)
          config.url = purl
        }
      }
    }
    if (config.url.indexOf('?') >= 0) {
      // regular query string
      const url = config.url
      const arr = url.split('?', 2)
      if (arr.length >= 2 && arr[1] != '') {
        const obj = qs.parse(arr[1])
        config.params = Object.assign({}, obj, config.params)
        config.url = arr[0]
      }
    }
    config.secure = {
      url: config.url
    }
    if (SecureTransfer.existsAsymSlfPubKeySign()) {
      config.headers[SecureConfig.clientAsymSignName] = SecureTransfer.getAsymSlfPubSign()
    }
    if (config.data) {
      if (SecureUtils.typeOf(config.data) == 'formdata') {
        config.headers['Content-Type'] = 'multipart/form-data'
      }
    }
    let contentType = this.getRequestContentType(config)
    if (StringUtils.isEmpty(contentType)) {
      contentType = ''
    }
    contentType = contentType.toLowerCase()
    if (contentType.indexOf('multipart/form-data') >= 0) {
      return config
    }
    if (this.isInWhiteList(config.url, SecureConfig.whileList)) {
      return config
    }
    if (SecureConfig.enableDebugLog) {
      console.log('request:beforeSecureConfig:', (config.secure.url || config.url), ObjectUtils.deepClone(config))
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
    const symmKey = SecureTransfer.symmKeyGen(SecureConfig.symmKeySize / 8)
    const requestHeader = SecureHeader.newObj()
    requestHeader.nonce = new Date().getTime().toString(16) + '-' + Math.floor(Math.random() * 0x0fff).toString(16)
    requestHeader.randomKey = SecureTransfer.getRequestSecureHeader(symmKey)
    requestHeader.serverAsymSign = SecureTransfer.getAsymOthPubSign()
    let signText = ''
    if (secureData) {
      if (config.data) {
        const method = config.method
        if (method == 'put' || method == 'post') {
          if (!StringUtils.isEmpty(contentType)) {
            if (contentType.indexOf('application/x-www-form-urlencoded') >= 0) {
              config.data = qs.stringify(config.data)
            }
          }
        }
        config.data = SecureTransfer.encrypt(config.data, symmKey)
        signText += config.data
      }
    }
    if (secureParams) {
      if (config.params) {
        const paramText = qs.stringify(config.params)
        config.params = {}
        config.params[SecureConfig.parameterName] = SecureTransfer.encrypt(paramText, symmKey)
        signText += config.params[SecureConfig.parameterName]
      }
    }
    requestHeader.clientAsymSign = SecureTransfer.getAsymSlfPubSign()
    requestHeader.sign = SecureUtils.makeSecureSign(signText, requestHeader)
    requestHeader.digital = SecureTransfer.makeDigitalSign(requestHeader.sign)
    if (SecureConfig.enableDebugLog) {
      console.log('request:secureHeader:', (config.secure.url || config.url), ObjectUtils.deepClone(requestHeader))
    }
    config.headers[SecureConfig.headerName] = SecureUtils.encodeSecureHeader(requestHeader, SecureConfig.headerSeparator)

    if (SecureConfig.enableDebugLog) {
      console.log('request:afterSecureConfig:', (config.secure.url || config.url), ObjectUtils.deepClone(config))
    }
    return config
  },
  // 响应解密过滤，res需要包含headers和data
  // 分别表示响应头和响应体
  // 当响应头中存在SECURE_DATA_HEADER时，将会自动解密响应体
  responseFilter (res) {
    if (SecureConfig.enable == false) {
      return res
    }
    if (res == null || res == undefined) {
      return
    }
    const callbackFlags = {
      pubKey: false,
      priKey: false,
      swapKey: false
    }
    if (SecureConfig.enableDebugLog) {
      console.log('response:beforeSecureRes:', (res.config.secure.url || res.config.url), ObjectUtils.deepClone(res))
    }
    const skeyHeader = res.headers[SecureConfig.dynamicKeyHeaderName]
    if (!StringUtils.isEmpty(skeyHeader)) {
      if (SecureConfig.enableDebugLog) {
        console.log('response:updateAsymPubKey:', (res.config.secure.url || res.config.url), skeyHeader)
      }
      SecureTransfer.saveAsymOthPubKey(skeyHeader)
    }
    const wkeyHeader = res.headers[SecureConfig.clientKeyHeaderName]
    if (!StringUtils.isEmpty(wkeyHeader)) {
      if (SecureConfig.enableDebugLog) {
        console.log('response:updateAsymPriKey:', (res.config.secure.url || res.config.url), wkeyHeader)
      }
      if (SecureConfig.enableSwapAsymKey) {
        if (SecureCallback.callSwapKey && !callbackFlags.swapKey) {
          SecureCallback.callSwapKey()
          callbackFlags.swapKey = true
        }
      } else {
        SecureTransfer.saveAsymSlfPriKey(wkeyHeader)
      }
    }

    const headerValue = res.headers[SecureConfig.headerName]
    if (StringUtils.isEmpty(headerValue)) {
      return res
    }
    const responseHeader = SecureUtils.parseSecureHeader(SecureConfig.headerName, SecureConfig.headerSeparator, res)
    responseHeader.clientAsymSign = SecureTransfer.getAsymSlfPubSign()
    if (SecureConfig.enableDebugLog) {
      console.log('response:secureHeader:', (res.config.secure.url || res.config.url), ObjectUtils.deepClone(responseHeader))
    }

    const ok = SecureUtils.verifySecureHeader(res.data, responseHeader)
    if (!ok) {
      if (SecureConfig.enableSwapAsymKey) {
        if (SecureCallback.callSwapKey && !callbackFlags.swapKey) {
          SecureCallback.callSwapKey()
          callbackFlags.swapKey = true
        }
      } else {
        if (SecureCallback.callPubKey && !callbackFlags.pubKey) {
          SecureCallback.callPubKey()
          callbackFlags.pubKey = true
        }
        if (SecureCallback.callPriKey && !callbackFlags.priKey) {
          SecureCallback.callPriKey()
          callbackFlags.priKey = true
        }
      }
      throw SecureException.newObj(SecureErrorCode.BAD_SIGN, '签名验证失败')
    }

    const digitalOk = SecureTransfer.verifyDigitalSign(responseHeader.digital, responseHeader.sign)
    if (!digitalOk) {
      if (SecureConfig.enableSwapAsymKey) {
        if (SecureCallback.callSwapKey && !callbackFlags.swapKey) {
          SecureCallback.callSwapKey()
          callbackFlags.swapKey = true
        }
      } else {
        if (SecureCallback.callPubKey && !callbackFlags.pubKey) {
          SecureCallback.callPubKey()
          callbackFlags.pubKey = true
        }
      }
      throw SecureException.newObj(SecureErrorCode.BAD_DIGITAL(), '数字签名验证失败，请重试！')
    }

    const symmKey = SecureTransfer.getResponseSecureHeader(responseHeader.randomKey)
    if (symmKey == null) {
      if (SecureConfig.enableSwapAsymKey) {
        if (SecureCallback.callSwapKey && !callbackFlags.swapKey) {
          SecureCallback.callSwapKey()
          callbackFlags.swapKey = true
        }
      } else {
        if (SecureCallback.callPriKey && !callbackFlags.priKey) {
          SecureCallback.callPriKey()
          callbackFlags.priKey = true
        }
      }
      throw SecureException.newObj(SecureErrorCode.BAD_RANDOM_KEY(), '随机秘钥无效或已失效，请重试！')
    }

    res.data = SecureTransfer.decrypt(res.data, symmKey)
    if (SecureConfig.enableDebugLog) {
      console.log('response:afterSecureRes:', (res.config.secure.url || res.config.url), ObjectUtils.deepClone(res))
    }
    return res
  }
}

export default SecureTransferFilter
