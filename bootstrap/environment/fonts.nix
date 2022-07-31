{ runCommand, makeFontsConf, source-han-sans }: let
    fonts = makeFontsConf {
        fontDirectories = [ source-han-sans ];
    };
in runCommand "etc-fonts" {} ''
    mkdir -p $out/etc/fonts
    ln -s ${fonts} $out/etc/fonts/fonts.conf
''
