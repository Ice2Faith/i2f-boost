<template>
  <div id="app">
    <HelloWorld />
  </div>
</template>

<script>
import HelloWorld from './components/HelloWorld.vue'
import SecureTransfer from "@/secure/core/SecureTransfer";
import SecureCallback from '@/secure/core/SecureCallback'
import SecureConfig from "@/secure/SecureConfig";

export default {
  name: 'App',
  components: {
    HelloWorld
  },
  created() {
    if (SecureConfig.enableSwapAsymKey) {
      this.swapAsymKey()
      SecureCallback.callSwapKey = this.swapAsymKey
      let _this = this
      window.rsaTimer = setInterval(function () {
        _this.swapAsymKey()
      }, 5 * 60 * 1000)
    } else {
      this.initAsymOthPubKey()
      this.initAsymSlfPriKey()
      SecureCallback.callPubKey = this.initAsymOthPubKey
      SecureCallback.callPriKey = this.initAsymSlfPriKey
      let _this = this
      window.rsaTimer = setInterval(function () {
        _this.initAsymPubKey()
      }, 5 * 60 * 1000)
    }
  },
  destroyed() {
    clearInterval(window.rsaTimer)
  },
  methods: {
    swapAsymKey() {
      this.$axios({
        url: 'secure/swapKey',
        method: 'post',
        data: {
          key: SecureTransfer.loadWebAsymSlfPubKey()
        }
      }).then(({data}) => {
        console.log('SECURE_KEY', data)
        SecureTransfer.saveAsymOthPubKey(data)
      })
    },
    initAsymOthPubKey() {
      this.$axios({
        url: 'secure/key',
        method: 'post'
      }).then(({data}) => {
        console.log('SECURE_KEY', data)
        SecureTransfer.saveAsymOthPubKey(data)
      })
    },
    initAsymSlfPriKey() {
      this.$axios({
        url: 'secure/clientKey',
        method: 'post'
      }).then(({data}) => {
        console.log('SECURE_KEY', data)
        SecureTransfer.saveAsymSlfPriKey(data)
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
