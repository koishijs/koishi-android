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
    curl -L -o yarn.js https://repo.yarnpkg.com/4.12.0/packages/yarnpkg-cli/bin/yarn.js
    checkStatus $? 'yarn.js 下载失败。'
    popd
}

if [ ! -d app/src/main/assets/bootstrap ]; then
    buildAssets
fi

nix develop .. --command bash -c 'export JAVA_HOME=$(dirname $(dirname $(which java))) && ./gradlew build --no-daemon'
