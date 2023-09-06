import SecureProviderPresets from "../preset/SecureProviderPresets";

const SecureProvider = {
  asymmetricEncryptor: SecureProviderPresets.asymmetricEncryptor_RSA,
  symmetricEncryptor: SecureProviderPresets.symmetricEncryptor_AES,
  messageDigester: SecureProviderPresets.messageDigester_SHA256
}
export default SecureProvider
