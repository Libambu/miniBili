<template>
  <template v-for="item in dataList">
    <VideoItem :data="item" :layoutType="1" :marginTop="20"></VideoItem>
  </template>
</template>

<script setup>
import {
  ref,
  reactive,
  getCurrentInstance,
  nextTick,
  inject,
  onMounted,
} from "vue";
const { proxy } = getCurrentInstance();
import { useRoute, useRouter } from "vue-router";
const route = useRoute();
const router = useRouter();

const dataList = ref([]);

const videoInfo = inject("videoInfo");

const loadDataList = async () => {
  let result = await proxy.Request({
    url: proxy.Api.getVideoRecommend,
    params: {
      keyword: videoInfo.value.videoName,
      videoId: videoInfo.value.videoId,
    },
  });
  if (!result) {
    return;
  }
  dataList.value = result.data;
};
loadDataList();
</script>

<style lang="scss" scoped>
</style>
