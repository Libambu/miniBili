<template>
  <div :style="{ width: width + 'px', height: width + 'px' }">
    <router-link v-if="userId" :to="`/user/${userId}`" target="_blank">
      <Cover
        :lazy="lazy"
        :width="width"
        :source="avatar"
        :defaultImg="defaultAvatar"
        borderRadius="50%"
        :scale="1"
      >
      </Cover>
    </router-link>
    <Cover
      v-else
      :lazy="lazy"
      :source="avatar"
      :defaultImg="defaultAvatar"
      borderRadius="50%"
      :scale="1"
      :width="width"
    ></Cover>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick } from "vue";
const { proxy } = getCurrentInstance();
import { useRoute, useRouter } from "vue-router";
const route = useRoute();
const router = useRouter();
const defaultAvatar = ref("user.png");
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
  lazy: {
    type: Boolean,
    default: true,
  },
});
</script>

<style lang="scss" scoped>
</style>
