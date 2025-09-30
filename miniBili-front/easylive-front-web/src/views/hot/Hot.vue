<template>
  <div class="hot-container">
    <div class="hot-part-title-panel">
      <div class="hot-24">
        <div class="iconfont icon-hot"></div>
        <div>24小时热榜</div>
      </div>
    </div>
    <div class="data-list">
      <DataLoadMoreList :dataSource="dataSource" :loading="loadingData" @loadData="loadDataList" :gridCount="2">
        <template #default="{ data }">
          <VideoItem :data="data" :marginTop="20" :layoutType="1"></VideoItem>
        </template>
      </DataLoadMoreList>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick } from 'vue'
const { proxy } = getCurrentInstance()
import { useRoute, useRouter } from 'vue-router'
const route = useRoute()
const router = useRouter()

const loadingData = ref(false)
const dataSource = ref({})
const loadDataList = async () => {
  let params = {
    pageNo: dataSource.value.pageNo,
  }
  loadingData.value = true
  let result = await proxy.Request({
    url: proxy.Api.hotVideoList,
    params,
  })
  loadingData.value = false
  if (!result) {
    return
  }
  const dataList = dataSource.value.list
  dataSource.value = Object.assign({}, result.data)
  if (result.data.pageNo > 1) {
    dataSource.value.list = dataList.concat(result.data.list)
  }
}
loadDataList()
</script>

<style lang="scss" scoped>
.hot-container {
  margin: 20px auto 0px;
  min-width: 1070px;
  max-width: 1286px;
  .hot-part-title-panel {
    border-bottom: 1px solid #ddd;
    padding: 10px 0px 20px 0px;
    display: flex;
    .hot-24 {
      font-size: 20px;
      display: flex;
      align-items: center;
      position: relative;
      &::after {
        content: '';
        position: absolute;
        border-bottom: 2px solid var(--blue);
        width: 100%;
        bottom: -20px;
      }
      .icon-hot {
        width: 46px;
        height: 46px;
        background: #f07775;
        color: #fff;
        font-size: 20px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 10px;
      }
    }
  }
  .data-list {
    margin-top: 10px;
  }
}
</style>
