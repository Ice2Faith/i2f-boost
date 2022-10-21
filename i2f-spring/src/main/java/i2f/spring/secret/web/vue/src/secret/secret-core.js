import {Base64} from 'js-base64';
import CryptoJS from 'crypto-js';
import jsrsasign from 'jsrsasign';

export function ICoder() {
    return {
        code() {

        }
    }
}

export function IEncryptor() {
    return {
        encrypt(data) {

        },
        decrypt(data) {

        }
    }
}

export function IAsymmetricalEncryptor() {
    return {
        encrypt(data) {

        },
        decrypt(data) {

        },
        encryptKey(data, key) {

        },
        decryptKey(data, key) {

        },
        encryptPublicKey(data, key) {

        },
        decryptPrivateKey(data, key) {

        }
    }
}

export function ISymmetricalEncryptor() {
    return {
        encrypt(data) {

        },
        decrypt(data) {

        },
        encryptKey(data, key) {

        },
        decryptKey(data, key) {

        }
    }
}

export function IHasher() {
    return {
        hash(data) {

        }
    }
}

export function IKeyPair() {
    return {
        publicKey() {

        },
        privateKey() {

        }
    }
}

export function INoncer() {
    return {
        nonce() {

        },
        pass(nonce) {

        },
        store(nonce) {

        }
    }
}

export function ICacher() {
    return {
        set(key, value) {

        },
        setExpire(key, value, expireSecond) {

        },
        get(key) {

        },
        exists(key) {

        },
        remove(key) {

        }
    }
}

export function SecretException(msg) {
    return {
        type: 'SecretException',
        msg: msg
    }
}

export function SecretKeyPair(pubKey = null, priKey = null) {
    return {
        _pubKey: pubKey,
        _priKey: priKey,
        publicKey() {
            return this._pubKey;
        },
        privateKey() {
            return this._priKey;
        }
    }
}


