<template>
  <div class="category-video-body">
    <div :class="['category-list', categoryFxied ? 'category-fxied' : '']" id="category-list">
      <div class="category-title">
        {{ categoryStore.cureentPCategory.categoryName }}
      </div>
      <div :class="['category-item', !route.params.categoryCode ? 'active' : '']" @click="jump()">
        首页
      </div>
      <div :class="[
          'category-item',
          route.params.categoryCode == item.categoryCode ? 'active' : '',
        ]" v-for="item in categoryStore.cureentPCategory.children" @click="jump(item)">
        {{ item.categoryName }}
      </div>
    </div>
    <VideoList></VideoList>
  </div>
</template>

<script setup>
import { mitter } from '@/eventbus/eventBus.js'
import VideoList from '@/views/videoList/VideoList.vue'
import { useNavAction } from '@/stores/navActionStore'
const navActionStore = useNavAction()

import {
  ref,
  reactive,
  getCurrentInstance,
  nextTick,
  onMounted,
  onUnmounted,
  provide,
  inject,
  watch,
} from 'vue'
const { proxy } = getCurrentInstance()
import { useRoute, useRouter } from 'vue-router'
const route = useRoute()
const router = useRouter()

import { useCategoryStore } from '@/stores/categoryStore.js'
const categoryStore = useCategoryStore()

const jump = (item = { categoryCode: '' }) => {
  if (!item.categoryCode) {
    router.push({
      name: 'categoryVideo',
    })
    return
  }
  router.push({
    name: 'subCategoryVideo',
    params: {
      categoryCode: item.categoryCode,
    },
  })
}

//分类距离顶部距离
const categoryTopDistance = ref(200)
//分类是否固定
const categoryFxied = ref(false)

//初始化距离顶部距离
let initScrollTop = 0
//是否向下关东
let scrollDown = true
const scrollHandler = (curScrollTop) => {
  if (curScrollTop - initScrollTop > 0) {
    scrollDown = true
  } else {
    scrollDown = false
  }
  initScrollTop = curScrollTop

  if (curScrollTop >= categoryTopDistance.value) {
    categoryFxied.value = true
    //超过分类固定向上滚动就展示顶部导航否则不展示
    if (scrollDown) {
      navActionStore.setFixedHeader(false)
    } else {
      navActionStore.setFixedHeader(true)
    }
  } else {
    categoryFxied.value = false
    //如果低于分类固定高度 顶部导航不展示
    navActionStore.setFixedHeader(false)
  }
}

onMounted(() => {
  //获取分类距离顶部的距离
  categoryTopDistance.value = document
    .querySelector('#category-list')
    .getBoundingClientRect().top
  //初始化store
  navActionStore.setShowHeader(true)
  navActionStore.setFixedHeader(false)
  navActionStore.setFixedCategory(false)
  navActionStore.setShowCategory(true)
  navActionStore.setForceFixedHeader(false)

  mitter.on('windowScroll', (curScrollTop) => {
    scrollHandler(curScrollTop)
  })
})

onUnmounted(() => {
  mitter.off('windowScroll')
})
</script>

<style lang="scss" scoped>
.category-video-body {
  margin-top: 30px;
  .category-list {
    display: flex;
    align-items: center;
    line-height: 30px;
    position: sticky;
    top: 0px;
    height: 60px;
    background: #fff;
    z-index: 2;
    .category-title {
      font-size: 24px;
      margin-right: 40px;
      cursor: pointer;
    }
    .category-item {
      margin-right: 40px;
      font-size: 15px;
      cursor: pointer;
      border-bottom: 3px solid #fff;
      &:hover {
        color: var(--blue);
        border-color: var(--blue);
      }
    }
    .active {
      color: var(--blue);
      border-color: var(--blue);
    }
  }
  .category-fxied {
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.08);
  }
}
</style>
