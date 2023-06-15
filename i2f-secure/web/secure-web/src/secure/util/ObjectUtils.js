/**
 * 对象工具
 */
const ObjectUtils = {
    deepClone(obj) {
        if (typeof obj !== "object" || obj == null) {
            return obj
        }
        let res
        if (obj instanceof Array) {
            res = []
        } else {
            res = {}
        }
        for (let key in obj) {
            if (obj.hasOwnProperty(key)) {
                res[key] = this.deepClone(obj[key])
            }
        }
        return res
    }
}

export default ObjectUtils