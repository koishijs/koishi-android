{ writeScriptBin, lib }:

with builtins;
with lib;

let

in writeScriptBin "login" ''
    set -e

    # /nix is writable
    if [ -n "$KOISHI_DNS" ]; then
        echo "nameserver $KOISHI_DNS" > /etc/resolv.conf
    fi
    if [ -n "$KOISHI_TIMEZONE" ] && [ -e /etc/zoneinfo ]; then
        /bin/ln -sf /etc/zoneinfo/$KOISHI_TIMEZONE /etc/localtiome
    fi

    for var in $(/bin/env | /bin/cut -d '=' -f 1); do unset $var; done

    export PATH=/bin
    export HOME=/home

    cd $HOME
    exec sh "$@"
''
