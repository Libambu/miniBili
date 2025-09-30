<template>
  <div class="summary-panel">
    <div class="summary" :style="{ height: expandType ? 'auto' : '90px' }">
      <div
        class="summary-inner"
        id="summary-inner"
        v-html="videoInfo.introduction"
      ></div>
    </div>
    <div class="expand-btn" @click="expand" v-show="showExpandBtn">
      {{ expandType ? "收起" : "展开更多" }}
    </div>
    <div class="tag-list">
      <router-link
        :to="`/search?keyword=${item}`"
        class="tag-item"
        target="_blank"
        v-for="item in videoInfo.tags"
        >{{ item }}</router-link
      >
    </div>
  </div>
</template>

<script setup>
import {
  ref,
  reactive,
  getCurrentInstance,
  nextTick,
  onMounted,
  computed,
  inject,
  onUpdated,
} from "vue";
const { proxy } = getCurrentInstance();
import { useRoute, useRouter } from "vue-router";
const route = useRoute();
const router = useRouter();

const videoInfo = inject("videoInfo");

const showExpandBtn = ref(false);
//是否展开
const expandType = ref(false);
const expand = () => {
  expandType.value = !expandType.value;
};

onMounted(() => {
  const height = document.querySelector("#summary-inner").clientHeight;
  if (height > 90) {
    expandType.value = false;
    showExpandBtn.value = true;
  } else {
    expandType.value = true;
    showExpandBtn.value = false;
  }
});
</script>

<style lang="scss" scoped>
.summary-panel {
  padding: 20px 0px;
  border-bottom: 1px solid #ddd;
  .summary {
    overflow: hidden;
  }
  .expand-btn {
    display: inline-block;
    margin-top: 5px;
    cursor: pointer;
    &:hover {
      color: var(--blue);
    }
  }
  .tag-list {
    margin-top: 20px;
    display: flex;
    flex-wrap: wrap;
    .tag-item {
      cursor: pointer;
      text-decoration: none;
      color: var(--text2);
      background: #f1f2f3;
      border-radius: 16px;
      height: 32px;
      line-height: 32px;
      padding: 0px 12px;
      margin: 0 12px 8px 0;
    }
  }
}
</style>
