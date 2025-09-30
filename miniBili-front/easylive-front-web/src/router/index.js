import { createRouter, createWebHistory } from 'vue-router'
import { useLoginStore } from "@/stores/loginStore"
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: '框架页',
      redirect: "/",
      component: () => import('@/views/layout/Layout.vue'),
      children: [
        {
          path: '/',
          name: 'index',
          component: () => import('@/views/index/Index.vue'),
        },
        {
          path: '/video/:videoId',
          name: 'videoDetail',
          component: () => import('@/views/videoDetail/VideoDetail.vue'),
        },
        {
          path: '/v/:pCategoryCode',
          name: 'categoryVideo',
          component: () => import('@/views/videoList/CategoryVideo.vue'),
        },
        {
          path: '/v/:pCategoryCode/:categoryCode',
          name: 'subCategoryVideo',
          component: () => import('@/views/videoList/CategoryVideo.vue'),
        },
        {
          path: '/history',
          name: 'history',
          component: () => import('@/views/history/History.vue'),
        }, {
          path: '/message',
          name: 'messagehome',
          component: () => import('@/views/message/UserMessage.vue'),
        }, {
          path: '/message/:messageType',
          name: 'message',
          component: () => import('@/views/message/UserMessage.vue'),
        }, {
          path: '/search',
          name: 'search',
          component: () => import('@/views/search/Search.vue'),
        }, {
          path: '/hot',
          name: 'hot',
          component: () => import('@/views/hot/Hot.vue'),
        }
      ]
    },
    {
      path: '/ucenter',
      name: 'ucenter',
      redirect: "/ucenter/home",
      component: () => import('@/views/ucenter/UcLayout.vue'),
      children: [{
        path: '/ucenter/home',
        name: '用户中心首页',
        component: () => import('@/views/ucenter/Home.vue'),
      }, {
        path: '/ucenter/postVideo',
        name: '上传视频',
        component: () => import('@/views/ucenter/postvideo/Post.vue'),
      }, {
        path: '/ucenter/editVideo',
        name: '编辑视频',
        component: () => import('@/views/ucenter/postvideo/Post.vue'),
      }, {
        path: '/ucenter/video',
        name: '视频列表',
        component: () => import('@/views/ucenter/VideoList.vue'),
      }, {
        path: '/ucenter/fans',
        name: '粉丝管理',
        component: () => import('@/views/ucenter/FansList.vue'),
      }, {
        path: '/ucenter/comment',
        name: '评论管理',
        component: () => import('@/views/ucenter/CommentList.vue'),
      }, {
        path: '/ucenter/danmu',
        name: '弹幕管理',
        component: () => import('@/views/ucenter/DanmuList.vue'),
      }]
    },
    {
      path: '/user/:userId',
      name: 'userhome',
      redirect: "/user/:userId",
      component: () => import('@/views/userhome/UserHomeLayout.vue'),
      children: [
        {
          path: '/user/:userId',
          name: 'uhome',
          component: () => import('@/views/userhome/Home.vue'),
        }, {
          path: '/user/:userId/video',
          name: 'uhomeMyVideo',
          component: () => import('@/views/userhome/VideoList.vue'),
        }, {
          path: '/user/:userId/series',
          name: 'uhomeSeries',
          component: () => import('@/views/userhome/VideoSeries.vue'),
        }, {
          path: '/user/:userId/series/:seriesId',
          name: 'uhomeSeriesDetail',
          component: () => import('@/views/userhome/VideoSeriesDetail.vue'),
        }, {
          path: '/user/:userId/collection',
          name: 'collection',
          component: () => import('@/views/userhome/Collection.vue'),
        }, {
          path: '/user/:userId/focus',
          name: 'uhomeFocus',
          component: () => import('@/views/userhome/FocusFansList.vue'),
        }, {
          path: '/user/:userId/fans',
          name: 'uhomeFans',
          component: () => import('@/views/userhome/FocusFansList.vue'),
        }]
    },
    {
      path: '/404',
      name: '错误页404',
      component: () => import('@/views/error/404.vue'),
    }
  ]
})
/* router.beforeEach((to, from, next) => {
  const loginStore = useLoginStore();
  if (to.path.indexOf("/ucenter") != -1 && Object.keys(loginStore.userInfo).length == 0) {
    router.push("/")
    loginStore.setLogin(true);
  }
  next();
}) */
export default router