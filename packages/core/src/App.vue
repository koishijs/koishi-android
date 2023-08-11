<template>
  <bg></bg>
  <router-view v-slot="{ Component }">
    <transition>
      <component :is="Component"/>
    </transition>
  </router-view>
</template>

<script setup lang="ts">
import { RouterView } from 'vue-router'
import Bg from './components/Bg.vue'
</script>

<style scoped>
.v-enter-active,
.v-leave-active {
  transition: opacity 1.3s ease;
}

.v-enter-from,
.v-leave-to {
  opacity: 0;
}
</style>

<style>
* {
  padding: 0;
  margin: 0;
  box-sizing: border-box;
}

html, body, #app {
  width: 100%;
  height: 100%;
}
</style>
