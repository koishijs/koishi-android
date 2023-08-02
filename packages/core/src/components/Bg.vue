<template>
  <div class="bg">
    <div :class="splash ? 'left-splash' : 'left'">
      <img v-if="splash" class="eyelash" src="../assets/eyelash.png" />
    </div>
    <div v-if="!splash" class="right"></div>
  </div>
</template>

<script lang="ts" setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
const route = useRoute()
const splash = computed(() => route.name === 'splash')
</script>

<style scoped>
.bg {
  width: 100%;
  height: 100%;
  position: absolute;
  z-index: -1;
  display: flex;
  flex-direction: column;
  align-items: center;
  overflow: hidden;
}

.left-splash {
  position: absolute;
  width: 172px;
  height: 172px;
  display: flex;
  justify-content: center;
  align-items: center;
  top: 232px;
}

.left-splash::before {
  position: absolute;
  content: '';
  display: block;
  z-index: -1;
  width: 100%;
  height: 100%;
  border-radius: 50%;
  filter: blur(40px);
  background-color: #5546A3BF;
}

.left-splash .eyelash {
  transform: translate(30px, 30px);
}

.left {
  position: absolute;
  width: 358px;
  height: 358px;
  left: -92px;
  top: -44px;
  border-radius: 50%;
  filter: blur(80px);
  background-color: #5546A34D;
}

.right {
  position: absolute;
  width: 358px;
  height: 358px;
  margin: auto;
  right: -92px;
  bottom: -44px;
  border-radius: 50%;
  filter: blur(80px);
  background-color: #E255554D;
}
</style>
