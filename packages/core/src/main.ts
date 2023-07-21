import 'vfonts/Lato.css'
import 'vfonts/FiraCode.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(router)

app.mount('#app')
