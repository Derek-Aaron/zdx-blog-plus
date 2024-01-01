import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/Login.vue'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'login',
      meta:{
        title:'登录'
      },
      component: Login
    },
    {
      path: '/register',
      name: 'register',
      meta:{
        title:'注册页面'
      },
      component: () => import('@/views/Register.vue')
    },
    {
      path: '/callback',
      name: 'callback',
      meta:{
        title:'回调页面'
      },
      component: () => import('@/views/Callback.vue')
    },
    {
      path: '/index',
      name: 'index',
      meta:{
        title: '首页',
        permission: false
      },
      component: () => import('@/layout/index.vue'),
      children:[
        {
          path:'/home',
          name:'首页',
          meta:{
            title: '首页',
            permission: false
          },
          component: () => import('@/views/index.vue')
        },
        {
          path:'/profile',
          name:'profile',
          meta:{
            title: '个人中心',
            permission: false
          },
          component: () => import('@/views/us/user/profile/index.vue')
        },
        {
          path:'/user',
          name:'user',
          meta:{
            title: '用户管理',
            permission:'zdx:user'
          },
          component: () => import('@/views/us/user/index.vue')
        },
        {
          path:'/role',
          name:'role',
          meta:{
            title: '角色管理',
            permission:'zdx:role'
          },
          component: () => import('@/views/us/role/index.vue')
        },
        {
          path:'/menu',
          name:'menu',
          meta:{
            title: '菜单管理',
            permission:'zdx:menu'
          },
          component: () => import('@/views/tk/menu/index.vue')
        },
        {
          path:'/auth',
          name:'auth',
          meta:{
            title: '登录管理',
            permission:'zdx:auth'
          },
          component: () => import('@/views/us/auth/index.vue')
        },
        {
          path:'/dict',
          name:'dict',
          meta:{
            title: '字典管理',
            permission:'zdx:dict'
          },
          component: () => import('@/views/tk/dict/index.vue')
        },
        {
          path:'/config',
          name:'config',
          meta:{
            title: '配置管理',
            permission:'zdx:config'
          },
          component: () => import('@/views/tk/config/index.vue')
        },
        {
          path:'/online',
          name:'online',
          meta:{
            title: '在线用户',
            permission:'zdx:online'
          },
          component: () => import('@/views/us/online/index.vue')
        },
        {
          path:'/file',
          name:'file',
          meta:{
            title: '文件管理',
            permission:'zdx:file'
          },
          component: () => import('@/views/tk/file/index.vue')
        },
        {
          path: '/schedule',
          name:'schedule',
          meta: {
            title:'定时任务',
            permission:'zdx:schedule'
          },
          component: () => import('@/views/tk/schedule/index.vue')
        },
        {
          path: '/schedule-log/:scheduleId',
          name:'schedule-log',
          meta: {
            title:'定时任务日志',
            permission:'zdx:schedule-log'
          },
          component: () => import('@/views/tk/schedule/log.vue')
        },
        {
          path: '/druid',
          name:'druid',
          meta: {
            title:'数据监控',
            permission:'zdx:druid'
          },
          component: () => import('@/views/system/druid/index.vue')
        },
        {
          path: '/server',
          name:'server',
          meta: {
            title:'服务监控',
            permission:'zdx:server'
          },
          component: () => import('@/views/system/server/index.vue')
        },
        {
          path: '/cache',
          name:'cache',
          meta: {
            title:'缓存监控',
            permission:'zdx:cache'
          },
          component: () => import('@/views/system/cache/index.vue')
        },
        {
          path: '/log-login',
          name:'log-login',
          meta: {
            title:'登录日志',
            params:{
              event: 'LOGIN',
            },
            permission:'zdx:log:login'
          },
          component: () => import('@/views/us/log/index.vue')
        },
        {
          path: '/log-operate',
          name:'log-operate',
          meta: {
            title:'操作日志',
            params:{
              event: '',
            },
            permission:'zdx:log:operate'
          },
          component: () => import('@/views/us/log/index.vue')
        },
        {
          path: '/add-article/:articleId*',
          name:'add-article',
          meta: {
            title:'编写文章',
            permission:'zdx:add:article'
          },
          component: () => import('@/views/zdx/article/addArticle.vue')
        },
        {
          path: '/article',
          name:'article',
          meta: {
            title:'文章列表',
            permission:'zdx:article:list'
          },
          component: () => import('@/views/zdx/article/index.vue')
        },
        {
          path: '/category',
          name:'category',
          meta: {
            title:'分类管理',
            permission:'zdx:category'
          },
          component: () => import('@/views/zdx/category/index.vue')
        },
        {
          path: '/tag',
          name:'tag',
          meta: {
            title:'标签管理',
            permission:'zdx:tag'
          },
          component: () => import('@/views/zdx/tag/index.vue')
        },
        {
          path: '/comment',
          name:'comment',
          meta: {
            title:'评论管理',
            permission:'zdx:comment'
          },
          component: () => import('@/views/zdx/comment/index.vue')
        },
        {
          path: '/friend',
          name:'friend',
          meta: {
            title:'友链管理',
            permission:'zdx:friend'
          },
          component: () => import('@/views/zdx/friend/index.vue')
        },
        {
          path: '/talk',
          name:'talk',
          meta: {
            title:'说说管理',
            permission:'zdx:talk'
          },
          component: () => import('@/views/zdx/talk/index.vue')
        },
        {
          path: '/album',
          name:'album',
          meta: {
            title:'相册管理',
            permission:'zdx:album'
          },
          component: () => import('@/views/zdx/album/index.vue')
        },
        {
          path: '/photo/:albumId',
          name:'photo',
          meta: {
            title:'图片管理',
            permission:'zdx:photo'
          },
          component: () => import('@/views/zdx/photo/index.vue')
        },
        {
          path: '/message',
          name:'message',
          meta: {
            title:'留言管理',
            permission:'zdx:message'
          },
          component: () => import('@/views/zdx/message/index.vue')
        },
        {
          path: '/site',
          name:'site',
          meta: {
            title:'配置管理',
            permission:'zdx:site'
          },
          component: () => import('@/views/zdx/site/index.vue')
        },
        {
          path: '/navigation',
          name:'navigation',
          meta: {
            title:'导航管理',
            permission:'zdx:navigation'
          },
          component: () => import('@/views/zdx/navigation/index.vue')
        }
      ]
    }
  ]
})

export default router
