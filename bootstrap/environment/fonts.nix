{ runCommand, makeFontsConf, noto-fonts, noto-fonts-cjk-sans }: let
    fonts = makeFontsConf {
        fontDirectories = [
            noto-fonts
            noto-fonts-cjk-sans
        ];
    };
in runCommand "etc-fonts" {} ''
    mkdir -p $out/etc/fonts
    ln -s ${fonts} $out/etc/fonts/fonts.conf
''