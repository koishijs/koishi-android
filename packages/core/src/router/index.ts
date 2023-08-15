import { createRouter, createWebHashHistory } from 'vue-router'
import Splash from '../views/Splash.vue'

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: '/',
      name: 'splash',
      component: Splash,
    },
    {
      path: '/home',
      name: 'home',
      component: () => import('../views/Home.vue')
    },
    {
      path: '/instance',
      name: 'instance',
      component: () => import('../views/Instance.vue')
    },
    {
      path: '/terminal',
      name: 'terminal',
      component: () => import('../views/Terminal.vue')
    },
  ]
})

export default router
