{ pkgs, buildEnv, callPackage, lib, inputs }:

with builtins;
with lib;

let
    aarch64-pkgs = import inputs.nixpkgs { system = "aarch64-linux"; };
    profile = callPackage ./profile.nix {};
    login = callPackage ./login.nix {};
    env = callPackage ./env.nix { inherit (aarch64-pkgs) busybox; };
    resolvconf = callPackage ./resolvconf.nix {};
    fonts = callPackage ./fonts.nix {};
in buildEnv {
    name = "koishi-env";
    paths = with aarch64-pkgs; [
        profile login env
        resolvconf cacert
        busybox
        chromium fonts
        nodejs
        (yarn.overrideAttrs (x: {
            buildInputs = [ nodejs ];
        }))
    ];
}