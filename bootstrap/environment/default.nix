{ pkgs, buildEnv, callPackage, lib }:

with builtins;
with lib;

let
    aarch64-pkgs = pkgs.pkgsCross.aarch64-multiplatform;
    profile = callPackage ./profile.nix {};
    login = callPackage ./login.nix {};
    env = callPackage ./env.nix { inherit (aarch64-pkgs) busybox; };
    resolvconf = callPackage ./resolvconf.nix {};
in buildEnv {
    name = "koishi-env";
    paths = with aarch64-pkgs; [
        profile login env
        resolvconf cacert
        busybox
        nodejs_latest
        (yarn.overrideAttrs (x: {
            buildInputs = [ nodejs_latest ];
        }))
    ];
}