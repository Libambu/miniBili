import { createRouter, createWebHistory } from 'vue-router'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/account/Account.vue'),
    },
    {
      path: '/',
      name: 'layout',
      redirect: "/login",
      component: () => import('@/views/layout/Layout.vue'),
      children: [{
        path: '/home',
        name: '首页',
        component: () => import('@/views/home/Home.vue'),
      }, {
        path: '/content/category',
        name: '分类管理',
        component: () => import('@/views/content/CategoryList.vue'),
      }, {
        path: '/content/video',
        name: '稿件管理',
        component: () => import('@/views/content/VideoList.vue'),
      }, {
        path: '/interact/comment',
        name: '评论管理',
        component: () => import('@/views/interact/CommentList.vue'),
      }, {
        path: '/interact/danmu',
        name: '弹幕管理',
        component: () => import('@/views/interact/DanmuList.vue'),
      }, {
        path: '/user/userList',
        name: '用户管理',
        component: () => import('@/views/user/UserList.vue'),
      }, {
        path: '/setting',
        name: '设置',
        component: () => import('@/views/setting/Setting.vue'),
      }]
    },
    { path: '/:pathMatch(.*)', component: import('@/views/error/404.vue') },
  ]
})
export default router