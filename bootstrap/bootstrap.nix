{ pkgs, callPackage, lib, full ? false, ... }:

with builtins;
with lib;

let
  env = callPackage ./environment { inherit full; };
  info = readFile "${pkgs.closureInfo { rootPaths = [ env ]; }}/store-paths";

  bootstrap = pkgs.runCommand "bootstrap" {} ''
    mkdir -p $out/nix/store
    for i in "${info}"; do
      cp -r $i $out/nix/store
    done
    cp ${pkgs.prootTermux}/bin/proot-static $out
    chmod -R u+w $out/nix $out/proot-static

    find $out -executable -type f | sed s@^$out/@@ > $out/EXECUTABLES.txt
    find $out -type l | while read -r LINK; do
      LNK=''${LINK#$out/}
      TGT=$(readlink "$LINK")
      echo "$TGT←$LNK" >> $out/SYMLINKS.txt
      rm "$LINK"
    done
  '';
in pkgs.runCommand "bootstrap.zip" {} ''
  mkdir -p $out
  cd ${bootstrap}
  ${pkgs.zip}/bin/zip -q -9 -r $out/bootstrap.zip ./*
  echo ${env} > $out/env.txt
''