export function SecretUtil() {
    return {
        randInt() {
            return Math.floor(Math.random() * 65535);
        },
        randIntMax(max) {
            return this.randInt() % max;
        },
        randIntRange(min, max) {
            return this.randInt() % (max - min) + min;
        },
        randDouble() {
            return Math.random();
        },
        randBool() {
            return Math.random() >= 0.5;
        },
        char2int(ch) {
            return ch.charCodeAt(0);
        },
        int2char(it) {
            return String.fromCharCode(it);
        },
        randAlphabet() {
            let num = this.randIntMax(26 + 26 + 10);
            if (num < 10) {
                return this.int2char(num + this.char2int('0'));
            } else if (num < 10 + 26) {
                return this.int2char(num - 10 + this.char2int('a'));
            } else {
                return this.int2char(num - 10 - 26 + this.char2int('A'));
            }
        },
        md5(data) {
            return CryptoJS.MD5(data).toString();
        },
        uuid() {
            let str = Math.floor(new Date().getTime() / 1000) + "-";
            for (let i = 0; i < 5; i++) {
                str = str + this.randInt();
            }
            return this.md5(str);
        },
        toBase64(data) {
            // return btoa(data);
            return Base64.encode(data);
        },
        parseBase64(str) {
            // return atob(str);
            return Base64.decode(str);
        },
        readText(is) {
            return localStorage.getItem(is);
        },
        writeText(os, text) {
            return localStorage.setItem(os, text);
        },
        saveKeyPair(os, key) {
            let pubKey = "";
            if (key.publicKey() != null) {
                pubKey = this.escapeBase64(key.publicKey());
            }
            let priKey = "";
            if (key.privateKey() != null) {
                priKey = this.escapeBase64(key.privateKey());
            }
            let content = pubKey + "\n" + priKey;
            this.writeText(os, content);
        },
        loadKeyPair(is) {
            let lines = this.readText(is);
            let arr = lines.split("\n", 2);
            let pubKey = arr[0].trim();
            let priKey = arr[1].trim();
            let pubKeyBytes = null;
            if (pubKey.length != 0) {
                pubKeyBytes = this.descapeBase64(pubKey);
            }
            let priKeyBytes = null;
            if (priKey.length != 0) {
                priKeyBytes = this.descapeBase64(priKey);
            }
            return SecretKeyPair(pubKeyBytes, priKeyBytes);
        },
        toCharArray(str) {
            let ret = [];
            for (let i = 0; i < str.length; i++) {
                ret.push(str.charAt(i));
            }
            return ret;
        },
        charArrayToString(arr) {
            let ret = '';
            for (let i = 0; i < arr.length; i++) {
                ret += arr[i];
            }
            return ret;
        },
        escapeBase64(bs4) {
            if (bs4 == null) {
                return bs4;
            }

            let arr = this.toCharArray(bs4);
            let elen = 0;
            while (arr[arr.length - 1 - elen] == '=') {
                elen++;
            }
            let logicLen = arr.length - elen;
            for (let i = 0; i < logicLen / 2; i++) {
                let tmp = arr[i];
                arr[i] = arr[logicLen - 1 - i];
                arr[logicLen - 1 - i] = tmp;
                if (this.char2int(arr[i]) >= this.char2int('A') && this.char2int(arr[i]) <= this.char2int('Z')) {
                    arr[i] = this.int2char(this.char2int(arr[i]) - this.char2int('A') + this.char2int('a'));
                } else if (this.char2int(arr[i]) >= this.char2int('a') && this.char2int(arr[i]) <= this.char2int('z')) {
                    arr[i] = this.int2char(this.char2int(arr[i]) - this.char2int('a') + this.char2int('A'));
                }
                if (this.char2int(arr[logicLen - 1 - i]) >= this.char2int('A') && this.char2int(arr[logicLen - 1 - i]) <= this.char2int('Z')) {
                    arr[logicLen - 1 - i] = this.int2char(this.char2int(arr[logicLen - 1 - i]) - this.char2int('A') + this.char2int('a'));
                } else if (this.char2int(arr[logicLen - 1 - i]) >= this.char2int('a') && this.char2int(arr[logicLen - 1 - i]) <= this.char2int('z')) {
                    arr[logicLen - 1 - i] = this.int2char(this.char2int(arr[logicLen - 1 - i]) - this.char2int('a') + this.char2int('A'));
                }
            }
            return this.charArrayToString(arr);
        },
        descapeBase64(bs4) {
            if (bs4 == null) {
                return bs4;
            }

            let arr = this.toCharArray(bs4);
            let elen = 0;
            while (arr[arr.length - 1 - elen] == '=') {
                elen++;
            }
            let logicLen = arr.length - elen;
            for (let i = 0; i < logicLen / 2; i++) {
                let tmp = arr[i];
                arr[i] = arr[logicLen - 1 - i];
                arr[logicLen - 1 - i] = tmp;
                if (this.char2int(arr[i]) >= this.char2int('A') && this.char2int(arr[i]) <= this.char2int('Z')) {
                    arr[i] = this.int2char(this.char2int(arr[i]) - this.char2int('A') + this.char2int('a'));
                } else if (this.char2int(arr[i]) >= this.char2int('a') && this.char2int(arr[i]) <= this.char2int('z')) {
                    arr[i] = this.int2char(this.char2int(arr[i]) - this.char2int('a') + this.char2int('A'));
                }
                if (this.char2int(arr[logicLen - 1 - i]) >= this.char2int('A') && this.char2int(arr[logicLen - 1 - i]) <= this.char2int('Z')) {
                    arr[logicLen - 1 - i] = this.int2char(this.char2int(arr[logicLen - 1 - i]) - this.char2int('A') + this.char2int('a'));
                } else if (this.char2int(arr[logicLen - 1 - i]) >= this.char2int('a') && this.char2int(arr[logicLen - 1 - i]) <= this.char2int('z')) {
                    arr[logicLen - 1 - i] = this.int2char(this.char2int(arr[logicLen - 1 - i]) - this.char2int('a') + this.char2int('A'));
                }
            }
            return this.charArrayToString(arr);
        }

    }
}

export function SecretMsg() {
    return {
        _util: SecretUtil(),
        randomKey: null,
        nonce: null,
        signature: null,
        msg: null,
        publicKey: null,
        convert() {
            let ret = Base64SecretMsg();
            if (this.randomKey != null) {
                ret.randomKey = this._util.escapeBase64(this.randomKey);
            }
            if (this.nonce != null) {
                ret.nonce = this._util.escapeBase64(this._util.toBase64(this.nonce));
            }
            if (this.signature != null) {
                ret.signature = this._util.escapeBase64(this.signature);
            }
            if (this.msg != null) {
                ret.msg = this.msg;
            }
            if (this.publicKey != null) {
                ret.publicKey = this._util.escapeBase64(this.publicKey);
            }
            return ret;
        }
    }
}

