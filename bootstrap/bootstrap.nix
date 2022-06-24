{ pkgs, lib, ... }: let
  aarch64-pkgs = pkgs.pkgsCross.aarch64-multiplatform;
in

with builtins;
with lib;

let
  env = pkgs.buildEnv {
    name = "koi";
    paths = with aarch64-pkgs; [
      # TODO: proot
      bash nodejs_latest
      (yarn.overrideAttrs (x: {
        buildInputs = [ nodejs_latest ];
      }))
    ];
  };
  info = readFile "${pkgs.closureInfo { rootPaths = [ env ]; }}/store-paths";
  bootstrap = pkgs.runCommand "bootstrap" {} ''
    mkdir -p $out/nix/store
    for i in "${info}"; do
      cp -r $i $out/nix/store
    done
    cp ${pkgs.prootTermux}/bin/proot-static $out/proot-static
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
