{ pkgs, buildEnv, callPackage, lib, inputs }:

with builtins;
with lib;

let
    aarch64-pkgs = import inputs.nixpkgs { system = "aarch64-linux"; };
    login = callPackage ./login.nix {};
    env = callPackage ./env.nix { inherit (aarch64-pkgs) busybox; };
    resolvconf = callPackage ./resolvconf.nix {};
    fonts = callPackage ./fonts.nix {};
    certs = callPackage ./certs.nix {};
in buildEnv {
    name = "koishi-env";
    paths = with aarch64-pkgs; [
        login env
        certs
        busybox zip
        chromium fonts
        nodejs
    ];
}
