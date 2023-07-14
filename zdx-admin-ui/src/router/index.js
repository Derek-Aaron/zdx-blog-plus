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
      path: '/index',
      name: 'index',
      meta:{
        title: '首页'
      },
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/layout/index.vue'),
      children:[
        {
          path:'/user',
          name:'user',
          meta:{
            title: '用户管理',
          },
          component: () => import('@/views/us/user/index.vue')
        },
        {
          path:'/profile',
          name:'profile',
          meta:{
            title: '个人中心',
          },
          component: () => import('@/views/us/user/profile/index.vue')
        },
        {
          path:'/role',
          name:'role',
          meta:{
            title: '角色管理',
          },
          component: () => import('@/views/us/role/index.vue')
        },
        {
          path:'/menu',
          name:'menu',
          meta:{
            title: '菜单管理',
          },
          component: () => import('@/views/tk/menu/index.vue')
        },
        {
          path:'/auth',
          name:'auth',
          meta:{
            title: '登录管理',
          },
          component: () => import('@/views/us/auth/index.vue')
        },
        {
          path:'/dict',
          name:'dict',
          meta:{
            title: '字典管理',
          },
          component: () => import('@/views/tk/dict/index.vue')
        },
        {
          path:'/config',
          name:'config',
          meta:{
            title: '配置管理',
          },
          component: () => import('@/views/tk/config/index.vue')
        },
        {
          path:'/online',
          name:'online',
          meta:{
            title: '在线用户',
          },
          component: () => import('@/views/us/online/index.vue')
        },
        {
          path:'/file',
          name:'file',
          meta:{
            title: '文件管理',
          },
          component: () => import('@/views/tk/file/index.vue')
        },
        {
          path: '/schedule',
          name:'schedule',
          meta: {
            title:'定时任务',
          },
          component: () => import('@/views/tk/schedule/index.vue')
        },
        {
          path: '/schedule-log/:scheduleId(\\d+)',
          name:'schedule-log',
          meta: {
            title:'定时任务日志',
          },
          component: () => import('@/views/tk/schedule/log.vue')
        },
        {
          path: '/druid',
          name:'druid',
          meta: {
            title:'数据监控',
          },
          component: () => import('@/views/system/druid/index.vue')
        },
        {
          path: '/server',
          name:'server',
          meta: {
            title:'服务监控',
          },
          component: () => import('@/views/system/server/index.vue')
        },
        {
          path: '/cache',
          name:'cache',
          meta: {
            title:'缓存监控',
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
            }
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
            }
          },
          component: () => import('@/views/us/log/index.vue')
        }
      ]
    }
  ]
})

export default router
