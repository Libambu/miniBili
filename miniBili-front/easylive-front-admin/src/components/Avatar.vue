
<template>
  <div class="image-panel" :style="{ width: width + 'px', height: width + 'px' }">
    <router-link v-if="userId" :to="`/user/${userId}`" target="_blank">
      <Cover :source="avatar" :defaultImg="defaultAvatar" borderRadius="50%"></Cover>
    </router-link>
    <Cover v-else :source="avatar" :defaultImg="defaultAvatar" borderRadius="50%"></Cover>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick } from 'vue'
const { proxy } = getCurrentInstance()
import { useRoute, useRouter } from 'vue-router'
const route = useRoute()
const router = useRouter()
const defaultAvatar = ref('user.png')
const props = defineProps({
  width: {
    type: Number,
    default: 50,
  },
  avatar: {
    type: String,
  },
  userId: {
    type: String,
  },
})
</script>

<style lang="scss" scoped>
</style>
