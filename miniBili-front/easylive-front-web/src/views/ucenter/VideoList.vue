<template>
  <div class="video-tab">
    <div class="tab">视频管理</div>
    <div class="search">
      <el-input
        clearable
        placeholder="搜索稿件"
        size="small"
        v-model="videoNameFuzzy"
        @keyup.enter="loadVideoList"
      >
        <template #suffix>
          <span class="iconfont icon-search"></span>
        </template>
      </el-input>
    </div>
  </div>
  <div class="video-manage">
    <div class="top-info">
      <div class="all-video-panel">
        <div class="all-video" @click="cleanStatusLoad">
          全部稿件<span class="count-info">{{
            countInfo.inProgress +
            countInfo.auditPassCount +
            countInfo.auditFailCount
          }}</span>
        </div>
      </div>
      <div class="video-status">
        <span
          :class="['item', status == -1 ? 'active' : '']"
          @click="statusLoad(-1)"
          >进行中<span class="count-info">{{
            countInfo.inProgress
          }}</span></span
        >
        <el-divider direction="vertical" />
        <span
          :class="['item', status == 3 ? 'active' : '']"
          @click="statusLoad(3)"
          >已通过<span class="count-info">{{
            countInfo.auditPassCount
          }}</span></span
        >
        <el-divider direction="vertical" />
        <span
          :class="['item', status == 4 ? 'active' : '']"
          @click="statusLoad(4)"
          >未通过<span class="count-info">{{
            countInfo.auditFailCount
          }}</span></span
        >
      </div>
    </div>
    <div class="video-list">
      <DataList :dataSource="dataSource" @loadData="loadVideoList">
        <template #default="{ data }">
          <VideoItem :data="data" @reload="loadVideoList"></VideoItem>
        </template>
      </DataList>
    </div>
  </div>
</template>

<script setup>
import VideoItem from "./VideoItem.vue";
import {
  ref,
  reactive,
  getCurrentInstance,
  nextTick,
  onMounted,
  onUnmounted,
} from "vue";
const { proxy } = getCurrentInstance();
import { useRoute, useRouter } from "vue-router";
const route = useRoute();
const router = useRouter();

const videoNameFuzzy = ref();
const status = ref();
const statusLoad = (_status) => {
  status.value = _status;
  loadVideoList();
};
const cleanStatusLoad = () => {
  status.value = null;
  loadVideoList();
};

const dataSource = ref({});
const loadVideoList = async () => {
  let params = {
    pageNo: dataSource.value.pageNo,
    videoNameFuzzy: videoNameFuzzy.value,
    status: status.value,
  };

  let result = await proxy.Request({
    url: proxy.Api.loadUcenterVideoList,
    params,
  });
  if (!result) {
    return;
  }
  dataSource.value = result.data;
};
loadVideoList();

const countInfo = ref({ inProgress: 0, auditPassCount: 0, auditFailCount: 0 });
const loadCountInfo = async () => {
  let result = await proxy.Request({
    url: proxy.Api.getUcenterVideoCountInfo,
  });
  if (!result) {
    return;
  }
  countInfo.value = result.data;
};
loadCountInfo();

let timmer = ref(null);
const startTimer = () => {
  timmer.value = setInterval(() => {
    loadVideoList();
    loadCountInfo();
  }, 5000);
};

const cleanTimer = () => {
  if (timmer.value !== null) {
    clearInterval(timmer.value);
    timmer.value = null;
  }
};
onMounted(() => {
  startTimer();
});

onUnmounted(() => {
  cleanTimer();
});
</script>

<style lang="scss" scoped>
.video-tab {
  border-bottom: 1px solid #ddd;
  display: flex;
  padding: 0px 40px;
  justify-content: space-between;
  .tab {
    cursor: pointer;
    font-weight: bold;
    font-size: 16px;
    color: var(--blue);
    padding-bottom: 15px;
    border-bottom: 3px solid var(--blue);
  }
  .search {
    width: 200px;
  }
}
.video-manage {
  margin-top: 10px;
  padding: 0px 40px 10px 40px;
  .top-info {
    .count-info {
      padding: 0px 5px;
    }
    .all-video-panel {
      display: flex;
      .all-video {
        cursor: pointer;
        font-size: 14px;
        color: var(--blue);
      }
    }
    .video-status {
      margin-top: 10px;
      display: flex;
      align-items: center;
      .item {
        cursor: pointer;
        font-size: 13px;
        margin-right: 0px;
        color: var(--text2);
        &:hover {
          color: var(--blue);
        }
      }
      .active {
        color: var(--blue);
      }
    }
  }
}
</style>
