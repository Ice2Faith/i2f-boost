/**
 * 字符串签名工具
 */
const StringSignature = {
    sign(text) {
        let num1 = 0x51
        let num2 = 0x73
        let num3 = 0x117
        let num4 = 0x19
        let i = 0
        const len = text.length
        while (i < len) {
            const ch = text.charAt(i).charCodeAt(0)
            const mod = i % 4
            if (mod == 0) {
                num1 = (num1 * 7 + ch + Math.floor(i / 11)) % 0x07fff
            } else if (mod == 1) {
                num2 = (num2 * 13 + ch + Math.floor(i / 5)) % 0x07fff
            } else if (mod == 2) {
                num3 = (num3 * 3 + ch + Math.floor(i / 17)) % 0x07fff
            } else if (mod == 3) {
                num4 = (num4 * 31 + ch + Math.floor(i / 19)) % 0x07fff
            }
            i++
        }
        return num1.toString(16) +
            '' + num2.toString(16) +
            '' + len.toString(16) +
            '' + num3.toString(16) +
            '' + num4.toString(16)
    }
}

export default StringSignature
