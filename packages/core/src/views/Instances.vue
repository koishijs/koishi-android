<template>
  <title-layout>
    <template #text>Koishi</template>
    <template #right><img src="../assets/settings.png" @click="$router.push('/settings')"/></template>
    <div class="information">
      <p><b>Information</b></p>
      <p>Instance 2/3</p>
      <p>Uptime 1 days, 12 hours</p>
      <p>System Android 11</p>
    </div>
    <div class="instances">
      <instance-card v-for="(instance, i) in instances"
        :key="i" :focused="i === 0" :instance="instance"/>
      <div @click="createInstance" class="create">
          <img src="../assets/add.svg" />
          New Instance
      </div>
    </div>
  </title-layout>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import TitleLayout from '../components/TitleLayout.vue'
import InstanceCard from '../components/InstanceCard.vue'
import { useNative } from '../native'
import type { Instance } from '@/native/register'

const native = useNative()
const instances = ref<Instance[]>()

async function sync() {
  instances.value = (await native.instances()).value
}

sync()
setInterval(sync, 1000)

async function createInstance() {
  const name = (Math.random() + 1).toString(36).substring(7)
  const { value } = await native.createInstance({ name: `instance-${name}` })
  // name existed
  if (!value) await createInstance()
}
</script>

<style scoped>
.information {
  padding-left: 30px;
  padding-right: 30px;
  line-height: 25px;
}

.instances {
  margin-top: 21px;
  padding-left: 30px;
  padding-right: 30px;
  display: flex;
  flex-direction: column;
  row-gap: 20px;
}

.create {
  width: 100%;
  height: 49px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  box-shadow: 0px 0px 24px rgba(85, 70, 163, 0.15);
  background: #FFFFFFB2;
  column-gap: 10px;
}

.create img {
  width: 24px;
  height: 24px;
}
</style>
