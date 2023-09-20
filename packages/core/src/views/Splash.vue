<template>
  <div class="splash">
    <div class="welcome">Welcome to Koishi</div>
    <div class="starting">Starting koishi......</div>
  </div>
</template>

<script lang="ts" setup>
import { useRouter } from 'vue-router'
import { native } from '../native'
import { sleep } from '../utils'

const router = useRouter()

;(async () => {
  await Promise.all([
    sleep(3000),
    native.starting(),
  ])
  router.push('/instances')
})()
</script>

<style scoped>
.splash {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.welcome {
  position: absolute;
  font-size: 22px;
  font-weight: 600;
  top: 520px;
}

.starting {
  position: absolute;
  font-size: 14px;
  color: #636363;
  margin: auto;
  bottom: 81px;
}
</style>
