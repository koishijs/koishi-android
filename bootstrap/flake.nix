{
    inputs.nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    inputs.flake-utils.url = "github:numtide/flake-utils";
    inputs.anillc.url = "github:Anillc/flakes";
    inputs.nix-on-droid = {
        url = "github:Anillc/nix-on-droid";
        inputs.nixpkgs.follows = "nixpkgs";
    };
    outputs = inputs@{ self, nixpkgs, flake-utils, anillc, nix-on-droid }:
        with flake-utils.lib;
    eachDefaultSystem (system: let
        pkgs = import nixpkgs {
            inherit system;
            overlays = [
                (oself: osuper: self.packages.${system}
                    // anillc.packages.${system}
                    // nix-on-droid.packages.${system}
                    // {
                        inherit inputs;
                    })
            ];
        };
    in {
        packages.bootstrap = pkgs.callPackage ./bootstrap.nix {};
        apps.copy = mkApp {
            drv = pkgs.writeScriptBin "copy" ''
                FOLDER=../app/src/main/assets/bootstrap
                rm -rf $FOLDER && mkdir -p $FOLDER
                cp -f ${self.packages.${system}.bootstrap}/* $FOLDER
            '';
        };
    });
}
