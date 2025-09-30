<template>
  <div class="video-panel">
    <div class="video-title-panel">
      <div class="video-title">我的收藏</div>
    </div>
    <div v-if="dataSource.list && dataSource.list.length == 0">
      <NoData msg="空间主人还没有收藏视频哦~~"></NoData>
    </div>
    <DataGridList :dataSource="dataSource" v-else @loadData="loadVideoList">
      <template #default="{ data }">
        <div class="data-item">
          <div class="cover" @click="jump(data)">
            <Cover :source="data.videoCover"></Cover>
          </div>
          <div class="video-name" @click="jump(data)">{{ data.videoName||"已失效视频" }}</div>
          <div class="collection-info">
            <div class="collection-time">
              收藏于： {{ proxy.Utils.formatDate(data.actionTime) }}
            </div>
            <el-dropdown>
              <div class="iconfont icon-more" @click.stop v-show="myself"></div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click.stop="cancelCollection(data)">取消收藏</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </template>
    </DataGridList>
  </div>
</template>

<script setup>
import VideoItem from './VideoItem.vue'
import { ref, reactive, getCurrentInstance, nextTick, computed } from 'vue'
const { proxy } = getCurrentInstance()
import { useRoute, useRouter } from 'vue-router'
const route = useRoute()
const router = useRouter()

import { useLoginStore } from '@/stores/loginStore.js'
const loginStore = useLoginStore()

//是否是自己
const myself = ref(loginStore.userInfo.userId == route.params.userId)

const dataSource = ref({})
const loadVideoList = async () => {
  let params = {
    pageNo: dataSource.value.pageNo,
  }
  params.userId = route.params.userId
  let result = await proxy.Request({
    url: proxy.Api.uHomeLoadCollection,
    params,
  })
  if (!result) {
    return
  }
  dataSource.value = result.data
}
loadVideoList()

const cancelCollection = (data) => {
  proxy.Confirm({
    message: '确定要取消收藏吗？',
    okfun: async () => {
      let result = await proxy.Request({
        url: proxy.Api.userAction,
        params: {
          videoId: data.videoId,
          actionType: 3,
        },
      })
      if (!result) {
        return
      }
      loadVideoList()
    },
  })
}

const jump = (item) => {
  if (!item.videoName) {
    return
  }
  router.push(`/video/${item.videoId}`)
}
</script>

<style lang="scss" scoped>
.video-panel {
  padding: 20px;
  background: #fff;
  border-radius: 5px;
  .video-title-panel {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
    .video-title {
      font-size: 18px;
    }
  }
  .data-item {
    .video-name {
      height: 35px;
      font-size: 13px;
      margin-top: 5px;
      color: var(--text2);
      margin-top: 10px;
      display: -webkit-box;
      overflow: hidden;
      -webkit-box-orient: vertical;
      text-overflow: -o-ellipsis-lastline;
      text-overflow: ellipsis;
      word-break: break-word !important;
      word-break: break-all;
      line-break: anywhere;
      -webkit-line-clamp: 2;
      cursor: pointer;
    }
    .collection-info {
      color: var(--text3);
      margin-top: 5px;
      display: flex;
      justify-content: space-between;
      font-size: 13px;
      .icon-more {
        cursor: pointer;
      }
    }
  }
}
</style>
