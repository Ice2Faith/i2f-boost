import Vue from 'vue'
import App from './App.vue'

import './secure/secure-axios'
import './secure/secure-vue-main'

Vue.config.productionTip = false

new Vue({
  render: h => h(App),
}).$mount('#app')
