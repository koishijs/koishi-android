{ runCommand }:

runCommand "resolv.conf" {} ''
    mkdir -p $out/etc
    cat > $out/etc/resolv.conf <<EOF
    nameserver 223.5.5.5
    nameserver 114.114.114.114
    EOF
''