<template>
  <title-layout>
    <template #text>WebUI</template>
    <template #left><img src="../assets/back.svg" @click="$router.go(-1)"/></template>
    <iframe :src="src" frameborder="0"></iframe>
  </title-layout>
</template>

<script lang="ts" setup>
import { useRoute } from 'vue-router'
import TitleLayout from '../components/TitleLayout.vue'

const route = useRoute()
const src = route.query.url as string
</script>
