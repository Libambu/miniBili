<template>
  <Dialog
    :show="dialogConfig.show"
    :title="dialogConfig.title"
    :buttons="dialogConfig.buttons"
    width="90%"
    :showCancel="false"
    @close="closeWin"
  >
    <div class="video-detail">
      <div class="video-info">
        <el-tabs v-model="activeName">
          <el-tab-pane label="视频分P" name="video">
            <div class="video-tips">红色标题代表视频有更新</div>
            <el-scrollbar :max-height="400" class="video-list">
              <div
                :class="['video-item', index == currentP - 1 ? 'active' : '']"
                v-for="(item, index) in videoFileList"
                @click="selectVideo(index + 1)"
              >
                <div class="playing" v-if="index == currentP - 1"></div>
                <div
                  :class="['title', item.updateType == 1 ? 'update' : '']"
                  :title="item.title"
                >
                  P{{ index + 1 }} {{ item.fileName }}
                </div>
                <div class="duration">
                  {{ proxy.Utils.convertSecondsToHMS(item.duration) }}
                </div>
              </div>
            </el-scrollbar>
          </el-tab-pane>
          <el-tab-pane label="基本信息" name="base">
            <div class="video-base-info">
              <div class="base-info-item">
                <div class="item-title">标题：</div>
                <div class="item-value">{{ videoInfo.videoName }}</div>
              </div>
              <div class="base-info-item">
                <div class="item-title">发布人：</div>
                <div class="item-value">{{ videoInfo.nickName }}</div>
              </div>
              <div class="base-info-item">
                <div class="item-title">类型：</div>
                <div class="item-value">
                  {{ videoInfo.postType == 0 ? "自制" : "转载" }}
                </div>
              </div>

              <div v-if="videoInfo.postType == 1" class="base-info-item">
                <div class="item-title">资源说明：</div>
                <div class="item-value">{{ videoInfo.originInfo }}</div>
              </div>
              <div class="base-info-item">
                <div class="item-title">标签：</div>
                <div class="item-value">{{ videoInfo.tags }}</div>
              </div>
              <div class="base-info-item">
                <div class="item-title">简介：</div>
                <div class="item-value">{{ videoInfo.introduction }}</div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
      <div class="video-play">
        <Player ref="playerRef" :autoplay="false"></Player>
      </div>
    </div>
  </Dialog>
</template>

<script setup>
import { mitter } from "@/eventbus/eventBus.js";
import { ref, reactive, getCurrentInstance, nextTick } from "vue";
const { proxy } = getCurrentInstance();
import { useRoute, useRouter } from "vue-router";
const route = useRoute();
const router = useRouter();

const dialogConfig = ref({
  show: false,
});

const activeName = ref("video");

const videoInfo = ref();
const videoFileList = ref([]);
//播放器
const playerRef = ref();
//当前播放的视频
const currentP = ref(1);
const show = (data) => {
  dialogConfig.value.show = true;
  videoInfo.value = Object.assign({}, data);
  currentP.value = 1;
  loadPList();
};

const loadPList = async () => {
  let result = await proxy.Request({
    url: proxy.Api.loadVideoPList,
    params: {
      videoId: videoInfo.value.videoId,
    },
  });
  if (!result) {
    return;
  }
  videoFileList.value = result.data;
  nextTick(() => {
    playerRef.value.showPlayer(window.innerHeight - 150);
    selectVideoFile();
  });
};

const selectVideo = (index) => {
  currentP.value = index;
  selectVideoFile();
};

const selectVideoFile = () => {
  mitter.emit("changeP", videoFileList.value[currentP.value - 1].fileId);
};

const closeWin = () => {
  dialogConfig.value.show = false;
  playerRef.value.destroyPlayer();
};

defineExpose({
  show,
});
</script>

<style lang="scss" scoped>
.video-detail {
  display: flex;
  .video-info {
    width: 400px;
    .video-base-info {
      padding-right: 10px;
      .base-info-item {
        margin-top: 5px;
        display: flex;
        .item-title {
          width: 60px;
          text-align: right;
          font-size: 15px;
        }
        .item-value {
          flex: 1;
        }
      }
    }
    .video-tips {
      font-size: 13px;
      color: red;
    }
    .video-list {
      .video-item {
        padding: 6px 8px 6px 0px;
        display: flex;
        align-items: center;
        cursor: pointer;
        margin-top: 5px;
        border-radius: 3px;
        .playing {
          width: 14px;
          height: 14px;
          margin-right: 5px;
          background-position: center center;
          background-size: cover;
          background-repeat: no-repeat;
          background-image: url("@/assets/playing.gif");
        }
        .title {
          font-size: 14px;
          flex: 1;
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
        }
        .update {
          color: red;
        }
        .duration {
          margin-left: 5px;
        }
        &:hover {
          background: #fff;
        }
      }
      .active {
        background: #fff;
      }
    }
  }
  .video-play {
    flex: 1;
  }
}
</style>
