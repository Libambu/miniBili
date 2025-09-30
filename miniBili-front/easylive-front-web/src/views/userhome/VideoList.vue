<template>
  <div class="video-panel">
    <div class="video-title-panel">
      <div class="video-title">我的视频</div>
      <MyTab v-model="activeTab" @clickHandler="loadVideoList" :tags="[
          { name: '最新发布' },
          { name: '最多播放' },
          { name: '最多收藏' },
        ]"></MyTab>
    </div>
    <div v-if="dataSource.list && dataSource.list.length == 0">
      <NoData msg="空间主人还没有投过视频哦~~"></NoData>
    </div>
    <DataGridList :dataSource="dataSource" v-else @loadData="loadVideoList">
      <template #default="{ data }">
        <VideoItem :data="data"> </VideoItem>
      </template>
    </DataGridList>
  </div>
</template>

<script setup>
import VideoItem from './VideoItem.vue'
import { ref, reactive, getCurrentInstance, nextTick, watch } from 'vue'
const { proxy } = getCurrentInstance()
import { useRoute, useRouter } from 'vue-router'
const route = useRoute()
const router = useRouter()

const activeTab = ref(0)

const videoName = ref()
const dataSource = ref({})
const loadVideoList = async () => {
  let params = {
    pageNo: dataSource.value.pageNo,
    videoName: videoName.value,
    orderType: activeTab.value,
  }
  params.userId = route.params.userId
  let result = await proxy.Request({
    url: proxy.Api.uHomeLoadVideo,
    params,
  })
  if (!result) {
    return
  }
  dataSource.value = result.data
}
loadVideoList()

watch(
  () => route.query.videoName,
  (newVal, oldVal) => {
    videoName.value = newVal
    loadVideoList()
  },
  { immediate: true, deep: true }
)
</script>

<style lang="scss" scoped>
.video-panel {
  padding: 20px;
  background: #fff;
  border-radius: 5px;
  .video-title-panel {
    display: flex;
    align-items: center;
    .video-title {
      font-size: 18px;
    }
  }
}
</style>
