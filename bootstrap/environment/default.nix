{ pkgs, buildEnv, callPackage, lib }:

with builtins;
with lib;

let
    aarch64-pkgs = pkgs.pkgsCross.aarch64-multiplatform;
    profile = callPackage ./profile.nix {};
    login = callPackage ./login.nix {};
in buildEnv {
    name = "koishi-env";
    # TODO: /usr/bin/env
    paths = with aarch64-pkgs; [
        profile
        login
        cacert
        busybox
        nodejs_latest
        pkgs.inputs.anillc.packages.aarch64-linux.go-cqhttp
        (yarn.overrideAttrs (x: {
            buildInputs = [ nodejs_latest ];
        }))
    ];
}