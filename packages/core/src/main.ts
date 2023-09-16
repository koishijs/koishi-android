import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { native } from './native/register'
import 'xterm/css/xterm.css'

const app = createApp(App)

app.use(router)
app.provide('native', native)

app.mount('#app')
