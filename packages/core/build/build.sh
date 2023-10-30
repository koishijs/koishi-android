#!/usr/bin/env bash
set -eu

cat > capacitor.config.json <<EOF
{
  "appId": "cn.anillc.koishi",
  "appName": "Koishi",
  "webDir": "dist",
  "server": {
    "androidScheme": "https"
  }
}
EOF

yarn build-ui
yarn cap sync
cd android
gradle build
