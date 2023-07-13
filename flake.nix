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
            build-tools-32-0-0
            platform-tools
            platforms-android-28
            emulator
          ]);
        in {
          packages = with pkgs; [
            android-sdk gradle yarn
          ];
        };
      };
    };
}
