{ writeScriptBin, lib }:

with builtins;
with lib;

let

in writeScriptBin "login" ''
    set -e
    . /etc/profile
    export HOME=/home
    cd $HOME
    exec sh "$@"
''