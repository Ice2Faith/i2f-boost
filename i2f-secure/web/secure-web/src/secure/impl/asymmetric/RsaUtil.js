/**
 * RSA工具
 */
import JSEncrypt from '../../static/jsencrypt'
import jsrsasign from 'jsrsasign'
import AsymKeyPair from '../../data/AsymKeyPair'

const RsaUtil = {
  genKeyPair (size = 1024) {
    if (!size || size <= 0) {
      size = 1024
    }
    const rsaKeypair = jsrsasign.KEYUTIL.generateKeypair('RSA', size)
    const publicKey = this.keyInflater(jsrsasign.KEYUTIL.getPEM(rsaKeypair.prvKeyObj)) // 获取公钥
    const privateKey = this.keyInflater(jsrsasign.KEYUTIL.getPEM(rsaKeypair.prvKeyObj, 'PKCS1PRV')) // 获取私钥
    return AsymKeyPair.newObj(publicKey, privateKey)
  },
  keyInflater (str) {
    const arr = str.split('\n')
    let ret = ''
    for (let i = 1; i < arr.length - 2; i++) {
      ret += arr[i].trim()
    }
    return ret
  },
  publicKeyEncrypt (pubKey, text) {
    const chiper = new JSEncrypt()
    chiper.setKey(pubKey)
    return chiper.encrypt(text, false)
  },
  publicKeyDecrypt (pubKey, text) {
    const chiper = new JSEncrypt()
    chiper.setKey(pubKey)
    return chiper.decrypt(text, false)
  },
  privateKeyEncrypt(priKey, text) {
    const chiper = new JSEncrypt()
    chiper.setKey(priKey)
    return chiper.encrypt(text, true)
  },
  privateKeyDecrypt(priKey, text) {
    const chiper = new JSEncrypt()
    chiper.setKey(priKey)
    return chiper.decrypt(text, true)
  },
  makeSign(priKey, text) {
    return this.privateKeyEncrypt(priKey, text);
  },
  verifySign(pubKey, sign, text) {
    let realData = this.publicKeyDecrypt(pubKey, sign)
    return realData == text
  }
}

export default RsaUtil
