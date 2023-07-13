{ tzdata, runCommand }:

runCommand "timezone" {} ''
    mkdir -p $out/etc
    ln -s ${tzdata}/share/zoneinfo $out/etc/zoneinfo
''
