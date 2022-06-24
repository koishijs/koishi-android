{ runCommand, lib }:

with builtins;
with lib;

runCommand "etc-profile" {} ''
  mkdir -p $out/etc
  cat > $out/etc/profile <<EOF
  export PATH=/bin
  EOF
  chmod +x $out/etc/profile
''