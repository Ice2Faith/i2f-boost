export function JsonWebsocketMessage() {
  return {
    instance() {
      return {
        action: null,
        msgId: null,
        code: null,
        msg: null,
        data: null
      }
    },
    request(action, data) {
      return {
        action: action,
        msgId: new Date().getTime() + '' + Math.floor(Math.random() * 10000),
        code: 200,
        msg: null,
        data: data
      }
    }
  }
}

function JsonWebsocketClient(config = {
  url: 'ws://localhost:8080/handler?token=xxx',
  protocols: undefined,
  debug: false,
  onMessage: e => console.log(e.data),
  onOpen: e => console.log('opened.'),
  onError: e => console.log('error!'),
  onClose: e => console.log('closed.')
}) {
  const defaultConfig = {
    url: null,
    protocols: undefined,
    debug: false,
    onMessage: e => console.log(e.data),
    onOpen: e => console.log('opened.'),
    onError: e => console.log('error!'),
    onClose: e => console.log('closed.')
  }

  const ret = Object.assign(defaultConfig, config, {
    websocket: null,
    promiseMap: {},
    connectPromise: {},
    sendText(text) {
      if (this.debug) {
        console.debug('websocket:send:text', text)
      }
      this.websocket.send(text)
    },
    sendObj(obj) {
      if (this.debug) {
        console.debug('websocket:send:obj', obj)
      }
      this.sendText(JSON.stringify(obj))
    },
    sendMessage(action, data) {
      const msg = JsonWebsocketMessage().request(action, data)
      if (this.debug) {
        console.debug('websocket:send:message', msg)
      }
      const json = this.encodeMessage(msg)
      this.sendText(json)
    },
    requestMessage(action, data) {
      const msg = JsonWebsocketMessage().request(action, data)
      if (this.debug) {
        console.debug('websocket:request:message', msg)
      }
      const msgId = msg.msgId
      return new Promise((resolve, reject) => {
        const json = this.encodeMessage(msg)
        this.sendText(json)
        setTimeout(() => {
          delete this.promiseMap[msgId]
        }, 30 * 1000)
        this.promiseMap[msgId] = {
          resolve: resolve,
          reject: reject
        }
      })
    },
    encodeMessage(msg) {
      msg.data = JSON.stringify(msg.data)
      return JSON.stringify(msg)
    },
    parseMessage(json) {
      return JSON.parse(json)
    },
    unpackMessage(msg) {
      if (msg.data) {
        msg.data = JSON.parse(msg.data)
      }
      return msg
    },
    decodeMessage(json) {
      const msg = this.parseMessage(json)
      return this.unpackMessage(msg)
    },
    connect() {
      return new Promise((resolve, reject) => {
        this.connectPromise = {
          resolve: resolve,
          reject: reject
        }

        if (this.websocket) {
          this.websocket.close()
        }
        this.websocket = new WebSocket(this.url, this.protocols)
        this.websocket.onopen = (e) => {
          this.connectPromise.resolve(e)
          this.onOpen(e)
        }
        this.websocket.onerror = (e) => {
          this.connectPromise.reject(e)
          this.onError(e)
        }
        this.websocket.onmessage = (e) => {
          if (e.data) {
            const msg = this.decodeMessage(e.data)
            if (this.debug) {
              console.debug('websocket:recv:msg', msg)
            }
            let isPromiseFlag = false
            if (msg.msgId) {
              if (this.promiseMap[msg.msgId]) {
                if (this.debug) {
                  console.debug('websocket:response:message', msg)
                }
                const promise = this.promiseMap[msg.msgId]
                delete this.promiseMap[msg.msgId]
                isPromiseFlag = true
                if (msg.code == 200) {
                  promise.resolve(msg)
                } else {
                  promise.reject(msg)
                }
              }
            }
            if (!isPromiseFlag) {
              this.onMessage(msg)
            }
          }
        }
        this.websocket.onclose = this.onClose
      })
    },
    close() {
      this.websocket.close()
      this.websocket = null
    },
    state() {
      return this.websocket.readyState
    },
    isConnecting() {
      return this.websocket.readyState === WebSocket.CONNECTING
    },
    isConnected() {
      return this.websocket.readyState === WebSocket.OPEN
    },
    isClosing() {
      return this.websocket.readyState === WebSocket.CLOSING
    },
    isClosed() {
      return this.websocket.readyState === WebSocket.CLOSED
    }
  })
  return ret
}

export default JsonWebsocketClient
