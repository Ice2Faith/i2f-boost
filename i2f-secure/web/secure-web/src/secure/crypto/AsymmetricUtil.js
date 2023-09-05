import SecureProvider from './SecureProvider'

const AsymmetricUtil = {
  genKeyPair (size) {
    return SecureProvider.asymmetricEncryptor.genKeyPair(size)
  },
  publicKeyEncrypt (pubKey, text) {
    return SecureProvider.asymmetricEncryptor.publicKeyEncrypt(pubKey, text)
  },
  publicKeyDecrypt(pubKey, text) {
    return SecureProvider.asymmetricEncryptor.publicKeyDecrypt(pubKey, text)
  },
  privateKeyEncrypt(priKey, text) {
    return SecureProvider.asymmetricEncryptor.privateKeyEncrypt(priKey, text)
  },
  privateKeyDecrypt(priKey, text) {
    return SecureProvider.asymmetricEncryptor.privateKeyDecrypt(priKey, text)
  },
  makeSign(priKey, text) {
    return SecureProvider.asymmetricEncryptor.makeSign(priKey, text)
  },
  verifySign(pubKey, sign, text) {
    return SecureProvider.asymmetricEncryptor.verifySign(pubKey, sign, text)
  }
}

export default AsymmetricUtil
