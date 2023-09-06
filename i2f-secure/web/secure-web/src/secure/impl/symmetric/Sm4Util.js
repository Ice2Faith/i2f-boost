/**
 * AES工具
 */
import {sm4} from 'sm-crypto'
import CodeUtil from '../../util/CodeUtil'
import Base64Util from "../../util/Base64Util";

const Sm4Util = {
  genKey(size = 32) {
    return CodeUtil.makeCheckCode(size)
  },
  hexKey(key) {
    key = key + ''
    let ret = ''
    for (let i = 0; i < key.length; i++) {
      let hex = key.charCodeAt(i).toString(16)
      if (hex.length == 1) {
        hex = '0' + hex
      }
      ret += hex
    }
    return ret
  },
  encrypt: function (data, key) {
    key = this.hexKey(key)
    let ret = sm4.encrypt(data, key)
    return Base64Util.encrypt(ret)
  },
  decrypt: function (data, key) {
    key = this.hexKey(key)
    data = Base64Util.decrypt(data)
    return sm4.decrypt(data, key)
  }
}
export default Sm4Util
