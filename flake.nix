{
  description = "Description for the project";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    devshell.url = "github:numtide/devshell";
    android.url = "github:tadfisher/android-nixpkgs";
  };

  outputs = inputs@{ flake-parts, android, ... }:
    flake-parts.lib.mkFlake { inherit inputs; } {
      imports = [
        inputs.devshell.flakeModule
      ];
      systems = [ "x86_64-linux" "aarch64-linux" "aarch64-darwin" "x86_64-darwin" ];
      perSystem = { config, pkgs, system, ... }: {
        packages.default = pkgs.hello;
        devshells.default = let
          android-sdk = android.sdk.${system} (pkgs: with pkgs; [
            cmdline-tools-latest
            build-tools-30-0-3
            build-tools-33-0-2
            platform-tools
            platforms-android-30
            platforms-android-33
            emulator
          ]);
        in {
          env = [{
            name = "ANDROID_HOME";
            value = "${android-sdk}/share/android-sdk";
          }];
          packages = with pkgs; [
            android-sdk gradle yarn
          ];
        };
      };
    };
}
