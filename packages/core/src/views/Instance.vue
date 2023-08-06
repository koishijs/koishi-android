<template>
  <title-layout>
    <template #text>Koishi</template>
    <div class="information">
      <p><b>Information</b></p>
      <p>Instance 2/3</p>
      <p>Uptime 1 days, 12 hours</p>
      <p>System Android 11</p>
    </div>
    <div class="instances">
      <card>123</card>
    </div>
  </title-layout>
</template>

<script lang="ts" setup>
import TitleLayout from '../components/TitleLayout.vue'
import Card from '../components/Card.vue'
</script>

<style scoped>
.information {
  padding-left: 30px;
  padding-right: 30px;
  line-height: 25px;
}
</style>