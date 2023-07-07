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
        }
      ]
    }
  ]
})

export default router
