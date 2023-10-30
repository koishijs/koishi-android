<template>
  <div class="item" @click="click">
    <img :src="props.icon" />
    <div class="text">
      <p class="name"><slot></slot></p>
      <p class="description"><slot name="description"></slot></p>
    </div>
    <img v-if="props.type === 'next'" src="../assets/next.svg" />
    <k-switch v-else-if="props.type === 'switch'"
      :modelValue="props.modelValue"
      @update:modelValue="(value: boolean) => emit('update:modelValue', value)"/>
  </div>
</template>

<script lang="ts" setup>
import KSwitch from '../components/Switch.vue'

const props = defineProps({
  icon: String,
  // for switch
  modelValue: Boolean,
  // next/switch
  type: {
    type: String,
    default: 'next',
  },
})

const emit = defineEmits(['update:modelValue', 'click'])

function click() {
  if (props.type === 'next') {
    emit('click')
  } else if (props.type === 'switch') {
    emit('update:modelValue', !props.modelValue)
  }
}
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
