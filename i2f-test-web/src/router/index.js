import Vue from 'vue'
import Router from 'vue-router'
import SecretTest from "../components/SecretTest";

Vue.use(Router)

export default new Router({
    routes: [
        {
            path: '/',
            name: 'SecretTest',
            component: SecretTest
        }
    ]
})
