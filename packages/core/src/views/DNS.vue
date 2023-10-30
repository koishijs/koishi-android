<template>
  <title-layout>
    <template #text>DNS</template>
    <template #left><img src="../assets/back.svg" @click="$router.go(-1)"/></template>
    <div class="wrapper">
      <text-field></text-field>
      <p class="description">Split multiple DNS with ' , '</p>
    </div>
    <div class="button">
      <k-button>ok</k-button>
    </div>
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
  display: flex;
  flex-direction: column;
  row-gap: 4px;
  margin-bottom: 16px;
}

.description {
  color: #49454F;
  font-size: 12px;
  font-weight: 400;
  line-height: 16px;
  margin-left: 16px;
}

.button {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: right;
  padding-right: 30px;
}
</style>
