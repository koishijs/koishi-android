<template>
  <bg>
    <RouterView />
  </bg>
</template>

<script setup lang="ts">
import { RouterView } from 'vue-router'
import Bg from './components/Bg.vue'
</script>

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
