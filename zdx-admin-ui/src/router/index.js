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
        }
      ]
    }
  ]
})

export default router
