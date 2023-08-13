import Random from './random'

const CodeUtil = {
  makeCheckCode(len, onlyNumber = false) {
    let ret = ''
    let bounce = 10 + 26 + 26
    if (onlyNumber) {
      bounce = 10
    }
    for (let i = 0; i < len; i++) {
      const val = Random.nextLowerInt(10 + 26 + 26)
      if (val < 10) {
        ret += String.fromCharCode(val + '0'.charCodeAt())
      } else if (val < (10 + 26)) {
        ret += String.fromCharCode(val - 10 + 'a'.charCodeAt(0))
      } else {
        ret += String.fromCharCode(val - 10 - 26 + 'A'.charCodeAt(0))
      }
    }
    return ret
  }
}
export default CodeUtil
