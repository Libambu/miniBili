<template>
  <div class="content-404">
    <div class="bg"></div>
    <router-link class="gohome" to="/">返回首页</router-link>
    <div class="jump-auto">{{ leftSeconds }} 秒后自动跳转</div>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick } from "vue";
import { useRouter } from "vue-router";
const { proxy } = getCurrentInstance();
const router = useRouter();

const leftSeconds = ref(5);

const init = () => {
  setInterval(() => {
    leftSeconds.value--;
    if (leftSeconds.value == 0) {
      router.push("/");
    }
  }, 1000);
};
init();
</script>

<style lang="scss" scoped>
.content-404 {
  text-align: center;
  .bg {
    margin: 0px auto;
    width: 800px;
    height: 500px;
    background-image: url("../../assets/404.png");
  }
  .gohome {
    font-size: 18px;
    margin-top: 30px;
    text-align: center;
    color: var(--blue);
    text-decoration: none;
  }
  .jump-auto {
    margin-top: 20px;
    color: var(--text3);
  }
}
</style>
