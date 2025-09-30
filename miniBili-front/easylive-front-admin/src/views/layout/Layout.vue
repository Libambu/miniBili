<template>
  <div class="layout-body">
    <el-container>
      <el-aside class="aside">
        <div class="logo">
          <span>Easylive管理后台</span>
        </div>
        <div class="menu-panel">
          <el-menu
            :default-openeds="defaultOpeneds"
            :collapse-transition="false"
            class="el-menu-vertical-demo"
            background-color="#3d3c4a"
            text-color="#fff"
            active-text-color="#fff"
            router
            :defaultActive="route.path"
          >
            <template v-for="item in menuList">
              <el-sub-menu :index="item.path" v-if="item.children">
                <template #title>
                  <i :class="['iconfont', item.icon]"></i>
                  <span class="menu-name">{{ item.menuName }}</span>
                </template>
                <el-menu-item
                  :index="subItem.path"
                  v-for="subItem in item.children"
                >
                  <span class="menu-name">{{
                    subItem.menuName
                  }}</span></el-menu-item
                >
              </el-sub-menu>
              <el-menu-item :index="item.path" v-else>
                <i :class="['iconfont', item.icon]"></i>
                <template #title>
                  <span class="menu-name">{{ item.menuName }}</span>
                </template>
              </el-menu-item>
            </template>
          </el-menu>
        </div>
      </el-aside>
      <el-container>
        <el-header class="header"> </el-header>
        <el-main class="main-content">
          <div class="content-body">
            <router-view />
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { useRoute, useRouter } from "vue-router";
import { ref, watch } from "vue";
const router = useRouter();
const route = useRoute();

//系统菜单
const menuList = [
  {
    menuName: "首页",
    path: "/home",
    icon: "icon-home",
  },
  {
    menuName: "内容管理",
    path: "/content",
    icon: "icon-content",
    children: [
      {
        menuName: "分类管理",
        path: "/content/category",
      },
      {
        menuName: "稿件管理",
        path: "/content/video",
      },
    ],
  },
  {
    menuName: "互动管理",
    path: "/interact",
    icon: "icon-hudong",
    children: [
      {
        menuName: "评论管理",
        path: "/interact/comment",
      },
      {
        menuName: "弹幕管理",
        path: "/interact/danmu",
      },
    ],
  },
  {
    menuName: "用户管理",
    path: "/user/userList",
    icon: "icon-fans",
  },
  {
    menuName: "系统设置",
    path: "/setting",
    icon: "icon-setting",
  },
];

//默认选中
const defaultActive = ref();
//aside宽度
//默认展开的菜单
const defaultOpeneds = ref([]);
//菜单转换为map
const init = () => {
  menuList.forEach((item) => {
    defaultOpeneds.value.push(item.path);
  });
};

init();
</script>

<style lang="scss" scoped>
.layout-body {
  .aside {
    background: #3d3c4a;
    width: 250px;
    .logo {
      display: flex;
      height: 50px;
      color: #fff;
      background: #191828;
      align-items: center;
      font-size: 18px;
      padding-left: 5px;
    }
    .menu-panel {
      height: calc(100vh - 50px);
      .menu-name {
        padding-left: 10px;
      }
      //去除边框
      .el-menu {
        border-right: none;
      }
      //每个菜单的颜色
      .el-menu-item {
        background: #353544;
      }
      //选中的颜色
      .el-menu-item.is-active {
        color: #fff;
        background: var(--el-color-primary);
      }
      //鼠标移上去的颜色
      .el-menu-item:hover {
        color: #d8d8ee;
      }
    }
  }
  .header {
    background: #fff;
    border-bottom: 1px solid #ddd;
    height: 50px;
    line-height: 50px;
    padding: 0px 10px !important;
    display: flex;
    align-items: center;
    .op-menu {
      font-size: 20px;
      cursor: pointer;
      color: #3a3a40;
    }
  }
  .main-content {
    padding: 0px;
    .tag-content {
      .el-tabs--border-card {
        border: none;
      }
      .el-tabs__content {
        display: none;
      }
    }
    .content-body {
      overflow: hidden;
      padding: 10px 10px 5px 10px;
    }
  }
}
</style>