<template>
  <bg></bg>
  <Transition>
    <RouterView />
  </Transition>
</template>

<script setup lang="ts">
import { RouterView } from 'vue-router'
import Bg from './components/Bg.vue'
</script>

<style scoped>
.v-enter-active,
.v-leave-active {
  transition: opacity 2s ease;
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
