<template>
  <div class="title" @click="change">
    <slot name="title"></slot>
    <span :class="['iconfont', show ? 'icon-up' : 'icon-down']"></span>
  </div>
  <div class="content" v-show="show">
    <slot></slot>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick } from "vue";
import { useRouter } from "vue-router";
const { proxy } = getCurrentInstance();
const router = useRouter();

const show = ref(false);

const change = () => {
  show.value = !show.value;
};
</script>

<style lang="scss" scoped>
.title {
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #f1f2f3;
  border-radius: 5px;
  height: 44px;
  padding: 0px 20px;
}
</style>
