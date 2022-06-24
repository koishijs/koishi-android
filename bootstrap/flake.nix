{
  inputs.nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
  inputs.flake-utils.url = "github:numtide/flake-utils";
  inputs.nix-on-droid.url = "github:Anillc/nix-on-droid";
  outputs = { self, nixpkgs, flake-utils, nix-on-droid }: flake-utils.lib.eachDefaultSystem (system: let
    pkgs = import nixpkgs {
      inherit system;
      overlays = [
        (oself: osuper: self.packages.${system})
        (oself: osuper: nix-on-droid.packages.${system})
      ];
    };
  in {
    packages.bootstrap = pkgs.callPackage ./bootstrap.nix {};
  });
}
