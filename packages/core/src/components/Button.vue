<template>
  <!-- TODO: click ripple -->
  <div class="button" @click.stop="emit('click')">
    <div class="inner">
      <slot></slot>
    </div>
  </div>
</template>

<script lang="ts" setup>
const emit = defineEmits(['click'])
</script>

<style scoped>
.button {
  height: 40px;
  display: inline-block;
  background: #6750A4;
  border-radius: 100px;
  box-shadow: 0px 0px 24px rgba(85, 70, 163, 0.30);

}

.button:hover {
  box-shadow: 0px 1px 3px 1px rgba(0, 0, 0, 0.15);
}

.inner {
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

.inner:hover {
  background: rgba(255, 255, 255, 0.08);
}
</style>
