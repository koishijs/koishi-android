<template>
  <div class="text-field">
    <!-- TODO: animation -->
    <div class="label">DNS</div>
    <input type="text" class="input"
      :value="props.modelValue"
      @input="emit('update:modelValue', ($event.target as HTMLInputElement).value)" />
  </div>
</template>

<script lang="ts" setup>
const props = defineProps({
  modelValue: String,
})

const emit = defineEmits(['update:modelValue'])
</script>

<style scoped>
.text-field {
  width: 100%;
  height: 56px;
  background: rgba(103, 80, 164, 0.15);
  border-radius: 4px 4px 0 0;
  border-bottom: 1px #49454F solid;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 8px 16px 8px 16px;
}

.label {
  color: #49454F;
  font-size: 12px;
  font-weight: 400;
  line-height: 16px;
}

.input {
  border: none;
  outline: none;
  resize: none;
  background-color: transparent;
  color: #1D1B20;
  font-size: 16px;
  font-weight: 400;
  line-height: 24px;
  letter-spacing: 0.50px;
}
</style>
