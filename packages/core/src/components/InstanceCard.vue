<template>
  <div ref="card" @click="focused = true" class="card">
    <div :class="{
      'top': true,
      'top-radius': !focused,
    }">
      <img class="icon" src="../assets/koishi.png" />
      <div class="text">
        <b>{{ props.instance.name }}</b>
        <p><small>{{ props.instance.status }}</small></p>
      </div>
    </div>
    <transition mode="out-in" name="control">
      <div v-if="focused" class="bottom">
        <div @click="toggleInstance">
          <img :src="started ? stopIcon : startIcon" />
          {{ started ? 'Stop' : 'Start' }}
        </div>
        <div @click="openWebUI">
          <img src="../assets/point.svg" />
          WebUI
        </div>
        <div>
          <img src="../assets/point.svg" />
          Terminal
        </div>
        <div>
          <img src="../assets/delete.svg" />
          Delete
        </div>
      </div>
    </transition>
  </div>
</template>

<script lang="ts" setup>
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { onClickOutside } from '@vueuse/core'
import type { Instance } from '../native/register'
import { useNative } from '../native'
import startIcon from '../assets/play.svg'
import stopIcon from '../assets/stop.svg'

const props = defineProps<{
  focused: boolean,
  instance: Instance,
}>()

const card = ref()
const focused  = ref(props.focused)
const started = computed(() => props.instance.status === 'Running')
const router = useRouter()
onClickOutside(card, () => focused.value = false)

const native = useNative()

// TODO: debounce
async function toggleInstance() {
  if (started.value) {
    await native.stopInstance({ name: props.instance.name })
  } else {
    await native.startInstance({ name: props.instance.name })
  }
}

function openWebUI() {
  const link = props.instance.link
  if (!link) {
    native.toast({ value: 'Instance is not started.' })
  } else {
    router.push({ path: '/webui', query: { url: link } })
  }
}
</script>

<style scoped>
.card {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  border-radius: 12px;
  box-shadow: 0px 0px 24px rgba(85, 70, 163, 0.15);
}

.top {
  width: 100%;
  height: 80px;
  background: #FFFFFFB2;
  border-radius: 12px 12px 0 0;
  overflow: hidden;
  padding: 12px;
  display: flex;
  align-items: center;
}

.top .icon {
  width: 40px;
  height: 40px;
  margin: 4px;
}

.top .text {
  margin-left: 12px;
  margin-right: 12px;
  height: 40px;
}

.top .text p {
  line-height: 28px;
}

.top-radius {
  border-radius: 12px;
}

.bottom {
  width: 100%;
  height: 45px;
  background: #FFFFFFB2;
  border-radius: 0 0 12px 12px;
  overflow: hidden;
  padding: 12px;
  border-top: 1px #CAC4D0 solid;
  display: flex;
  flex-grow: 1;
  align-items: center;
  justify-content: space-around;
}

.bottom > div:first-child {
  border-radius: 0 0 0 12px;
}

.bottom > div:last-child {
  border-radius: 0 0 12px 0;
}

.bottom > div {
  display: flex;
  align-items: center;
}

.control-enter-active,
.control-leave-active {
  transition: .3s ease;
}

.control-enter-from,
.control-leave-to {
  height: 0;
  padding-top: 0;
  padding-bottom: 0;
  border-top: 1px #FFFFFF00 solid;
  opacity: 0;
}
</style>
