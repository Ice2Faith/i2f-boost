import AesUtil from '../impl/symmetric/AesUtil'
import Sha256Signature from '../impl/digest/Sha256Signature'
import Sm2Util from "../impl/asymmetric/Sm2Util";
import RsaUtil from "../impl/asymmetric/RsaUtil";
import Sm4Util from "../impl/symmetric/Sm4Util";
import Sm3Signature from "../impl/digest/Sm3Signature";

const SecureProviderPresets = {
  asymmetricEncryptor_RSA: RsaUtil,
  asymmetricEncryptor_SM2: Sm2Util,

  symmetricEncryptor_AES: AesUtil,
  symmetricEncryptor_SM4: Sm4Util,

  messageDigester_SHA256: Sha256Signature,
  messageDigester_SM3: Sm3Signature
}
export default SecureProviderPresets
