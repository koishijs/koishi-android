<template>
  <div class="splash" @click="$router.push('/instances')">
    <div class="welcome">Welcome to Koishi</div>
    <div class="starting">Starting koishi......</div>
  </div>
</template>

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
