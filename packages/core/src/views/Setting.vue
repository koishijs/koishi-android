<template>
  <title-layout>
    <template #text>Settings</template>
    <template #left><img src="../assets/back.svg" @click="$router.go(-1)"/></template>
  </title-layout>
</template>

<script lang="ts" setup>
import TitleLayout from '../components/TitleLayout.vue'
</script>
