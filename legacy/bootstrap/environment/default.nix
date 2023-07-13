{ pkgs, buildEnv, callPackage, lib, inputs, full ? false }:

with builtins;
with lib;

let
    aarch64-pkgs = import inputs.nixpkgs { system = "aarch64-linux"; };
    login = callPackage ./login.nix {};
    env = callPackage ./env.nix { inherit (aarch64-pkgs) busybox; };
    fonts = callPackage ./fonts.nix {};
    certs = callPackage ./certs.nix {};
    timezone = callPackage ./timezone.nix {};
in buildEnv {
    name = "koishi-env";
    # dns is in login.nix
    paths = with aarch64-pkgs; [
        login env
        certs
        busybox zip
        nodejs
    ] ++ (optionals full [
        fonts
        chromium
        timezone
    ]);
}
