import Vue from 'vue';

import Rsa from './rsa'
import Random from "./random";
import AES from "./aes";
import B64 from "./base64";
import Base64Obfuscator from "./base64-obfuscator";
import SecureTransfer from "./secure-transfer";
import SecureTransferFilter from "./secure-transfer-filter";

Vue.prototype.$random=Random;

Vue.prototype.$rsa=Rsa;

Vue.prototype.$aes=AES;

Vue.prototype.$base64=B64;

Vue.prototype.$base64Obfuscator=Base64Obfuscator;

Vue.prototype.$secureTransfer=SecureTransfer;

Vue.prototype.$secureTransferFilter=SecureTransferFilter;

