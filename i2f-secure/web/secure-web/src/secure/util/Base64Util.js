/**
 * Base64工具
 */
import {Base64} from 'js-base64'

const Base64Util = {
  encrypt: function (data) {
    return Base64.encode(data)
  },
  decrypt: function (data) {
    return Base64.decode(data)
  },
  encryptObj: function (data) {
    const js = JSON.stringify(data)
    return this.encrypt(js)
  },
  decryptObj: function (data) {
    const js = this.decrypt(data)
    return JSON.parse(js)
  }
}

export default Base64Util
