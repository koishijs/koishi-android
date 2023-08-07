<template>
  <div class="card">
    <div class="top">
      <img class="icon" src="../assets/koishi.png" />
      <div class="text">
        <b>{{ props.name }}</b>
        <p><small>{{ props.status }}</small></p>
      </div>
    </div>
    <div class="bottom">
      <div>
        <img src="../assets/play.svg" />
        Start
      </div>
      <div>
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
  </div>
</template>

<script lang="ts" setup>
import { defineProps } from 'vue'

const props = defineProps({
  name: String,
  status: String,
})
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
  border-bottom: 1px #CAC4D0 solid;
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

.bottom {
  width: 100%;
  height: 45px;
  background: #FFFFFFB2;
  border-radius: 0 0 12px 12px;
  overflow: hidden;
  padding: 12px;
  display: flex;
  align-items: center;
  justify-content: space-around;
}

.bottom:first-child {
  border-radius: 0 0 0 12px;
}

.bottom:last-child {
  border-radius: 0 0 12px 0;
}

.bottom div {
  display: flex;
  align-items: center;
}
</style>
