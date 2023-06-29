import RsaUtil from "../util/RsaUtil";
import AesUtil from "../util/AesUtil";
import Sha256Signature from "../util/sha256-signature";

const SecureProvider = {
    asymmetricEncryptor: RsaUtil,
    symmetricEncryptor: AesUtil,
    messageDigester: Sha256Signature
}
export default SecureProvider;