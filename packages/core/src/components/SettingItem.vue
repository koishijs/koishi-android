<template>
  <div class="item">
    <img :src="props.icon" />
    <div class="text">
      <p class="name"><slot></slot></p>
      <p class="description"><slot name="description"></slot></p>
    </div>
    <img v-if="props.type == 'next'" src="../assets/next.svg" />
    <!-- TODO: v-else switch -->
  </div>
</template>

<script lang="ts" setup>
import { defineProps } from 'vue'

const props = defineProps({
  icon: String,
  // next/switch
  type: {
    type: String,
    default: 'next',
  },
})
</script>

<style scoped>
.item {
  width: 100%;
  height: 56px;
  border-radius: 100px;
  display: flex;
  align-items: center;
  padding: 16px 24px 16px 16px;
  column-gap: 12px;
}

.text {
  flex-grow: 1;
}

.name {
  color: #49454F;
  font-size: 14px;
  font-weight: 500;
  line-height: 20px;
  letter-spacing: 0.10px;
}

.description {
  color: #8A868F;
  font-size: 14px;
  font-weight: 300;
  line-height: 20px;
}
</style>
