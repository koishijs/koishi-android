<template>
  <!-- TODO: click ripple -->
  <div class="k-button" @click.stop="emit('click')">
    <div class="k-button-inner">
      <slot></slot>
    </div>
  </div>
</template>

<script lang="ts" setup>
const emit = defineEmits(['click'])
</script>

<style scoped>
.k-button {
  height: 40px;
  display: inline-block;
  background: #6750A4;
  border-radius: 100px;
  box-shadow: 0px 0px 24px rgba(85, 70, 163, 0.30);

}

.k-button:hover {
  box-shadow: 0px 1px 3px 1px rgba(0, 0, 0, 0.15);
}

.k-button-inner {
  width: 100%;
  height: 100%;
  border-radius: 100px;
  padding: 10px 24px 10px 24px;
  color: white;
  font-size: 14px;
  font-weight: 500;
  text-transform: uppercase;
  line-height: 20px;
  letter-spacing: 0.10px;
  cursor: default;
}

.k-button-inner:hover {
  background: rgba(255, 255, 255, 0.08);
}
</style>
