import B64 from '../util/base64'
import SecureProvider from './SecureProvider'

const SymmetricUtil = {
  genKey(size) {
    return SecureProvider.symmetricEncryptor.genKey(size)
  },
  encrypt: function (data, key) {
    return SecureProvider.symmetricEncryptor.encrypt(data, key)
  },
  decrypt: function (data, key) {
    return SecureProvider.symmetricEncryptor.decrypt(data, key)
  },
  encryptObj: function (data, key) {
    const srcs = B64.encryptObj(data)
    return this.encrypt(srcs, key)
  },
  decryptObj: function (data, key) {
    const srcs = this.decrypt(data, key)
    return B64.decryptObj(srcs)
  }
}

export default SymmetricUtil
