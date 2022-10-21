<template>
  <div>
      <textarea v-model="form.input" rows="5" cols="20">
      </textarea>
    <hr/>
    <textarea v-model="form.output" rows="5" cols="20">
      </textarea>
    <hr/>
    <button @click="send">发送</button>
  </div>
</template>

<script>
import {RsaProvider} from '@/secret/secret-core'

export default {
  name: 'SecretTest',
  data() {
    return {
      form: {
        input: '{}',
        output: '',
      }
    }
  },
  mounted() {
    this.handlePublicKey();
  },
  methods: {
    handlePublicKey() {
      this.$axios({
        url: 'test/handle',
        method: 'post',
        data: {}
      }).then(({data}) => {
        let opk = data.data;
        this.$secretWebConfig
          .secretWebCore()
          .storeOtherPublicKey(opk);
      });

    },
    send() {
      this.$secretAxios({
        url: 'test/both',
        method: 'post',
        data: JSON.parse(this.form.input)
      }).then(({data}) => {
        this.form.output = JSON.stringify(data);
      });
    }
  }
}
</script>

<style scoped>
textarea {
  width: 100%;
  height: 30%;
  font-family: Arial;
  font-size: 16px;
  border-radius: 5px;
  padding: 5px;
  border: solid 1px #eee;
}

textarea:hover {
  border: solid 2px darkorange;
}

button {
  border: solid 1px #eee;
  margin: 8px;
  padding: 5px;
  font-size: 18px;
  border-radius: 5px;
  color: white;
  background: limegreen;
  width: 120px;
  height: 38px;
}

button:hover {
  background: darkorange;
}
</style>
