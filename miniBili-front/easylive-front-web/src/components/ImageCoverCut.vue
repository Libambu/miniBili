<template>
  <Dialog :show="dialogConfig.show" :title="dialogConfig.title" :buttons="dialogConfig.buttons" width="1000px"
    @close="dialogConfig.show = false">
    <div class="cut-image-panel">
      <VueCropper ref="cropperRef" class="cropper" :img="sourceImage" outputType="png" :autoCrop="true"
        :autoCropWidth="props.cutWidth" :autoCropHeight="Math.round(props.cutWidth * props.scale)" :fixed="true"
        :fixedNumber="[1, props.scale]" :centerBox="true" :full="false" :fixedBox="true" @realTime="prview" mode="100%">
      </VueCropper>
      <div class="preview-panel">
        <div class="preview-image">
          <img :src="previewsImage" />
        </div>
        <el-upload :multiple="false" :show-file-list="false" :http-request="selectFile" :accept="proxy.imageAccept">
          <el-button class="select-btn" type="primary" @click="">选择图片</el-button>
        </el-upload>
      </div>
    </div>
    <div class="info">
      建议上传至少{{ props.cutWidth }}*{{
        Math.round(props.cutWidth * props.scale)
      }}的图片
    </div>
  </Dialog>
</template>

<script setup>
import 'vue-cropper/dist/index.css'
import { VueCropper } from 'vue-cropper'

import { ref, reactive, getCurrentInstance, nextTick, inject } from 'vue'
const { proxy } = getCurrentInstance()
import { useRoute, useRouter } from 'vue-router'
const route = useRoute()
const router = useRouter()

/**
 * 参数说明
  img: '', // 裁剪图片的地址 url 地址, base64, blob
  outputSize: 1, // 裁剪生成图片的质量
  outputType: 'jpeg', // 裁剪生成图片的格式 jpeg, png, webp
  info: true, // 裁剪框的大小信息
  canScale: false, // 图片是否允许滚轮缩放
  autoCrop: true, // 是否默认生成截图框
  autoCropWidth: 150, // 默认生成截图框宽度
  autoCropHeight: 150, // 默认生成截图框高度
  fixedBox: false, // 固定截图框大小 不允许改变
  fixed: false, // 是否开启截图框宽高固定比例，这个如果设置为true，截图框会是固定比例缩放的，如果设置为false，则截图框的狂宽高比例就不固定了
  fixedNumber: [1, 1], // 截图框的宽高比例 [ 宽度 , 高度 ]
  canMove: true, // 上传图片是否可以移动
  canMoveBox: true, // 截图框能否拖动
  original: false, // 上传图片按照原始比例渲染
  centerBox: true, // 截图框是否被限制在图片里面
  infoTrue: true, // true 为展示真实输出图片宽高 false 展示看到的截图框宽高
  full: true, // 是否输出原图比例的截图
  enlarge: '1', // 图片根据截图框输出比例倍数
  mode: 'contain' // 图片默认渲染方式 contain , cover, 100px, 100% auto
 */
const props = defineProps({
  cutWidth: {
    type: Number,
    default: 400,
  },
  //高宽比例
  scale: {
    type: Number,
    default: 0.5,
  },
})

const dialogConfig = ref({
  show: false,
  title: '上传图片',
  buttons: [
    {
      type: 'primary',
      text: '确定',
      click: (e) => {
        cutImage()
      },
    },
  ],
})

const cropperRef = ref()
const previewsImage = ref()
const prview = (data) => {
  cropperRef.value.getCropData((data) => {
    previewsImage.value = data
  })
}

const sourceImage = ref()
const selectFile = (file) => {
  console.log(props.cutWidth, props.scale)

  file = file.file
  let img = new FileReader()
  img.readAsDataURL(file)
  img.onload = ({ target }) => {
    sourceImage.value = target.result
  }
}

const show = () => {
  dialogConfig.value.show = true
  sourceImage.value = ''
  nextTick(() => {
    previewsImage.value = ''
  })
}

defineExpose({
  show,
})

const cutImageCallback = inject('cutImageCallback')
//裁剪
const cutImage = () => {
  //截图的高宽
  const cropW = Math.round(cropperRef.value.cropW)
  const cropH = Math.round(cropperRef.value.cropH)
  if (cropW == 0 || cropH == 0) {
    proxy.Message.warning(`请选择图片`)
    return
  }
  if (
    cropW < props.cutWidth ||
    cropH < Math.round(props.cutWidth * props.scale)
  ) {
    proxy.Message.warning(
      `图片尺寸至少满足(${props.cutWidth}*${Math.round(
        props.cutWidth * props.scale
      )}`
    )
    return
  }
  cropperRef.value.getCropBlob((blob) => {
    const file = new File(
      [blob],
      'temp.' + blob.type.substring(blob.type.indexOf('/') + 1),
      { type: blob.type }
    )
    dialogConfig.value.show = false

    cutImageCallback({
      coverImage: file,
    })
  })
}
</script>

<style lang="scss" scoped>
.cut-image-panel {
  display: flex;
  .cropper {
    flex: 1;
    height: 500px;
  }
  .preview-panel {
    width: 200px;
    margin-left: 20px;
    text-align: center;
    .preview-image {
      width: 100%;
      height: 200px;
      background: #f6f6f6;
      display: flex;
      align-items: center;
    }
    img {
      width: 100%;
    }
  }
  .select-btn {
    margin-top: 20px;
  }
}
.info {
  color: #6b6b6b;
}
</style>
