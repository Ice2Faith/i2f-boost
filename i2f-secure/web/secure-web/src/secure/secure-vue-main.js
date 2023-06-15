/**
 * VUE实例挂载
 */
import Vue from 'vue';

import Rsa from './util/rsa'
import Random from "./util/random";
import AES from "./util/aes";
import B64 from "./util/base64";
import Base64Obfuscator from "./util/base64-obfuscator";
import SecureTransfer from "./core/secure-transfer";
import SecureTransferFilter from "./core/secure-transfer-filter";

Vue.prototype.$random=Random;

Vue.prototype.$rsa=Rsa;

Vue.prototype.$aes=AES;

Vue.prototype.$base64=B64;

Vue.prototype.$base64Obfuscator=Base64Obfuscator;

Vue.prototype.$secureTransfer=SecureTransfer;

Vue.prototype.$secureTransferFilter=SecureTransferFilter;

