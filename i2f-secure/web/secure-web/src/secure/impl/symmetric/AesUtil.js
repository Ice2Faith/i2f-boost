/**
 * AES工具
 */
import CryptoJS from 'crypto-js'
import CodeUtil from '../../util/CodeUtil'

const AesUtil = {
  genKey (size = 32) {
    return CodeUtil.makeCheckCode(size)
  },
  encrypt: function (data, key) {
    const keys = CryptoJS.enc.Utf8.parse(key)// Latin1 w8m31+Yy/Nw6thPsMpO5fg==
    const srcs = CryptoJS.enc.Utf8.parse(data)
    const encrypted = CryptoJS.AES.encrypt(srcs, keys, { mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.Iso10126 })
    return encrypted.toString()
  },
  decrypt: function (data, key) {
    const keys = CryptoJS.enc.Utf8.parse(key)// Latin1 w8m31+Yy/Nw6thPsMpO5fg==
    const decrypt = CryptoJS.AES.decrypt(data, keys, { mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.Iso10126 })
    return CryptoJS.enc.Utf8.stringify(decrypt).toString()
  }
}
export default AesUtil
