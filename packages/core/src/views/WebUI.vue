<template>
  <title-layout>
    <template #text>WebUI</template>
    <template #left><img src="../assets/back.svg" @click="$router.push('/instances')"/></template>
    <iframe :src="src" frameborder="0" class="webui"></iframe>
  </title-layout>
</template>

<script lang="ts" setup>
import { useRoute } from 'vue-router'
import TitleLayout from '../components/TitleLayout.vue'

const route = useRoute()
const src = route.query.url as string
</script>

<style scoped>
.webui {
  width: 100%;
  height: 100%;
  box-shadow: 0px 0px 24px rgba(85, 70, 163, 0.30);
}
</style>