export function Base64SecretMsg() {
    return {
        _util: SecretUtil(),
        randomKey: null,
        nonce: null,
        signature: null,
        msg: null,
        publicKey: null,
        convert() {
            let ret = SecretMsg();
            if (this.randomKey != null) {
                ret.randomKey = this._util.descapeBase64(this.randomKey);
            }
            if (this.nonce != null) {
                ret.nonce = this._util.parseBase64(this._util.descapeBase64(this.nonce));
            }
            if (this.signature != null) {
                ret.signature = this._util.descapeBase64(this.signature);
            }
            if (this.msg != null) {
                ret.msg = this.msg;
            }
            if (this.publicKey != null) {
                ret.publicKey = this._util.descapeBase64(this.publicKey);
            }

            return ret;
        }
    }
}

export function SecretProvider() {
    return {
        mineKey: null,
        coder: null,
        noncer: null,
        hasher: null,
        symmetricalEncryptor: null,
        asymmetricalEncryptor: null,
        send(data, otherKeyPair) {
            let ret = SecretMsg();
            let key = this.coder.code();
            ret.publicKey = this.mineKey.publicKey();
            ret.randomKey = this.asymmetricalEncryptor.encryptPublicKey(key, otherKeyPair);
            ret.nonce = this.noncer.nonce();
            ret.msg = this.symmetricalEncryptor.encryptKey(data, key);

            let sign = this.hash(ret);
            ret.signature = this.asymmetricalEncryptor.encryptKey(sign, this.mineKey);

            return ret;
        },
        recv(msg) {
            let sendKeyPair = SecretKeyPair(msg.publicKey);
            let sendSign = null;
            try {
                sendSign = this.asymmetricalEncryptor.decryptKey(msg.signature, sendKeyPair);
            } catch (e) {
                throw SecretException("数字签名验证失败");
            }
            if (sendSign == null || sendSign.length == 0) {
                throw SecretException("数字签名验证失败");
            }
            let recvSign = this.hash(msg);
            if (sendSign != recvSign) {
                throw SecretException("消息签名验证失败");
            }
            if (!this.noncer.pass(msg.nonce)) {
                throw new SecretException("重放消息");
            }
            this.noncer.store(msg.nonce);
            let key = null;
            try {
                key = this.asymmetricalEncryptor.decryptPrivateKey(msg.randomKey, this.mineKey);
            } catch (e) {
                throw SecretException("密文解析失败");
            }
            if (key == null) {
                throw SecretException("密文解析失败");
            }
            let data = null;
            try {
                data = this.symmetricalEncryptor.decryptKey(msg.msg, key);
            } catch (e) {
                throw SecretException("密文解析失败");
            }
            if (data == null) {
                throw SecretException("密文解析失败");
            }
            return data;
        },
        hash(msg) {
            let sdata = (msg.msg + msg.nonce + msg.randomKey);
            let sign = this.hasher.hash(sdata);
            return sign;
        }
    }
}

//////////////////////////////////////////////////////////////////////////////////////

export function AesSymmEncryptor(key = null) {
    return {
        _key: key,
        encrypt(data) {
            return this.encryptKey(data, this._key);
        },
        decrypt(data) {
            return this.decryptKey(data, this._key);
        },
        encryptKey(data, key) {
            let keys = CryptoJS.enc.Utf8.parse(key);//Latin1 w8m31+Yy/Nw6thPsMpO5fg==
            let srcs = CryptoJS.enc.Utf8.parse(data);
            let encrypted = CryptoJS.AES.encrypt(srcs, keys, {
                mode: CryptoJS.mode.ECB,
                padding: CryptoJS.pad.Iso10126
            });
            return encrypted.toString();
        },
        decryptKey(data, key) {
            let keys = CryptoJS.enc.Utf8.parse(key);//Latin1 w8m31+Yy/Nw6thPsMpO5fg==
            let decrypt = CryptoJS.AES.decrypt(data, keys, {
                mode: CryptoJS.mode.ECB,
                padding: CryptoJS.pad.Iso10126
            });
            return CryptoJS.enc.Utf8.stringify(decrypt).toString();
        }
    }
}

