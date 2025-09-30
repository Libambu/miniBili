<template>
  <div class="search-panel">
    <!-- 下拉框 -->
    <el-select clearable placeholder="选择视频搜索" v-model="searchForm.videoId" @change="loadData">
      <el-option :value="item.videoId" :label="item.videoName" v-for="item in allVideoList"></el-option>
    </el-select>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
const { proxy } = getCurrentInstance()
const router = useRouter()
const route = useRoute()

const searchForm = ref({ videoId: route.query.videoId })
const allVideoList = ref([])
const loadAllVideo = async () => {
  let result = await proxy.Request({
    url: proxy.Api.ucLoadAllVideo,
  })
  if (!result) {
    return
  }
  allVideoList.value = result.data
}
loadAllVideo()

const emit = defineEmits(['loadData'])
const loadData = (e) => {
  emit('loadData', e)
}
</script>

<style lang="scss" scoped>
.search-panel {
  width: 200px;
  float: right;
  height: 40px;
}
</style>
