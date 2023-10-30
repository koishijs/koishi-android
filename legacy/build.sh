#!/usr/bin/env bash
set -e

checkStatus() {
    if [ ! $1 -eq 0 ]; then
        echo $2
        exit 1
    fi
}

buildAssets() {
    mkdir -p app/src/main/assets/bootstrap

    pushd bootstrap
    nix run .#${COPY:-copy}
    popd

    pushd app/src/main/assets/bootstrap
    BP_RELEASE=$(curl "https://api.github.com/repos/koishijs/boilerplate/releases/latest")
    BP_TAG=$(echo "$BP_RELEASE" | jq -r '.tag_name')
    BP="https://github.com/koishijs/boilerplate/releases/download/$BP_TAG/boilerplate-$BP_TAG-linux-arm64-node16.zip"
    curl -L -o koishi.zip $BP
    checkStatus $? 'boilerplate 下载失败。'
    curl -O https://repo.yarnpkg.com/3.2.0/packages/yarnpkg-cli/bin/yarn.js
    checkStatus $? 'yarn.js 下载失败。'
    popd
}

if [ ! -d app/src/main/assets/bootstrap ]; then
    buildAssets
fi

./gradlew build --no-daemon
