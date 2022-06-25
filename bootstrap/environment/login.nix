{ writeScriptBin, lib }:

with builtins;
with lib;

let

in writeScriptBin "login" ''
    set -e
    . /etc/profile
    exec bash "$@"
''