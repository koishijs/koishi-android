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
      path: '/instance',
      name: 'instance',
      component: () => import('../views/Instance.vue')
    },
    {
      path: '/terminal',
      name: 'terminal',
      component: () => import('../views/Terminal.vue')
    },
    {
      path: '/setting',
      name: 'setting',
      component: () => import('../views/Setting.vue')
    },
  ]
})

export default router
