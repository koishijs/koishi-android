<template>
  <title-layout>
    <template #text>Settings</template>
    <template #left><img src="../assets/back.svg" @click="$router.go(-1)"/></template>
    <div class="list">
      <setting-item icon="../assets/dns.svg">
        <template #description>Modify DNS setting.</template>
        DNS
      </setting-item>
      <setting-item></setting-item>
      <setting-item></setting-item>
      <setting-item></setting-item>
    </div>
  </title-layout>
</template>

<script lang="ts" setup>
import TitleLayout from '../components/TitleLayout.vue'
import SettingItem from '../components/SettingItem.vue'
</script>

<style scoped>
.list {
  display: flex;
  flex-direction: column;
  row-gap: 12px;
  padding: 9px;
}
</style>
