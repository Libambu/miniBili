<template>
  <div class="cover" :style="{ height: 200 * props.scale + 'px' }">
    <el-image :src="coverFile" fit="scale-down" width="200" v-if="coverFile">
      <template #error>
        <div class="iconfont icon-image-error"></div>
      </template>
    </el-image>
    <div class="mask" @click="selectImage">{{props.coverImage?"重新上传":"上传"}}</div>
  </div>
  <ImageCoverCut ref="imageCoverCutRef" :cutWidth="props.cutWidth" :scale="props.scale"></ImageCoverCut>
</template>
<script setup>
import ImageCoverCut from './ImageCoverCut.vue'
import {
  ref,
  reactive,
  getCurrentInstance,
  nextTick,
  inject,
  computed,
} from 'vue'
import { asyncComputed } from '@vueuse/core'

const { proxy } = getCurrentInstance()
import { useRoute, useRouter } from 'vue-router'
const route = useRoute()
const router = useRouter()

const props = defineProps({
  coverImage: {
    type: [String, File],
  },
  cutWidth: {
    type: Number,
    default: 150,
  },
  //高宽比例
  scale: {
    type: Number,
    default: 1,
  },
})

const coverFile = asyncComputed(async () => {
  if (!props.coverImage) {
    return null
  }
  if (typeof props.coverImage == 'string') {
    return proxy.Api.sourcePath + props.coverImage
  } else if (props.coverImage instanceof File) {
    const base64 = await convertFile2Base64(proxy.coverImage)
    return base64
  }
})

const convertFile2Base64 = (file) => {
  return new Promise((resolve, reject) => {
    let img = new FileReader()
    img.readAsDataURL(file)
    img.onload = ({ target }) => {
      resolve(target.result)
    }
  })
}

const imageCoverCutRef = ref()
const selectImage = async () => {
  imageCoverCutRef.value.show()
}
</script>

<style lang="scss" scoped>
.cover {
  width: 170px;
  background: #f0f0f0;
  position: relative;
  .mask {
    width: 100%;
    position: absolute;
    left: 0px;
    bottom: 0px;
    height: 30px;
    background: rgba(0, 0, 0, 0.7);
    opacity: 0.8;
    z-index: 1;
    color: #fff;
    text-align: center;
    cursor: pointer;
  }
}
</style>
