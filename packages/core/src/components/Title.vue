<template>
  <div class="title">
    <div v-if="$slots.left" class="left">
      <slot name="left"></slot>
    </div>
    <div class="text">
      <slot></slot>
    </div>
    <div v-if="$slots.right" class="right">
      <slot name="right"></slot>
    </div>
  </div>
</template>

<style scoped>
.title {
  width: 100%;
  height: 80px;
  display: flex;
  align-items: center;
  padding-left: 30px;
  padding-right: 30px;
  position: relative;
}

.text {
  line-height: 20px;
  font-size: 40px;
  font-weight: 800;
  transform: translateY(-3px);
}

.left {
  padding-right: 14px;
}

.right {
  margin-left: auto;
  margin-right: 0;
}
</style>
