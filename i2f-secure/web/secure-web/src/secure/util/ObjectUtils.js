/**
 * 对象工具
 */
const ObjectUtils = {
  deepClone (obj) {
    try {
      return JSON.parse(JSON.stringify(obj))
    } catch (e) {

    }
    return obj
  }
}

export default ObjectUtils
