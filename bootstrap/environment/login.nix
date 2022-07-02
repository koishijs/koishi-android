{ writeScriptBin, lib }:

with builtins;
with lib;

let

in writeScriptBin "login" ''
    set -e
    . /etc/profile
    export HOME=/home

    # /nix is writable
    if [ "$KOISHI_DNS" ]; then
    cat > /etc/resolv.conf <<EOF
    nameserver $KOISHI_DNS
    EOF
    fi
    cd $HOME
    exec sh "$@"
''