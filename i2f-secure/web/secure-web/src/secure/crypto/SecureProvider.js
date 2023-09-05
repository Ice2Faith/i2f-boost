import AesUtil from '../util/AesUtil'
import Sha256Signature from '../util/sha256-signature'
import Sm2Util from "@/secure/util/Sm2Util";

const SecureProvider = {
  // asymmetricEncryptor: RsaUtil,
  asymmetricEncryptor: Sm2Util,
  symmetricEncryptor: AesUtil,
  messageDigester: Sha256Signature
}
export default SecureProvider
