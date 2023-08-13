import CryptoJS from 'crypto-js'

const Sha256Signature = {
  sign(text) {
    return CryptoJS.SHA256(text).toString(CryptoJS.enc.Hex).toUpperCase()
  }
}

export default Sha256Signature
