# chromium requires /etc/static/ssl to verify ssl certs

{ cacert, runCommand }:

runCommand "certs" {} ''
    mkdir -p $out/etc/ssl/certs
    ln -s ${cacert}/etc/ssl/certs/ca-bundle.crt $out/etc/ssl/certs/ca-bundle.crt
    ln -s ${cacert}/etc/ssl/certs/ca-bundle.crt $out/etc/ssl/certs/ca-certificates.crt
''