let publicKey = null // 公钥
let privateKey = null // 私钥
if (true) {
    if (false) {
        console.log('sm')
        const sm2 = require('sm-crypto').sm2

        let keyPair = sm2.generateKeyPairHex()

        publicKey = keyPair.publicKey // 公钥
        privateKey = keyPair.privateKey // 私钥
    } else {
        console.log('gm')
        const {SM2} = require('gm-crypto')
        const keyPair = SM2.generateKeyPair();
        publicKey = keyPair.publicKey // 公钥
        privateKey = keyPair.privateKey // 私钥
    }

}
console.log('publicKey:', publicKey)
console.log('privateKey:', privateKey)
if (true) {
    const SM = require('gm-crypto')
    const {SM2, SM3, SM4} = require('gm-crypto');
    debugger

// 生成SM2密钥对
    function generateKeyPair() {
        const keyPair = SM2.generateKeyPair();
        return keyPair
    }

// 使用公钥加密数据
    function encryptData(data, publicKey) {
        const cipherText = SM2.encrypt(data, publicKey, {
            inputEncoding: 'utf-8',
            outputEncoding: 'base64'
        });
        return cipherText;
    }

// 使用私钥解密数据
    function decryptData(cipherText, privateKey) {
        const decryptedData = SM2.decrypt(cipherText, privateKey, {
            inputEncoding: 'base64',
            outputEncoding: 'utf-8'
        });
        return decryptedData;
    }

// 示例
//       const { privateKey, publicKey } = generateKeyPair();
    const data = '这是一段需要加密的数据';
    const cipherText = encryptData(data, publicKey);
    const decryptedData = decryptData(cipherText, privateKey);

    console.log('原始数据：', data);
    console.log('加密后的数据：', cipherText);
    console.log('解密后的数据：', decryptedData);

}

if (true) {
    const sm2 = require('sm-crypto').sm2

    // let keypair = sm2.generateKeyPairHex()
    //
    // let publicKey = keypair.publicKey // 公钥
    // let privateKey = keypair.privateKey // 私钥

    const cipherMode = 1 // 1 - C1C3C2，0 - C1C2C3，默认为1

    let msgString = '这是你的加密数据内容'
    let encryptData = sm2.doEncrypt(msgString, publicKey, cipherMode) // 加密结果
    let decryptData = sm2.doDecrypt(encryptData, privateKey, cipherMode) // 解密结果

    let digitalSign = '数字签名'
    let digital = sm2.doSignature(digitalSign, privateKey)
    let ok = sm2.doVerifySignature(digitalSign, digital, publicKey)
    console.log('digital:', ok)

    console.log('原始数据：', msgString);
    console.log('加密后的数据：', encryptData);
    console.log('解密后的数据：', decryptData);
}
if (true) {
    const sm3 = require('sm-crypto').sm3
    let hs = sm3('这是你hash的内容')
    console.log('哈希值：' + hs)
}
if (true) {
    const sm4 = require('sm-crypto').sm4

    const msg = '被加密的内容'
    // hex mode
    const key = '000102030405060708090a0b0c0d0e0f' // 128bit/16byte
    const enc = sm4.encrypt(msg, key)
    console.log('enc:', enc)
    const dec = sm4.decrypt(enc, key)
    console.log('dec', dec)

}
