import SecureProvider from './SecureProvider'
import Base64Util from '../util/Base64Util'

const SignatureUtil = {
  sign (text) {
    return SecureProvider.messageDigester.sign(Base64Util.encrypt(text))
  }
}
export default SignatureUtil
