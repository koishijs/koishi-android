<template>
  <title-layout>
    <template #text>DNS</template>
    <template #left><img src="../assets/back.svg" @click="$router.go(-1)"/></template>
    <div class="wrapper">
      <text-field></text-field>
    </div>
    <k-button />
  </title-layout>
</template>

<script lang="ts" setup>
import TitleLayout from '../components/TitleLayout.vue'
import TextField from '../components/TextField.vue'
import KButton from '../components/Button.vue'
</script>

<style scoped>
.wrapper {
  padding-left: 30px;
  padding-right: 30px;
}
</style>
