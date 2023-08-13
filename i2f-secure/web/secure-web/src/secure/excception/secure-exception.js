/**
 * 定义异常
 */
const SecureException = {
  newObj (code, msg) {
    return new Error(JSON.stringify({
      code: code,
      msg: msg
    }, null, '    '))
  }
}

export default SecureException
