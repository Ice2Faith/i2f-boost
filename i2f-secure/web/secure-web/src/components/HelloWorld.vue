<template>
  <div>



    <button @click="invokeInt">int</button>
    <button @click="invokeStr">str</button>
    <button @click="invokeMap">map</button>

    <button @click="invokeEcho">echo</button>

    <button @click="invokeObj">obj</button>
  </div>
</template>

<script>
import SecureTransfer from "@/secure/core/secure-transfer";
import Random from "@/secure/util/random";

export default {
  name: 'HelloWorld',
  props: {

  },
  data:()=>{
    return {
      msg: 'aaa'
    }
  },
  created() {
    this.initRsaContent()
  },
  methods:{
    initRsaContent(){
      this.$axios({
        url: 'secure/key',
        method: 'post'
      }).then(({data})=>{
        console.log('SECURE_KEY',data)
        localStorage.setItem('SECURE_KEY',data)
        SecureTransfer.saveRsaPubKey(data)
      })
    },
    invokeObj(){
      let obj={
        username:"张三",
        age: 25,
        roles: ['admin','log','data']
      }
      this.$axios({
        url: 'test/obj',
        method: 'post',
        data: obj,
        headers: SecureTransfer.getSecureHeader(false,true)
      }).then(({data})=>{
        console.log('echo',data)
      })
    },
    invokeEcho(){
      this.$axios({
        url: 'test/echo',
        method: 'post',
        data: Random.nextInt(),
        headers: SecureTransfer.getSecureHeader(false)
      }).then(({data})=>{
        console.log('echo',data)
      })
    },
    invokeInt(){
      this.$axios({
        url: 'test/int',
        method: 'get'
      }).then(({data})=>{
        console.log('int',data)
      })
    },
    invokeStr(){
      this.$axios({
        url: 'test/str',
        method: 'get'
      }).then((res)=>{
        let data=res.data
        console.log('str',data)
      })
    },
    invokeMap(){
      this.$axios({
        url: 'test/map',
        method: 'get'
      }).then(({data})=>{
        console.log('map',data)
      })
    }
  }
}
</script>

<style scoped>
</style>
