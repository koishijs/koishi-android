<template>
  <div :class="{
    'switch': true,
    'switch-enabled': props.modelValue,
  }" @click.stop="emit('update:modelValue', !props.modelValue)">
    <div :class="{
      'circle': true,
      'circle-enabled': props.modelValue,
    }"></div>
  </div>
</template>

<script lang="ts" setup>
const props = defineProps({
  modelValue: Boolean,
})

const emit = defineEmits(['update:modelValue'])
</script>

<style scoped>
.switch {
  width: 52px;
  height: 32px;
  border-radius: 100px;
  border: 2px #79747E solid;
  background: #E6E0E9;
  display: flex;
  align-items: center;
  position: relative;
  transition: .3s ease;
}

.switch-enabled {
  background: #6750A4;
  border: 0;
}

.circle {
  width: 16px;
  height: 16px;
  background: #79747E;
  border-radius: 100px;
  position: absolute;
  left: 8px;
  transition: .3s ease;
}

.circle-enabled {
  width: 24px;
  height: 24px;
  background: white;
  border-radius: 23px;
  left: 24px;
}
</style>
