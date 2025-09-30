<template>
  <div class="tag" :style="{'border':`1px solid ${TYPE[type].color}`,'color':`${TYPE[type].color}`}">{{TYPE[type].text}}
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
const { proxy } = getCurrentInstance()
const router = useRouter()
const route = useRoute()

const TYPE = {
  0: {
    text: '置顶',
    color: '#FF6699',
  },
}

const props = defineProps({
  type: {
    type: Number,
  },
})
</script>

<style lang="scss" scoped>
.tag {
  display: inline-flex;
  justify-content: center;
  align-items: center;
  position: relative;
  width: 30px;
  height: 18px;
  border-radius: 3px;
  vertical-align: text-bottom;
  margin-bottom: 0.0666em;
  font-size: 12px;
  margin-right: 5px;
}
</style>
