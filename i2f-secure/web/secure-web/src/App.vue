<template>
  <div id="app">
    <HelloWorld />
  </div>
</template>

<script>
import HelloWorld from './components/HelloWorld.vue'
import SecureTransfer from "@/secure/core/secure-transfer";

export default {
  name: 'App',
  components: {
    HelloWorld
  },
  created() {
    this.initAsymContent()
    this.initClientContent()
    let _this = this
    window.rsaTimer = setInterval(function () {
      _this.initAsymContent()
    }, 5 * 60 * 1000)
  },
  destroyed() {
    clearInterval(window.rsaTimer)
  },
  methods: {
    initAsymContent() {
      this.$axios({
        url: 'secure/key',
        method: 'post'
      }).then(({data}) => {
        console.log('SECURE_KEY', data)
        SecureTransfer.saveAsymPubKey(data)
      })
    },
    initClientContent() {
      this.$axios({
        url: 'secure/clientKey',
        method: 'post'
      }).then(({data}) => {
        console.log('SECURE_KEY', data)
        SecureTransfer.saveAsymPriKey(data)
      })
    }
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
