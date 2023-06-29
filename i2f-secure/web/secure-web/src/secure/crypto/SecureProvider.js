import Rsa from "../util/rsa";
import AES from "../util/aes";
import StringSignature from "../util/string-signature";

const SecureProvider = {
    asymmetricEncryptor: Rsa,
    symmetricEncryptor: AES,
    messageDigester: StringSignature
}
export default SecureProvider;