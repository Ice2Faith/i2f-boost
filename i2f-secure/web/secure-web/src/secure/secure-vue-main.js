/**
 * VUE实例挂载
 */
import Vue from 'vue'

import Random from './util/Random'
import Base64Util from './util/Base64Util'
import Base64Obfuscator from './util/Base64Obfuscator'
import SecureTransfer from './core/SecureTransfer'
import SecureTransferFilter from './core/SecureTransferFilter'
import SecureProvider from './crypto/SecureProvider'

Vue.prototype.$random = Random

Vue.prototype.$secureProvider = SecureProvider

Vue.prototype.$base64 = Base64Util

Vue.prototype.$base64Obfuscator = Base64Obfuscator

Vue.prototype.$secureTransfer = SecureTransfer

Vue.prototype.$secureTransferFilter = SecureTransferFilter
