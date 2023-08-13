/**
 * Base64混淆工具
 */
import Random from './random'

const Base64Obfuscator = {
  OBF_PREFIX () {
    return '$.'
  },
  encode (bs4, prefix) {
    let builder = ''
    if (prefix) {
      builder += this.OBF_PREFIX()
    }
    let jpc = (bs4.length + Random.nextLowerInt(31)) % 10
    builder += (String.fromCharCode('0'.charCodeAt(0) + jpc))
    for (let p = 0; p < jpc; p++) {
      const ird = Random.nextLowerInt(16)
      if (ird < 10) {
        builder += (String.fromCharCode('0'.charCodeAt(0) + ird))
      } else {
        builder += (String.fromCharCode('A'.charCodeAt(0) + (ird - 10)))
      }
    }
    jpc = jpc % 2 + 2
    for (let i = 0; i < bs4.length; i++) {
      if (i % jpc == 0 && bs4.charAt(i) != '=') {
        const ird = Random.nextLowerInt(16)
        if (ird < 10) {
          builder += (String.fromCharCode('0'.charCodeAt(0) + ird))
        } else {
          builder += (String.fromCharCode('A'.charCodeAt(0) + (ird - 10)))
        }
      }
      builder += (bs4.charAt(i))
    }
    return builder
  },
  decode (sob) {
    let str = sob
    if (sob.startsWith(this.OBF_PREFIX())) {
      str = sob.substring(this.OBF_PREFIX().length)
    }
    let builder = ''
    let jpc = str.charAt(0).charCodeAt(0) - '0'.charCodeAt(0)
    str = str.substring(1 + jpc)
    jpc = jpc % 2 + 3
    let i = 0
    while (i < str.length) {
      if (i % jpc == 0 && str.charAt(i) != '=') {
        i++
        continue
      }
      builder += (str.charAt(i))
      i++
    }
    return builder
  }
}

export default Base64Obfuscator
