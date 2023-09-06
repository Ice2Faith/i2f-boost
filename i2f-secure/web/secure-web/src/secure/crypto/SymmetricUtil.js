import Base64Util from '../util/Base64Util'
import SecureProvider from './SecureProvider'

const SymmetricUtil = {
  genKey (size) {
    return SecureProvider.symmetricEncryptor.genKey(size)
  },
  encrypt: function (data, key) {
    return SecureProvider.symmetricEncryptor.encrypt(data, key)
  },
  decrypt: function (data, key) {
    return SecureProvider.symmetricEncryptor.decrypt(data, key)
  },
  encryptObj: function (data, key) {
    const srcs = Base64Util.encryptObj(data)
    return this.encrypt(srcs, key)
  },
  decryptObj: function (data, key) {
    const srcs = this.decrypt(data, key)
    return Base64Util.decryptObj(srcs)
  }
}

export default SymmetricUtil
