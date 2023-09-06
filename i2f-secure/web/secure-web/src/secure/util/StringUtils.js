/**
 * 字符串工具
 * @type {{isEmpty(*=): *}}
 */
const StringUtils = {
  isEmpty (str) {
    return str == null || str == undefined || str == ''
  }
}

export default StringUtils
