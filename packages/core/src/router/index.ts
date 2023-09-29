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
      path: '/instances',
      name: 'instances',
      component: () => import('../views/Instances.vue')
    },
    {
      path: '/terminal',
      name: 'terminal',
      component: () => import('../views/Terminal.vue')
    },
    {
      path: '/settings',
      name: 'settings',
      component: () => import('../views/Settings.vue')
    },
    {
      path: '/settings/dns',
      name: 'dns',
      component: () => import('../views/DNS.vue')
    },
    {
      path: '/webui',
      name: 'webui',
      component: () => import('../views/WebUI.vue')
    },
  ]
})

export default router
