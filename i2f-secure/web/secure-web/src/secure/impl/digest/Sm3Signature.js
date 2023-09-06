import {sm3} from 'sm-crypto'

const Sm3Signature = {
  sign(text) {
    debugger
    return sm3(text)
  }
}

export default Sm3Signature
