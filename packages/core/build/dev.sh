#!/usr/bin/env bash
set -eu

ADDR=$(ip --json r s default | jq -r '.[0].prefsrc')
cat > capacitor.config.json <<EOF
{
  "appId": "cn.anillc.koishi",
  "appName": "Koishi",
  "webDir": "dist",
  "server": {
    "androidScheme": "https",
    "url": "http://$ADDR:5173"
  }
}
EOF

yarn cap sync
yarn vite
