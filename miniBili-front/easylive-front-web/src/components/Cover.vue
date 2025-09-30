<template>
  <div
    class="image-panel"
    ref="coverRef"
    :style="{
      'border-radius': borderRadius,
      width: width ? width + 'px' : '100%',
      height: width ? width * scale + 'px' : '100%',
    }"
  >
    <el-image
      :lazy="lazy"
      :src="fileSource || fileImage"
      :fit="fit"
      v-if="fileSource || fileImage"
      @click="showViewerHandler"
    >
      <template #placeholder>
        <div class="loading" :style="{ height: loadingHeight + 'px' }">
          <img :src="proxy.Utils.getLocalImage('playing.gif')" />
        </div>
      </template>
      <template #error>
        <img
          :src="proxy.Utils.getLocalImage(img404)"
          class="el-image__inner"
          :style="{ 'object-fit': fit }"
        />
      </template>
    </el-image>
    <div v-else class="no-image">请选择图片</div>
    <el-image-viewer
      :hide-on-click-modal="true"
      @close="
        () => {
          showViewer = false;
        }
      "
      v-if="showViewer"
      :url-list="imageList"
      :teleported="true"
    />
  </div>
</template>

<script setup>
import {
  ref,
  reactive,
  getCurrentInstance,
  nextTick,
  computed,
  onMounted,
} from "vue";
const { proxy } = getCurrentInstance();
import { useRoute, useRouter } from "vue-router";
const route = useRoute();
const router = useRouter();

const props = defineProps({
  source: {
    type: [String, File],
  },
  width: {
    type: Number,
  },
  fit: {
    type: String,
    default: "scale-down",
  },
  preview: {
    type: Boolean,
    default: false,
  },
  defaultImg: {
    type: String,
  },
  img404: {
    type: String,
    default: "404_cover.png",
  },
  borderRadius: {
    type: String,
    default: "5px",
  },
  lazy: {
    //顶部头像使用lazy页面不跳转路由切换不加载图片
    type: Boolean,
    default: true,
  },
  scale: {
    type: Number,
    default: 0.6,
  },
});

const fileImage = ref();
const fileSource = computed(() => {
  if (!props.source && !props.defaultImg) {
    fileImage.value = null;
    return null;
  }
  if (!props.source && props.defaultImg) {
    return proxy.Utils.getLocalImage(props.defaultImg);
  }
  if (props.source instanceof File) {
    let img = new FileReader();
    img.readAsDataURL(props.source);
    img.onload = ({ target }) => {
      fileImage.value = target.result;
    };
    return;
  } else if (typeof props.source === "string") {
    return `${proxy.Api.sourcePath}${props.source}`;
  } else {
    return;
  }
});

const imageList = computed(() => {
  if (!props.preview) {
    return [];
  }
  const sourceImg =
    proxy.Api.sourcePath + props.source.replace(proxy.imageThumbnailSuffix, "");
  return [sourceImg];
});

const showViewer = ref(false);
const showViewerHandler = () => {
  if (!props.preview) {
    return;
  }
  showViewer.value = true;
};

const coverRef = ref();
const loadingHeight = ref();
onMounted(() => {
  loadingHeight.value = coverRef.value.clientWidth * props.scale;
});
</script>

<style lang="scss" scoped>
.image-panel {
  position: relative;
  overflow: hidden;
  cursor: pointer;
  background: #f8f8f8;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  width: 100%;
  :deep(.el-image) {
    width: 100%;
    height: 100%;
  }
  :deep(.is-loading) {
    display: none;
  }
  :deep(.el-image__wrapper) {
    position: relative;
    vertical-align: top;
    width: 100%;
    height: 100%;
    display: flex;
  }
  .icon-image-error {
    margin: 0px auto;
    font-size: 20px;
    color: #838383;
    height: 100%;
  }
  .loading {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    img {
      width: 20px;
    }
  }
  .no-image {
    text-align: center;
    color: #9f9f9f;
  }
}
</style>
