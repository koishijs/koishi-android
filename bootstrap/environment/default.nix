{ pkgs, buildEnv, callPackage, lib }:

with builtins;
with lib;

let
    aarch64-pkgs = pkgs.pkgsCross.aarch64-multiplatform;
    profile = callPackage ./profile.nix {};
    login = callPackage ./login.nix {};
in buildEnv {
    name = "koishi-env";
    paths = with aarch64-pkgs; [
        profile
        cacert
        bash
        coreutils
        nodejs_latest
        go-cqhttp
        (yarn.overrideAttrs (x: {
            buildInputs = [ nodejs_latest ];
        }))
    ];
}