export function RsaProvider(usePkcs1 = false) {
    return {
        _usePkcs1: usePkcs1,
        makeRsaKey(size) {
            if (!this._usePkcs1) {
                throw SecretException('不支持非Pkcs1类型');
            }
            let rsaKeyPair = jsrsasign.KEYUTIL.generateKeypair('RSA', size);
            let pubKey = jsrsasign.KEYUTIL.getPEM(rsaKeyPair.pubKeyObj);
            let privKey = jsrsasign.KEYUTIL.getPEM(rsaKeyPair.prvKeyObj, 'PKCS1PRV');

            pubKey = this.parsePemKey(pubKey);
            privKey = this.parsePemKey(privKey);
            return SecretKeyPair(pubKey, privKey);
        },
        parsePemKey(pkey) {
            let ret = '';
            let arr = pkey.trim().split("\n");
            for (let i = 0; i < arr.length; i++) {
                if (i == 0 || i == arr.length - 1) {
                    continue;
                }
                ret += arr[i].trim();
            }
            return ret;
        },
        encryptKey(data, key) {
            if (!this._usePkcs1) {
                throw SecretException('不支持非Pkcs1类型');
            }
            let chiper = new JSEncrypt();
            chiper.setKey(key.privateKey());
            return chiper.encrypt(data, true);
        },
        decryptKey(data, key) {
            if (!this._usePkcs1) {
                throw SecretException('不支持非Pkcs1类型');
            }
            let chiper = new JSEncrypt();
            chiper.setKey(key.publicKey());
            return chiper.decrypt(data, false);
        },
        encryptPublicKey(data, key) {
            if (!this._usePkcs1) {
                throw SecretException('不支持非Pkcs1类型');
            }
            let chiper = new JSEncrypt();
            chiper.setKey(key.publicKey());
            return chiper.encrypt(data, false);
        },
        decryptPrivateKey(data, key) {
            if (!this._usePkcs1) {
                throw SecretException('不支持非Pkcs1类型');
            }
            let chiper = new JSEncrypt();
            chiper.setKey(key.privateKey());
            return chiper.decrypt(data, true);
        }
    }
}

export function RsaAsymEncryptor(key = null, rsaProvider = RsaProvider()) {
    return {
        _key: key,
        _rsaProvider: rsaProvider,
        encrypt(data) {
            return this.encryptKey(data, this._key);
        },
        decrypt(data) {
            return this.decryptKey(data, this._key);
        },
        encryptKey(data, key) {
            return this._rsaProvider.encryptKey(data, key);
        },
        decryptKey(data, key) {
            return this._rsaProvider.decryptKey(data, key);
        },
        encryptPublicKey(data, key) {
            return this._rsaProvider.encryptPublicKey(data, key);
        },
        decryptPrivateKey(data, key) {
            return this._rsaProvider.decryptPrivateKey(data, key);
        }
    }
}

export function MemCacher() {
    return {
        _map: {},
        set(key, value) {
            this._map[key] = value;
        },
        setExpire(key, value, expireSecond) {
            this._map[key] = value;
            let _this = this;
            setTimeout(function () {
                delete _this._map[key];
            }, expireSecond);
        },
        get(key) {
            return this._map[key];
        },
        exists(key) {
            return !this._map[key];
        },
        remove(key) {
            delete this._map[key];
        }
    }
}

export function CacheNoncer(cache = MemCacher()) {
    return {
        NONCE_HOLDER() {
            return ' ';
        },
        nonceTimeoutSecond() {
            return 30 * 60;
        },
        _cache: cache,
        nonce() {
            return SecretUtil().uuid();
        },
        pass(nonce) {
            return this._cache.exists(nonce);
        },
        store(nonce) {
            this._cache.setExpire(nonce, this.NONCE_HOLDER(), this.nonceTimeoutSecond());
        }
    }
}

export function CharsCoder(len = 16) {
    return {
        _util: SecretUtil(),
        _len: len,
        code() {
            let ret = "";
            for (let i = 0; i < this._len; i++) {
                ret += this._util.randAlphabet();
            }
            return ret;
        }
    }
}

export function Md5Hasher() {
    return {
        _util: SecretUtil(),
        hash(data) {
            return this._util.md5(data);
        }
    }
}

