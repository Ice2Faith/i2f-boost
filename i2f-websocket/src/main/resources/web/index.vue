<template>
  <div>
    {{websocket}}
  </div>
</template>

<script>

import WebsocketClient from './WebsocketClient'
import JsonWebsocketClient from './JsonWebsocketClient'

export default {
  data() {
    return {
      websocket: null
    }
  },
  computed: {

  },
  watch: {

  },
  async mounted() {
    this.websocket = JsonWebsocketClient({
      url: 'ws://localhost:8080/handler?token=xxx',
      debug: true,
      onMessage: e => {
        console.log('message', e)
      },
      onOpen: e => {
        console.log('opened.', e)
      },
      onError: e => console.log('error!', e),
      onClose: e => console.log('closed.', e)
    })
    await this.websocket.connect()
    console.log('websocket', this.websocket)
    this.websocket.requestMessage('hello', 'hello websocket').then(res => {
      console.log('websocket:response', res)
    }).catch(error => {
      console.log('websocket:response:error', error)
    })
  },
  destroyed() {
    this.websocket.close()
  },
  methods: {

  }
}
</script>

<style scoped>
</style>
