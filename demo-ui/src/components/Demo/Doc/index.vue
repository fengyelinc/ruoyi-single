<template>
  <div>
    <svg-icon icon-class="question" @click="goto" />
  </div>
</template>

<script>
export default {
  name: 'DemoDoc',
  data() {
    return {
      url: 'http://doc.demo.vip/demo-vue'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>