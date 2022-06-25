{ busybox, runCommand }:

runCommand "env" {} ''
    mkdir -p $out/usr/bin
    ln -s ${busybox}/bin/env $out/usr/bin/env
''