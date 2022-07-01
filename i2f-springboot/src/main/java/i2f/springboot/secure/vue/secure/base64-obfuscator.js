import Random from "./random";

const Base64Obfuscator = {
  OBF_PREFIX() {
    return "$.";
  },
  encode(bs4, prefix) {
    let builder = "";
    if (prefix) {
      builder += this.OBF_PREFIX();
    }
    for (let i = 0; i < bs4.length; i++) {
      if (i % 2 == 0 && bs4.charAt(i) != '=') {
        let ird = Random.nextLowerInt(16);
        if (ird < 10) {
          builder += (String.fromCharCode('0'.charCodeAt(0) + ird));
        } else {
          builder += (String.fromCharCode('A'.charCodeAt(0) + (ird - 10)));
        }
      }
      builder += (bs4.charAt(i));
    }
    return builder;
  },
  decode(sob) {
    let str = sob;
    if (sob.startsWith(this.OBF_PREFIX())) {
      str = sob.substring(this.OBF_PREFIX().length);
    }
    let builder = '';
    let i = 0;
    while (i < str.length) {
      if (i % 3 == 0 && str.charAt(i) != '=') {
        i++;
        continue;
      }
      builder += (str.charAt(i));
      i++;
    }
    return builder;
  }
};


export default Base64Obfuscator;
