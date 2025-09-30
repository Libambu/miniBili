<template>
  <div :class="['header-bar', 'header-bar-' + theme]">
    <div class="menu">
      <el-popover
        :width="categoryPartCount * (150 + 21) + 24"
        trigger="hover"
        :show-arrow="false"
        :offset="22"
        placement="bottom-start"
      >
        <template #reference>
          <router-link class="iconfont icon-logo menu-item" to="/"
            >首页</router-link
          >
        </template>
        <div class="nav-list">
          <div class="nav-part" v-for="index in categoryPartCount">
            <router-link
              class="nav-item"
              v-for="item in categoryStore.categoryList.slice(
                (index - 1) * partCount,
                (index - 1) * partCount + partCount
              )"
              :to="`/v/${item.categoryCode}`"
            >
              <span class="icon" v-if="item.icon">
                <img :src="`${proxy.Api.sourcePath}${item.icon}`" />
              </span>
              <span class="category-name">{{ item.categoryName }}</span>
            </router-link>
          </div>
        </div>
      </el-popover>
    </div>
    <div class="search-body">
      <div class="search-panel" @click.stop v-if="route.path != '/search'">
        <div class="search-panel-inner">
          <div :class="['input-panel', showHistory ? 'focus-input' : '']">
            <input
              @focus="onSearchFocus"
              v-model="keyword"
              ref="searchInputRef"
              :placeholder="placeholder"
              @keyup.enter="search"
            />
            <div class="iconfont icon-search" @click="search"></div>
          </div>
          <div class="history-panel" v-if="showHistory">
            <div class="search-title">
              <div class="title">搜索历史</div>
              <div class="btn-clean" @click="searchHistoryStore.cleanHistory()">
                清空
              </div>
            </div>
            <div class="search-tag-list">
              <el-tag
                v-for="tag in searchHistoryStore.searchHistory"
                :key="tag.name"
                closable
                type="info"
                class="search-tag"
                @click="searchKeyword(tag)"
                @close="searchHistoryStore.delHistory(tag)"
                >{{ tag }}</el-tag
              >
            </div>
            <div class="hot-search-title">热搜</div>
            <div class="hot-search-list">
              <div
                class="search-item"
                v-for="(item, index) in hotSearchList"
                @click="searchKeyword(item)"
              >
                {{ index + 1 }} {{ item }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="user-panel">
      <div class="user-avatar">
        <template v-if="Object.keys(loginStore.userInfo).length > 0">
          <Avatar
            class="avatar"
            :avatar="loginStore.userInfo.avatar"
            :userId="loginStore.userInfo.userId"
            :width="35"
            :lazy="false"
            @mouseover="getCountInfo"
          >
          </Avatar>
          <div class="user-info-panel">
            <div class="nick-name">{{ loginStore.userInfo.nickName }}</div>
            <div class="count-info">
              <div class="count-info-item">
                <div class="count-value">
                  {{ userCountInfo.focusCount }}
                </div>
                <div class="count-title">关注</div>
              </div>
              <div class="count-info-item">
                <div class="count-value">
                  {{ userCountInfo.fansCount }}
                </div>
                <div class="count-title">粉丝</div>
              </div>
              <div class="count-info-item">
                <div class="count-value">
                  {{ userCountInfo.currentCoinCount }}
                </div>
                <div class="count-title">硬币</div>
              </div>
            </div>
            <router-link
              :to="`/user/${loginStore.userInfo.userId}`"
              class="item iconfont icon-user"
            >
              <span class="item-name">个人中心</span>
              <span class="iconfont icon-left"></span>
            </router-link>
            <router-link to="/ucenter/video" class="item iconfont icon-play">
              <span class="item-name">投稿管理</span>
              <span class="iconfont icon-left"></span>
            </router-link>
            <div class="logout item iconfont icon-logout" @click="logout">
              退出登录
            </div>
          </div>
        </template>
        <Avatar
          v-if="Object.keys(loginStore.userInfo).length == 0"
          @click="loginHandler"
          :width="35"
          :lazy="false"
        ></Avatar>
      </div>
      <div class="user-panel-item" @click="navJump('/message')">
        <el-badge
          :value="loginStore.messageNoReadCount"
          :hidden="loginStore.messageNoReadCount == 0"
        >
          <div class="iconfont icon-message"></div>
        </el-badge>
        <div>消息</div>
      </div>

      <div
        class="user-panel-item"
        @click="navJump(`/user/${loginStore.userInfo.userId}/collection`)"
      >
        <div class="iconfont icon-collection"></div>
        <div>收藏</div>
      </div>
      <div class="user-panel-item" @click="navJump('/history')">
        <div class="iconfont icon-history"></div>
        <div>历史</div>
      </div>
      <div class="user-panel-item" @click="navJump('/ucenter/home')">
        <div class="iconfont icon-light"></div>
        <div>创作中心</div>
      </div>
      <div class="btn-upload">
        <el-button
          type="primary"
          @click="navJump('/ucenter/postVideo')"
          size="large"
        >
          <span class="iconfont icon-upload"></span>
          <span>投稿</span>
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { mitter } from "@/eventbus/eventBus.js";
import {
  ref,
  reactive,
  getCurrentInstance,
  nextTick,
  onMounted,
  onUnmounted,
  inject,
  computed,
  watch,
} from "vue";

const props = defineProps({
  theme: {
    type: String,
    default: "light", //"light" 白色  dark://黑色
  },
  hotSearchList: {
    type: Array,
    default: [],
  },
});

const { proxy } = getCurrentInstance();
import { useRoute, useRouter } from "vue-router";
const route = useRoute();
const router = useRouter();
import { useLoginStore } from "@/stores/loginStore.js";
const loginStore = useLoginStore();

import { useCategoryStore } from "@/stores/categoryStore.js";
const categoryStore = useCategoryStore();

import { useSearchHistoryStore } from "@/stores/searchHisotryStore.js";
const searchHistoryStore = useSearchHistoryStore();

const userCountInfo = ref({});
const getCountInfo = async () => {
  let result = await proxy.Request({
    url: proxy.Api.getUserCountInfo,
  });
  if (!result) {
    return;
  }
  userCountInfo.value = result.data;
};

//分类列表
const partCount = 5;
const categoryPartCount = computed(() => {
  const count = Math.ceil(categoryStore.categoryList.length / partCount);
  return count;
});

const loginHandler = () => {
  if (Object.keys(loginStore.userInfo).length == 0) {
    loginStore.setLogin(true);
    return;
  }
};

const placeholder = ref("程序员老罗");
const keyword = ref();

//历史搜索
const searchKeyword = (keyword) => {
  router.push({
    path: "/search",
    query: {
      keyword,
    },
  });
};

const search = () => {
  if (!keyword.value) {
    keyword.value = placeholder.value;
  }
  router.push({
    path: "/search",
    query: {
      keyword: keyword.value,
    },
  });
};

const showHistory = ref(false);
const onSearchFocus = () => {
  showHistory.value = true;
};

const searchInputRef = ref();
onMounted(() => {
  document.addEventListener("click", () => {
    showHistory.value = false;
  });
  mitter.on("windowScroll", () => {
    if (showHistory.value) {
      //滚动的时候手动blur否则光标没移除，再次点击下拉搜索不弹出
      searchInputRef.value.blur();
      showHistory.value = false;
    }
  });
});

onUnmounted(() => {
  mitter.off("windowScroll");
});

const navJump = (url) => {
  if (Object.keys(loginStore.userInfo).length == 0) {
    loginStore.setLogin(true);
    return;
  }
  window.open(url, "_blank");
};

//退出
const logout = () => {
  proxy.Confirm({
    message: "确定要退出?",
    okfun: async () => {
      let result = await proxy.Request({
        url: proxy.Api.logout,
      });
      if (!result) {
        return;
      }
      loginStore.saveUserInfo({});
    },
  });
};
</script>

<style lang="scss" scoped>
.header-bar {
  width: 100%;
  height: 64px;
  padding: 0px 25px;
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  .menu {
    display: flex;
    align-items: center;
    a {
      text-decoration: none;
    }
    .menu-item {
      padding-right: 20px;
      font-size: 16px;
    }
    .icon-logo {
      font-size: 16px;
      &::before {
        float: left;
        margin-top: -3px;
        font-size: 25px;
        margin-right: 5px;
      }
    }
  }
  .search-body {
    color: #61666d;
    .search-panel {
      margin: 0px auto;
      position: relative;
      max-width: 80%;
      .search-panel-inner {
        width: 100%;
        position: absolute;
        top: 10px;
        left: 0px;
        border: 1px solid #e3e5e7;
        border-radius: 8px;
        overflow: hidden;
        z-index: 1001;
        .input-panel {
          display: flex;
          align-items: center;
          background: #f1f2f3;
          input {
            width: 100%;
            border: none;
            background: #f1f2f3;
            border-radius: 5px;
            padding: 8px 10px;
            margin: 3px 10px 3px 10px;
            &:focus {
              outline: none;
            }
          }
          .iconfont {
            font-size: 20px;
            margin-right: 10px;
            color: #2f3238;
            width: 35px;
            height: 30px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 5px;
            cursor: pointer;
            &:hover {
              background: #ddd;
            }
          }
        }
        .focus-input {
          background: #fff;
        }
        .history-panel {
          background: #fff;
          padding: 10px;
          text-align: left;
          .search-title {
            display: flex;
            justify-content: space-between;
            .title {
              font-size: 16px;
            }
            .btn-clean {
              cursor: pointer;
              font-size: 13px;
              color: #9499a0;
              &:hover {
                color: #40c5f1;
              }
            }
          }
          .search-tag-list {
            margin-top: 10px;
            .el-tag {
              cursor: pointer;
              margin-right: 5px;
              margin-top: 5px;
              :deep(.el-tag__content) {
                text-overflow: ellipsis;
                white-space: nowrap;
                max-width: 200px;
                overflow: hidden;
              }
            }
          }
          .hot-search-title {
            margin-top: 10px;
            font-size: 16px;
          }
          .hot-search-list {
            margin-top: 5px;
            display: flex;
            justify-content: space-between;
            flex-wrap: wrap;
            .search-item {
              width: 50%;
              padding: 8px 5px;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
              &:hover {
                background: #d6d9dd;
                cursor: pointer;
              }
            }
          }
        }
      }
    }
  }

  .user-panel {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    .user-avatar {
      position: relative;
      margin-right: 13px;
      overflow: hidden;
      width: 35px;
      height: 35px;
      .avatar {
        transition: transform 0.3s;
        position: absolute;
        z-index: 10001;
        left: 0px;
        top: 0px;
      }
      .user-info-panel {
        padding: 10px 20px 10px;
        background: #fff;
        width: 300px;
        position: absolute;
        top: 60px;
        left: -150px;
        border-radius: 5px;
        box-shadow: 0px 0px 12px rgba(0, 0, 0, 0.12);
        z-index: 10000;
        opacity: 0;
        transition: opacity 0.3s;

        .nick-name {
          font-size: 16px;
          text-align: center;
          line-height: 40px;
          color: var(--text3);
        }
        .count-info {
          margin: 10px 0px;
          display: flex;
          justify-content: space-around;
          .count-info-item {
            text-align: center;
            .count-title {
              color: var(--text3);
              margin-top: 5px;
            }
            .count-value {
              text-align: center;
              color: var(--text);
            }
          }
        }
        .item {
          font-size: 14px;
          display: block;
          text-align: left;
          line-height: 40px;
          color: var(--text3);
          padding: 0px 20px;
          text-decoration: none;
          display: flex;
          justify-content: space-between;
          .item-name {
            flex: 1;
          }
          &::before {
            margin-right: 15px;
          }
          &:hover {
            background: #e8e8e8;
            border-radius: 5px;
          }
        }
        .logout {
          display: block;
          margin-top: 10px;
          border-top: 1px solid #ddd;
          cursor: pointer;
        }
      }
      &:hover {
        overflow: visible;
        .avatar {
          transform: scale(2) translateY(10px) translateX(-10px);
        }
        .user-info-panel {
          opacity: 1;
        }
      }
    }

    .user-panel-item {
      text-align: center;
      cursor: pointer;
      padding: 0px 13px;
      .iconfont {
        text-align: center;
        font-size: 20px;
        font-weight: normal;
      }
    }
    .btn-upload {
      margin-left: 10px;
      .el-button {
        background: #fb7299;
        border-color: #fb7299;
        border-radius: 8px;
        padding: 0px 20px;
        .iconfont {
          &::before {
            margin-right: 5px;
          }
        }
      }
    }
  }
}

.header-bar-light {
  color: #fff;
  .menu-item {
    color: #fff;
  }
}

.header-bar-dark {
  color: #61666d;
  .menu-item {
    color: #61666d;
  }
}

.nav-list {
  display: flex;
  .nav-part {
    &:last-child {
      border-right: none;
    }
    padding: 0px 10px;
    border-right: 1px solid #ddd;
    .nav-item {
      display: flex;
      padding: 0px 10px;
      height: 35px;
      border-radius: 3px;
      cursor: pointer;
      align-items: center;
      width: 150px;
      text-decoration: none;
      color: #2f3238;
      &:hover {
        background: #ddd;
      }
      .icon {
        width: 25px;
        height: 25px;
        overflow: hidden;
        margin-right: 5px;
        img {
          width: 100%;
        }
      }
      .category-name {
        flex: 1;
      }
    }
  }
}
</style>
