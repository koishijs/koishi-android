{ writeScriptBin, lib }:

with builtins;
with lib;

let

in writeScriptBin "login" ''
    set -e

    # /nix is writable
    if [ "$KOISHI_DNS" ]; then
    /bin/cat > /etc/resolv.conf <<EOF
    nameserver $KOISHI_DNS
    EOF
    fi

    for var in $(/bin/env | /bin/cut -d '=' -f 1); do unset $var; done

    export PATH=/bin
    export HOME=/home

    cd $HOME
    exec sh "$@"
''
