<template>
  <div class="bg">
    <div :class="{
      'left-splash': true,
      'left': !splash,
    }">
        <img v-if="splash" class="eyelash" src="../assets/eyelash.png" />
    </div>
    <div :class="{
      'right-splash': true,
      'right': !splash,
    }"></div>
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
  transition: 1s ease;
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
  transition: 1s ease;
}

.left-splash .eyelash {
  transform: translate(30px, 30px);
}

.left {
  width: 358px;
  height: 358px;
  transform: translateX(-92px);
  top: -44px;
}

.left::before {
  filter: blur(80px);
  background-color: #5546A34D;
}

.right-splash {
  position: absolute;
  width: 172px;
  height: 172px;
  border-radius: 50%;
  filter: blur(80px);
  background-color: #E255554D;
  bottom: -172px;
  transition: 1s ease;
}

.right {
  width: 358px;
  height: 358px;
  transform: translateX(92px);
  bottom: -44px;
}
</style>
