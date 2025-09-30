<template>
  <div class="upload-video-panel">
    <VideoUploader ref="videoUploaderRef"> </VideoUploader>
    <div v-if="startUpload" class="video-form">
      <el-form :model="formData" :rules="rules" ref="formDataRef" label-width="70px" @submit.prevent>
        <el-form-item label="封面" prop="videoCover">
          <ImageCoverSelect :coverWidth="200" :cutWidth="680" :scale="0.6" :coverImage="formData.videoCover">
          </ImageCoverSelect>
        </el-form-item>
        <!--input输入-->
        <el-form-item label="标题" prop="videoName">
          <el-input clearable placeholder="请输入标题" v-model="formData.videoName" maxlength="100"
            show-word-limit></el-input>
        </el-form-item>
        <!-- 单选 -->
        <el-form-item label="类型" prop="postType">
          <el-radio-group v-model="formData.postType">
            <el-radio :value="0">自制</el-radio>
            <el-radio :value="1">转载</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="" prop="originInfo" v-if="formData.postType == 1">
          <el-input clearable placeholder="转载视频请注明来源、时间、地点(例：转自https://www.xxxx.com/yyyy)，注明来源会更快地通过审核哦"
            v-model="formData.originInfo" maxlength="200" show-word-limit></el-input>
        </el-form-item>
        <el-form-item label="标签" prop="tags">
          <TagInput v-model="formData.tags"></TagInput>
        </el-form-item>
        <el-form-item label="分区" prop="categoryArray">
          <el-cascader v-model="formData.categoryArray" :options="categoryStore.categoryList"
            :props="{ value: 'categoryId', label: 'categoryName' }" />
        </el-form-item>

        <!--textarea输入-->
        <el-form-item label="简介" prop="introduction">
          <el-input clearable placeholder="填写更全面的相关信息，让更多的人能找到你的视频吧(: 	" type="textarea" :rows="5" :maxlength="2000"
            resize="none" show-word-limit v-model="formData.introduction"></el-input>
        </el-form-item>
        <el-form-item label="互动设置" prop="introduction">
          <el-checkbox-group v-model="formData.interactionArray">
            <el-checkbox value="0">关闭弹幕</el-checkbox>
            <el-checkbox value="1">关闭评论</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="">
          <el-button type="primary" @click="submitForm">立即投稿</el-button>
          <el-button @click="router.push('/ucenter/video')">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { useCategoryStore } from '@/stores/categoryStore.js'
const categoryStore = useCategoryStore()
import TagInput from './TagInput.vue'
import VideoUploader from './VideoUploader.vue'
import {
  ref,
  reactive,
  getCurrentInstance,
  nextTick,
  onMounted,
  onUnmounted,
  watch,
  provide,
  inject,
} from 'vue'

import { uploadImage } from '@/utils/Api.js'

const { proxy } = getCurrentInstance()
import { useRoute, useRouter } from 'vue-router'
const route = useRoute()
const router = useRouter()

import { mitter } from '@/eventbus/eventBus.js'

const startUpload = ref(false)
mitter.on('startUpload', (fileName) => {
  startUpload.value = true
  nextTick(() => {
    formDataRef.value.resetFields()
    formData.value = {}
    formData.value.tags = []
    formData.value.videoName = fileName
  })
})

const formData = ref({
  tags: [],
})
const formDataRef = ref()
const rules = {
  videoCover: [{ required: true, message: '封面不能为空' }],
  videoName: [{ required: true, message: '标题不能为空' }],
  postType: [{ required: true, message: '类型不能为空' }],
  originInfo: [{ required: true, message: '转载说明不能为空' }],
  categoryArray: [{ required: true, message: '分区不能为空' }],
  tags: [{ required: true, message: '标签不能为空' }],
}

provide('cutImageCallback', ({ coverImage }) => {
  formData.value.videoCover = coverImage
})

const videoUploaderRef = ref()
const videoList = ref([])

const submitForm = () => {
  const uploadFileList = videoUploaderRef.value.getUploadFileList()
  if (!uploadFileList) {
    return
  }
  formDataRef.value.validate(async (valid) => {
    if (!valid) {
      return
    }
    let params = {
      uploadFileList: JSON.stringify(uploadFileList),
    }
    Object.assign(params, formData.value)
    //处理分裂
    params.pCategoryId = params.categoryArray[0]
    if (params.categoryArray.length > 1) {
      params.categoryId = params.categoryArray[1]
    }
    delete params.categoryArray

    //互动设置
    if (params.interactionArray) {
      params.interaction = params.interactionArray.join(',')
      delete params.interactionArray
    }
    //判断文件
    if (params.videoCover instanceof File) {
      const videoCover = await uploadImage(params.videoCover)
      if (!videoCover) {
        return
      }
      params.videoCover = videoCover
    }
    let result = await proxy.Request({
      url: proxy.Api.postVideo,
      showLoading: true,
      params,
    })
    if (!result) {
      return
    }
    proxy.Message.success('发布成功')
    router.push('/ucenter/video')
  })
}

//编辑
const videoId = ref()
const init = async () => {
  nextTick(() => {
    videoUploaderRef.value.initUploader(startUpload.value, [])
  })
  if (videoId.value) {
    let result = await proxy.Request({
      url: proxy.Api.getVideoByVideoId,
      params: {
        videoId: videoId.value,
      },
    })
    if (!result) {
      return
    }
    formData.value = result.data.videoInfo
    //处理tags
    formData.value.tags = formData.value.tags.split(',')
    //处理分类
    formData.value.categoryArray = []
    if (formData.value.pCategoryId) {
      formData.value.categoryArray.push(formData.value.pCategoryId)
    }
    if (formData.value.categoryId) {
      formData.value.categoryArray.push(formData.value.categoryId)
    }
    //处理互动设置
    formData.value.interactionArray = formData.value.interaction
      ? formData.value.interaction.split(',')
      : []

    nextTick(() => {
      videoUploaderRef.value.initUploader(
        startUpload.value,
        result.data.videoInfoFileList
      )
    })
  }
}
watch(
  () => route.query.videoId,
  (newVal, oldVal) => {
    if (newVal) {
      startUpload.value = true
    } else {
      startUpload.value = false
    }
    videoId.value = newVal
    init()
  },
  { immediate: true, deep: true }
)

//重新加载
const reload = () => {}
onMounted(() => {
  window.addEventListener('beforeunload', reload)
})

onUnmounted(() => {
  mitter.off('startUpload')

  window.removeEventListener('beforeunload', reload)
})
</script>

<style lang="scss" scoped>
.upload-video-panel {
  background: #fff;
  padding: 20px;
}
.video-form {
  padding-right: 200px;
}
</style>
