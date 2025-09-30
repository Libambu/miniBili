<template>
  <div class="message-item">
    <div class="sys-info">
      <div :class="[
          'video-name iconfont',
          data.extendDto.auditStatus == 3 ? 'icon-success' : 'icon-error',
        ]">
        视频 【{{ data.videoName }}】审核
        {{ data.extendDto.auditStatus == 3 ? "成功" : "失败" }}
      </div>
      <div class="resean" v-if="data.extendDto.auditStatus != 3 ">
        失败原因：{{ data.extendDto.messageContent }}
      </div>
      <div class="success" v-if="data.extendDto.auditStatus == 3">
        <router-link class="a-link" :to="`/video/${data.videoId}`" target="_blank">点击查看</router-link>
      </div>
      <div class="send-time">
        {{ proxy.Utils.formatDate(data.createTime) }}
        <span class="iconfont icon-delete" @click="delMessage(data.messageId)"></span>
      </div>
    </div>
    <div class="video-cover">
      <router-link :to="`/video/${data.videoId}`" target="_blank">
        <Cover :source="data.videoCover"></Cover>
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick } from 'vue'
import { useRouter } from 'vue-router'
const { proxy } = getCurrentInstance()
const router = useRouter()

const props = defineProps({
  data: {
    type: Object,
    default: {},
  },
})

const emit = defineEmits(['delMessage'])
const delMessage = (messageId) => {
  emit('delMessage', messageId)
}
</script>

<style lang="scss" scoped>
.message-item {
  display: flex;
  padding: 10px 5px;
  border-bottom: 1px solid #ddd;
  align-items: center;
  .sys-info {
    flex: 1;
    .video-name {
      flex: 1;
      font-size: 14px;
      color: var(--text2);
      &::before {
        margin-right: 5px;
        font-size: 16px;
      }
    }
    .icon-success {
      &::before {
        color: green;
      }
    }
    .icon-error {
      color: red;
    }
    .resean {
      margin-top: 10px;
      color: red;
    }
    .success {
      margin-top: 10px;
    }
    .send-time {
      margin-top: 5px;
      font-size: 12px;
      color: var(--text3);
    }
    .icon-delete {
      font-size: 14px;
      cursor: pointer;
    }
  }
  .video-cover {
    margin-left: 10px;
    width: 100px;
  }
}
</style>