export function RamSecretProvider() {
    return {
        _util: SecretUtil(),
        getInstance() {
            return this.getCustomInstance(false, 1024, 16);
        },
        getPkcs1Instance(usePkcs1) {
            return this.getCustomInstance(usePkcs1, 1024, 16);
        },
        getRsaKey(rsa, rsaKeyLen) {
            let cacheKey = "pkcs1:" + rsa._usePkcs1 + ",len:" + rsaKeyLen;
            let keyPair = null;
            try {
                keyPair = this._util.loadKeyPair(cacheKey);
            } catch (e) {

            }
            if (!keyPair || !keyPair.privateKey() || !keyPair.publicKey()) {
                keyPair = rsa.makeRsaKey(rsaKeyLen);
            }
            this._util.saveKeyPair(cacheKey, keyPair);
            return keyPair;
        },
        getCustomInstance(usePkcs1, rsaKeyLen, aesKeyLen) {
            let ret = SecretProvider();
            let rsa = RsaProvider(usePkcs1);
            ret.mineKey = this.getRsaKey(rsa, rsaKeyLen);
            ret.coder = CharsCoder(aesKeyLen);
            ret.noncer = CacheNoncer();
            ret.hasher = Md5Hasher();
            ret.symmetricalEncryptor = AesSymmEncryptor();
            ret.asymmetricalEncryptor = RsaAsymEncryptor(null, rsa);
            return ret;
        }
    }
}

//////////////////////////////////////////////////////////////////////////////////////

export function SecretWebCore(secretProvider) {
    return {
        _util: SecretUtil(),
        _secretProvider: secretProvider,
        HEADER_RANDOM_KEY() {
            return "rdk";
        },
        HEADER_SIGN_KEY() {
            return "sign";
        },
        HEADER_NONCE_KEY() {
            return "nonce";
        },
        HEADER_PUB_KEY() {
            return "spk";
        },
        LOCAL_OTHER_PUB_KEY() {
            return "ospk";
        },
        secret() {
            return this._secretProvider;
        },
        storeOtherPublicKey(pubKey) {
            localStorage.setItem(this.LOCAL_OTHER_PUB_KEY(), pubKey);
        },
        loadOtherPublicKey() {
            return localStorage.getItem(this.LOCAL_OTHER_PUB_KEY());
        },
        parseMsg(res) {
            let ret = Base64SecretMsg();
            ret.signature = res.headers[this.HEADER_SIGN_KEY()];
            ret.randomKey = res.headers[this.HEADER_RANDOM_KEY()];
            ret.nonce = res.headers[this.HEADER_NONCE_KEY()];
            ret.publicKey = res.headers[this.HEADER_PUB_KEY()];
            ret.msg = res.data;
            try {
                let msg = ret.convert();
                let data = this.secret().recv(msg);
                this.storeOtherPublicKey(ret.publicKey);
                msg.msg = data;
                return JSON.parse(msg.msg);
            } catch (e) {
                if (e.type && e.type == 'SecretException') {
                    throw e;
                } else {
                    throw SecretException('解析消息失败');
                }
            }
        },
        writeMsg(config) {
            let key = SecretKeyPair(this._util.descapeBase64(this.loadOtherPublicKey()));
            let msg = this._secretProvider.send(JSON.stringify(config.data), key);

            let data = msg.convert();

            config.headers[this.HEADER_SIGN_KEY()] = data.signature;
            config.headers[this.HEADER_RANDOM_KEY()] = data.randomKey;
            config.headers[this.HEADER_NONCE_KEY()] = data.nonce;
            config.headers[this.HEADER_PUB_KEY()] = data.publicKey;
            config.headers['Access-Control-Expose-Headers'] = 'sign,rdk,spk,nonce';
            config.headers['Access-Control-Allow-Headers'] = 'sign,rdk,spk,nonce';

            config.data = data.msg;
        }
    }
}

export function SecretFilter(secretWebCore) {
    return {
        _secretWebCore: secretWebCore,
        requestFilter(config) {
            this._secretWebCore.writeMsg(config);
        },
        responseFilter(res) {
            let msg = this._secretWebCore.parseMsg(res);
            res.data = msg;
        }
    }
}

export function SecretWebConfig() {
    return {
        _util: SecretUtil(),
        RSA_KEY_PATH() {
            return "mspk";
        },
        restoreRsaKey(provider) {
            try {
                provider.mineKey = this._util.loadKeyPair(this.RSA_KEY_PATH());
                console.log('RSA秘钥已加载');
            } catch (e) {

            }
            this._util.saveKeyPair(this.RSA_KEY_PATH(), provider.mineKey);
        },
        secretProvider() {
            let ret = RamSecretProvider().getPkcs1Instance(true);
            this.restoreRsaKey(ret);
            return ret;
        },
        secretWebCore() {
            let ret = SecretWebCore(this.secretProvider());
            return ret;
        },
        secretFilter() {
            let ret = SecretFilter(this.secretWebCore());
            return ret;
        }
    }
}
