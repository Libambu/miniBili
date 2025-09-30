<template>
  <div v-if="dataSource.list && dataSource.list.length == 0">
    <NoData msg="空间主人还没有投过视频哦~~"></NoData>
  </div>
  <div class="part-item" v-else>
    <div class="part-title">
      <div class="title-panel">
        <router-link class="title" :to="`/user/${route.params.userId}}/video`"
          >TA的视频
        </router-link>
        <div class="count-info">{{ dataSource.totalCount }}</div>
      </div>
      <router-link class="op-btn" :to="`/user/${route.params.userId}/video`"
        >更多&gt;</router-link
      >
    </div>
    <div class="video-list5">
      <VideoItem :data="item" v-for="item in dataSource.list"> </VideoItem>
    </div>
  </div>
</template>

<script setup>
import "@/assets/scss/uhome.scss";
import VideoItem from "./VideoItem.vue";
import { ref, reactive, getCurrentInstance, nextTick } from "vue";
const { proxy } = getCurrentInstance();
import { useRoute, useRouter } from "vue-router";
const route = useRoute();
const router = useRouter();

const dataSource = ref({});
const loadVideoList = async () => {
  let result = await proxy.Request({
    url: proxy.Api.uHomeLoadVideo,
    params: {
      userId: route.params.userId,
      type: 0,
    },
  });
  if (!result) {
    return;
  }
  dataSource.value = result.data;
};
loadVideoList();
</script>

<style lang="scss" scoped>
</style>
