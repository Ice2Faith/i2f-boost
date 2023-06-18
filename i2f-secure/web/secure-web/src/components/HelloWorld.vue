<template>
  <div>

    <button @click="invokeInt">int</button>
    <button @click="invokeStr">str</button>
    <button @click="invokeMap">map</button>

    <button @click="invokeEcho">echo</button>

    <button @click="invokeObj">obj</button>

    <button @click="invokeParam">param</button>
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
  },
  methods:{
    invokeParam(){
      let obj={
        name:"张三",
        age: 25,
        time: new Date().getTime(),
        index: 1,
        size: 30
      }
      this.$axios({
        url: 'test/param',
        method: 'post',
        params: obj,
        data: obj,
        headers: SecureTransfer.getSecureHeader(true,true)
      }).then(({data})=>{
        console.log('echo',data)
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
        method: 'get',
        headers: SecureTransfer.getSecureHeader(false, false)
      }).then(({data})=>{
        console.log('int',data)
      })
    },
    invokeStr(){
      this.$axios({
        url: 'test/str',
        method: 'get',
        headers: SecureTransfer.getSecureHeader(false, false)
      }).then((res)=>{
        let data=res.data
        console.log('str',data)
      })
    },
    invokeMap(){
      this.$axios({
        url: 'test/map',
        method: 'get',
        headers: SecureTransfer.getSecureHeader(false, false)
      }).then(({data})=>{
        console.log('map',data)
      })
    }
  }
}
</script>

<style scoped>
</style>
