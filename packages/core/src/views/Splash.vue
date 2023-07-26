<template>
  <div class="splash">
    <div class="icon">
      <img class="eyelash" src="../assets/eyelash.png">
    </div>
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

.icon {
  position: absolute;
  width: 172px;
  height: 172px;
  display: flex;
  justify-content: center;
  align-items: center;
  top: 232px;
}

.icon::before {
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

.icon .eyelash {
  transform: translate(30px, 30px);
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
