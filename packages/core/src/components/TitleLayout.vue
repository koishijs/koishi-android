<template>
  <div class="container">
    <div class="title">
      <div v-if="$slots.left" class="left">
        <slot name="left"></slot>
      </div>
      <div class="text">
        <slot name="text"></slot>
      </div>
      <div v-if="$slots.right" class="right">
        <slot name="right"></slot>
      </div>
    </div>
    <div class="inner">
      <slot></slot>
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
  margin-top: 94px;
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

.container {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.inner {
  width: 100%;
  margin-top: 10px;
  flex-grow: 1;
}
</style>
