<template>
  <title-layout>
    <template #text>Settings</template>
    <template #left><img src="../assets/back.svg" @click="$router.go(-1)"/></template>
    <div class="list">
      <setting-item icon="../assets/dns.svg" @click="$router.push('/settings/dns')">
        <template #description>Modify DNS settings</template>
        DNS
      </setting-item>
      <setting-item icon="../assets/export.svg">
        <template #description>Export Koishi backup</template>
        Export
      </setting-item>
      <setting-item icon="../assets/import.svg">
        <template #description>Import Koishi from file</template>
        Import
      </setting-item>
      <setting-item icon="../assets/point.svg" type="switch" v-model="instance">
        <template #description>Use multi-instance mode</template>
        Instance
      </setting-item>
      <setting-item icon="../assets/info.svg">
        <!-- TODO: version -->
        <template #description>Koishi Android 0.1.0</template>
        About
      </setting-item>
    </div>
  </title-layout>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import TitleLayout from '../components/TitleLayout.vue'
import SettingItem from '../components/SettingItem.vue'

const instance = ref(false)
</script>

<style scoped>
.list {
  display: flex;
  flex-direction: column;
  row-gap: 12px;
  padding: 9px;
}
</style>
