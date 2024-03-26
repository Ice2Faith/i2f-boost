function WebsocketClient(config = {
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
            if (this.debug) {
              console.debug('websocket:recv', e.data)
            }
            this.onMessage(e.data)
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

export default WebsocketClient
