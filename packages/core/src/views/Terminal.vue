<template>
  <title-layout>
    <template #text>Terminal</template>
    <template #left><img src="../assets/back.svg" @click="$router.go(-1)"/></template>
    <div class="wrapper">
      <div class="terminal" ref="terminal"></div>
    </div>
  </title-layout>
</template>

<script lang="ts" setup>
import { onMounted, ref } from 'vue'
import TitleLayout from '@/components/TitleLayout.vue'
import { Terminal } from 'xterm'
import { FitAddon } from 'xterm-addon-fit'

const terminal = ref<HTMLElement>()
onMounted(() => {
  const term = new Terminal()
  const fit = new FitAddon()
  term.loadAddon(fit)
  term.open(terminal.value!)
  fit.fit()
})
</script>

<style scoped>
.wrapper {
  width: 100%;
  height: 100%;
  background: black;
  border-radius: 20px 20px 0 0;
  overflow: hidden;
  padding: 31px;
  box-shadow: 0px 0px 24px rgba(85, 70, 163, 0.30);
}

.terminal {
  width: 100%;
  height: 100%;
}
</style>
