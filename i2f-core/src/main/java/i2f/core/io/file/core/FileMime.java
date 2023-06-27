package i2f.core.io.file.core;


import i2f.core.annotations.remark.Author;
import i2f.core.type.str.Strings;

@Author("i2f")
public class FileMime {
    public static String getMimeType(String fileName) {
        String suffix = Strings.getExtension(fileName);

        String ret = "application/octet-stream";
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (suffix.equalsIgnoreCase(MIME_MapTable[i][0])) {
                ret = MIME_MapTable[i][1];
                break;
            }
        }
        return ret;
    }
    //建立一个MIME类型与文件后缀名的匹配表
    private static final String[][] MIME_MapTable = {
            //{后缀名，    MIME类型}
            {".txt",	"text/plain"},
            {".text",	"text/plain"},
            {".htm",	"text/html"},
            {".html",	"text/html"},
            {".stm",	"text/html"},
            {".xhtml",	"application/xhtml+xml"},
            {".js",	"text/javascript"},
            {".css",	"text/css"},
            {".xml",	"text/xml"},
            {".json",	"application/json"},
            {".pdf",	"application/pdf"},
            {".doc",	"application/msword"},
            {".xls",	"application/vnd.ms-excel"},
            {".ppt",	"application/vnd.ms-powerpoint"},
            {".wps",	"application/vnd.ms-works"},
            {".vsd",	"application/vnd.visio"},
            {".pptx",	"application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".xlsx",	"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".docx",	"application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".c",	"text/plain"},
            {".cpp",	"text/plain"},
            {".h",	"text/plain"},
            {".hpp",	"text/plain"},
            {".phps",	"text/text"},
            {".java",	"text/x-java"},
            {".py",	"text/plain"},
            {".go",	"text/plain"},
            {".sh",	"text/plain"},
            {".bat",	"text/plain"},
            {".csv",	"text/csv"},
            {".dot",	"application/msword"},
            {".pot",	"application/vnd.ms-powerpoint"},
            {".pps",	"application/vnd.ms-powerpoint"},
            {".xlt",	"application/vnd.ms-excel"},
            {".xlw",	"application/vnd.ms-excel"},
            {".ppsx",	"application/vnd.openxmlformats-officedocument.presentationml.slideshow"},
            {".potx",	"application/vnd.openxmlformats-officedocument.presentationml.template"},
            {".xltx",	"application/vnd.openxmlformats-officedocument.spreadsheetml.template"},
            {".conf",	"text/plain"},
            {".log",	"text/plain"},
            {".asm",	"text/plain"},
            {".prop",	"text/plain"},
            {".rc",	"text/plain"},
            {".ini",	"text/plain"},
            {".dotx",	"application/vnd.openxmlformats-officedocument.wordprocessingml.template"},
            {".jpg",	"image/jpeg"},
            {".jpeg",	"image/jpeg"},
            {".png",	"image/png"},
            {".gif",	"image/gif"},
            {".ico",	"image/ico"},
            {".wbmp",	"image/vnd.wap.wbmp"},
            {".webp",	"image/webp"},
            {".bmp",	"image/bmp"},
            {".tif",	"image/tiff"},
            {".tiff",	"image/tiff"},
            {".psd",	"image/x-photoshop"},
            {".jpe",	"image/jpeg"},
            {".cur",	"image/ico"},
            {".svg",	"image/svg+xml"},
            {".svgz",	"image/svg+xml"},
            {".ttf",	"font/ttf"},
            {".woff",	"font/woff"},
            {".otf",	"font/otf"},
            {".woff2",	"font/woff2"},
            {".aac",	"audio/aac"},
            {".mp3",	"audio/mpeg"},
            {".wav",	"audio/wav"},
            {".oga",	"audio/ogg"},
            {".m4a",	"audio/mpeg"},
            {".mp2",	"audio/mpeg"},
            {".mpega",	"audio/mpeg"},
            {".mpga",	"audio/mpeg"},
            {".m3u",	"audio/mpegurl"},
            {".flac",	"application/x-flac"},
            {".amr",	"audio/amr"},
            {".ogg",	"application/ogg"},
            {".ogx",	"application/ogg"},
            {".mp4",	"video/mp4"},
            {".avi",	"video/x-msvideo"},
            {".mpeg",	"video/mpeg"},
            {".mov",	"video/quicktime"},
            {".rmvb",	"video/vdn.rn-realvideo"},
            {".flv",	"video/x-flv"},
            {".mkv",	"video/x-matroska"},
            {".mpg4",	"video/mp4"},
            {".m4b",	"audio/mp4a-latm"},
            {".m4p",	"audio/mp4a-latm"},
            {".m4u",	"video/vnd.mpegurl"},
            {".qt",	"video/quicktime"},
            {".vob",	"video/mpeg"},
            {".ogv",	"video/ogg"},
            {".wmv",	"video/x-ms-wmv"},
            {".movie",	"video/x-sgi-movie"},
            {".fli",	"video/fli"},
            {".m4v",	"video/m4v"},
            {".3g2",	"video/3gpp"},
            {".3gp",	"video/3gpp"},
            {".3gpp",	"video/3gpp"},
            {".mpa",	"video/mpeg"},
            {".mpe",	"video/mpeg"},
            {".mpg",	"video/mpeg"},
            {".mpv2",	"video/mpeg"},
            {".asf",	"video/x-ms-asf"},
            {".mv",	"video/x-sgi-movie"},
            {".m13",	"application/x-msmediaview"},
            {".m14",	"application/x-msmediaview"},
            {".mvb",	"application/x-msmediaview"},
            {".wmf",	"application/x-msmetafile"},
            {".7z",	"application/x-7z-compressed"},
            {".bz",	"application/x-bzip"},
            {".bz2",	"application/x-bzip2"},
            {".z",	"application/x-compress"},
            {".gtar",	"application/x-gtar"},
            {".taz",	"application/x-gtar"},
            {".tgz",	"application/x-gtar"},
            {".gz",	"application/x-gzip"},
            {".tar",	"application/x-tar"},
            {".zip",	"application/zip"},
            {".jar",	"application/java-archive"},
            {".rar",	"application/rar"},
            {".apk",	"application/vnd.android.package-archive"},
            {".exe",	"application/octet-stream"},
            {".class",	"application/octet-stream"},
            {".cod",	"image/cis-cod"},
            {".ief",	"image/ief"},
            {".pcx",	"image/pcx"},
            {".jfif",	"image/pipeg"},
            {".djv",	"image/vnd.djvu"},
            {".djvu",	"image/vnd.djvu"},
            {".ras",	"image/x-cmu-raster"},
            {".cmx",	"image/x-cmx"},
            {".cdr",	"image/x-coreldraw"},
            {".pat",	"image/x-coreldrawpattern"},
            {".cdt",	"image/x-coreldrawtemplate"},
            {".art",	"image/x-jg"},
            {".jng",	"image/x-jng"},
            {".pnm",	"image/x-portable-anymap"},
            {".pbm",	"image/x-portable-bitmap"},
            {".pgm",	"image/x-portable-graymap"},
            {".ppm",	"image/x-portable-pixmap"},
            {".rgb",	"image/x-rgb"},
            {".xbm",	"image/x-xbitmap"},
            {".xpm",	"image/x-xpixmap"},
            {".xwd",	"image/x-xwindowdump"},
            {".au",	"audio/basic"},
            {".snd",	"audio/basic"},
            {".mid",	"audio/mid"},
            {".rmi",	"audio/mid"},
            {".kar",	"audio/midi"},
            {".midi",	"audio/midi"},
            {".xmf",	"audio/midi"},
            {".mxmf",	"audio/mobile-xmf"},
            {".sid",	"audio/prs.sid"},
            {".weba",	"audio/webm"},
            {".aif",	"audio/x-aiff"},
            {".aifc",	"audio/x-aiff"},
            {".aiff",	"audio/x-aiff"},
            {".gsm",	"audio/x-gsm"},
            {".wax",	"audio/x-ms-wax"},
            {".wma",	"audio/x-ms-wma"},
            {".ram",	"audio/x-pn-realaudio"},
            {".rm",	"audio/x-pn-realaudio"},
            {".qcp",	"audio/x-qcp"},
            {".ra",	"audio/x-realaudio"},
            {".pls",	"audio/x-scpls"},
            {".sd2",	"audio/x-sd2"},
            {".dl",	"video/dl"},
            {".dif",	"video/dv"},
            {".dv",	"video/dv"},
            {".mxu",	"video/vnd.mpegurl"},
            {".webm",	"video/webm"},
            {".lsf",	"video/x-la-asf"},
            {".lsx",	"video/x-la-asf"},
            {".mng",	"video/x-mng"},
            {".asr",	"video/x-ms-asf"},
            {".asx",	"video/x-ms-asf"},
            {".wm",	"video/x-ms-wm"},
            {".wmx",	"video/x-ms-wmx"},
            {".wvx",	"video/x-ms-wvx"},
            {".mjs",	"text/javascript"},
            {".cls",	"text/x-tex"},
            {".diff",	"text/plain"},
            {".xla",	"application/vnd.ms-excel"},
            {".xlc",	"application/vnd.ms-excel"},
            {".xlm",	"application/vnd.ms-excel"},
            {".eot",	"application/vnd.ms-fontobject"},
            {".msg",	"application/vnd.ms-outlook"},
            {".stl",	"application/vnd.ms-pki.stl"},
            {".sst",	"application/vnd.ms-pkicertstore"},
            {".cat",	"application/vnd.ms-pkiseccat"},
            {".mpp",	"application/vnd.ms-project"},
            {".wcm",	"application/vnd.ms-works"},
            {".wdb",	"application/vnd.ms-works"},
            {".wks",	"application/vnd.ms-works"},
            {".mht",	"message/rfc822"},
            {".mhtml",	"message/rfc822"},
            {".nws",	"message/rfc822"},
            {".texi",	"application/x-texinfo"},
            {".texinfo",	"application/x-texinfo"},
            {".ics",	"text/calendar"},
            {".icz",	"text/calendar"},
            {".323",	"text/h323"},
            {".uls",	"text/iuls"},
            {".mml",	"text/mathml"},
            {".asc",	"text/plain"},
            {".bas",	"text/plain"},
            {".po",	"text/plain"},
            {".rtf",	"text/rtf"},
            {".sct",	"text/scriptlet"},
            {".tsv",	"text/tab-separated-values"},
            {".ts",	"text/texmacs"},
            {".htt",	"text/webviewhtml"},
            {".bib",	"text/x-bibtex"},
            {".boo",	"text/x-boo"},
            {".h++",	"text/x-c++hdr"},
            {".hh",	"text/x-c++hdr"},
            {".hxx",	"text/x-c++hdr"},
            {".c++",	"text/x-c++src"},
            {".cxx",	"text/x-c++src"},
            {".htc",	"text/x-component"},
            {".csh",	"text/x-csh"},
            {".d",	"text/x-dsrc"},
            {".hs",	"text/x-haskell"},
            {".lhs",	"text/x-literate-haskell"},
            {".moc",	"text/x-moc"},
            {".p",	"text/x-pascal"},
            {".pas",	"text/x-pascal"},
            {".gcd",	"text/x-pcs-gcd"},
            {".etx",	"text/x-setext"},
            {".tcl",	"text/x-tcl"},
            {".ltx",	"text/x-tex"},
            {".sty",	"text/x-tex"},
            {".tex",	"text/x-tex"},
            {".vcs",	"text/x-vcalendar"},
            {".vcf",	"text/x-vcard"},
            {".ez",	"application/andrew-inset"},
            {".tsp",	"application/dsptype"},
            {".epub",	"application/epub+zip"},
            {".spl",	"application/futuresplash"},
            {".hta",	"application/hta"},
            {".jsonld",	"application/ld+json"},
            {".hqx",	"application/mac-binhex40"},
            {".cpt",	"application/mac-compactpro"},
            {".nb",	"application/mathematica"},
            {".mdb",	"application/msaccess"},
            {".bin",	"application/octet-stream"},
            {".oda",	"application/oda"},
            {".axs",	"application/olescript"},
            {".key",	"application/pgp-keys"},
            {".pgp",	"application/pgp-signature"},
            {".prf",	"application/pics-rules"},
            {".p10",	"application/pkcs10"},
            {".crl",	"application/pkix-crl"},
            {".ai",	"application/postscript"},
            {".eps",	"application/postscript"},
            {".ps",	"application/postscript"},
            {".rdf",	"application/rdf+xml"},
            {".rss",	"application/rss+xml"},
            {".set",	"application/set-payment-initiation"},
            {".azw",	"application/vnd.amazon.ebook"},
            {".mpkg",	"application/vnd.apple.installer+xml"},
            {".cdy",	"application/vnd.cinderella"},
            {".kml",	"application/vnd.google-earth.kml+xml"},
            {".kmz",	"application/vnd.google-earth.kmz"},
            {".xul",	"application/vnd.mozilla.xul+xml"},
            {".odb",	"application/vnd.oasis.opendocument.database"},
            {".odf",	"application/vnd.oasis.opendocument.formula"},
            {".odg",	"application/vnd.oasis.opendocument.graphics"},
            {".otg",	"application/vnd.oasis.opendocument.graphics-template"},
            {".odi",	"application/vnd.oasis.opendocument.image"},
            {".odp",	"application/vnd.oasis.opendocument.presentation"},
            {".ods",	"application/vnd.oasis.opendocument.spreadsheet"},
            {".ots",	"application/vnd.oasis.opendocument.spreadsheet-template"},
            {".odt",	"application/vnd.oasis.opendocument.text"},
            {".odm",	"application/vnd.oasis.opendocument.text-master"},
            {".ott",	"application/vnd.oasis.opendocument.text-template"},
            {".oth",	"application/vnd.oasis.opendocument.text-web"},
            {".mmf",	"application/vnd.smaf"},
            {".sdc",	"application/vnd.stardivision.calc"},
            {".sda",	"application/vnd.stardivision.draw"},
            {".sdd",	"application/vnd.stardivision.impress"},
            {".sdp",	"application/vnd.stardivision.impress"},
            {".smf",	"application/vnd.stardivision.math"},
            {".sdw",	"application/vnd.stardivision.writer"},
            {".vor",	"application/vnd.stardivision.writer"},
            {".sgl",	"application/vnd.stardivision.writer-global"},
            {".sxc",	"application/vnd.sun.xml.calc"},
            {".stc",	"application/vnd.sun.xml.calc.template"},
            {".sxd",	"application/vnd.sun.xml.draw"},
            {".std",	"application/vnd.sun.xml.draw.template"},
            {".sxi",	"application/vnd.sun.xml.impress"},
            {".sti",	"application/vnd.sun.xml.impress.template"},
            {".sxm",	"application/vnd.sun.xml.math"},
            {".sxw",	"application/vnd.sun.xml.writer"},
            {".sxg",	"application/vnd.sun.xml.writer.global"},
            {".stw",	"application/vnd.sun.xml.writer.template"},
            {".hlp",	"application/winhlp"},
            {".abw",	"application/x-abiword"},
            {".dmg",	"application/x-apple-diskimage"},
            {".bcpio",	"application/x-bcpio"},
            {".torrent",	"application/x-bittorrent"},
            {".cdf",	"application/x-cdf"},
            {".vcd",	"application/x-cdlink"},
            {".pgn",	"application/x-chess-pgn"},
            {".cpio",	"application/x-cpio"},
            {".deb",	"application/x-debian-package"},
            {".udeb",	"application/x-debian-package"},
            {".dcr",	"application/x-director"},
            {".dir",	"application/x-director"},
            {".dxr",	"application/x-director"},
            {".dms",	"application/x-dms"},
            {".wad",	"application/x-doom"},
            {".dvi",	"application/x-dvi"},
            {".gsf",	"application/x-font"},
            {".pcf",	"application/x-font"},
            {".pcf.Z",	"application/x-font"},
            {".pfa",	"application/x-font"},
            {".pfb",	"application/x-font"},
            {".arc",	"application/x-freearc"},
            {".mm",	"application/x-freemind"},
            {".gnumeric",	"application/x-gnumeric"},
            {".sgf",	"application/x-go-sgf"},
            {".gcf",	"application/x-graphing-calculator"},
            {".hdf",	"application/x-hdf"},
            {".ica",	"application/x-ica"},
            {".ins",	"application/x-internet-signup"},
            {".isp",	"application/x-internet-signup"},
            {".iii",	"application/x-iphone"},
            {".iso",	"application/x-iso9660-image"},
            {".jmz",	"application/x-jmol"},
            {".chrt",	"application/x-kchart"},
            {".kil",	"application/x-killustrator"},
            {".skd",	"application/x-koan"},
            {".skm",	"application/x-koan"},
            {".skp",	"application/x-koan"},
            {".skt",	"application/x-koan"},
            {".kpr",	"application/x-kpresenter"},
            {".kpt",	"application/x-kpresenter"},
            {".ksp",	"application/x-kspread"},
            {".kwd",	"application/x-kword"},
            {".kwt",	"application/x-kword"},
            {".latex",	"application/x-latex"},
            {".lha",	"application/x-lha"},
            {".lzh",	"application/x-lzh"},
            {".lzx",	"application/x-lzx"},
            {".book",	"application/x-maker"},
            {".fb",	"application/x-maker"},
            {".fbdoc",	"application/x-maker"},
            {".frame",	"application/x-maker"},
            {".frm",	"application/x-maker"},
            {".maker",	"application/x-maker"},
            {".mif",	"application/x-mif"},
            {".crd",	"application/x-mscardfile"},
            {".clp",	"application/x-msclip"},
            {".dll",	"application/x-msdownload"},
            {".msi",	"application/x-msi"},
            {".mny",	"application/x-msmoney"},
            {".pub",	"application/x-mspublisher"},
            {".scd",	"application/x-msschedule"},
            {".trm",	"application/x-msterminal"},
            {".wmd",	"application/x-ms-wmd"},
            {".wmz",	"application/x-ms-wmz"},
            {".wri",	"application/x-mswrite"},
            {".nc",	"application/x-netcdf"},
            {".pac",	"application/x-ns-proxy-autoconfig"},
            {".nwc",	"application/x-nwc"},
            {".o",	"application/x-object"},
            {".oza",	"application/x-oz-application"},
            {".pma",	"application/x-perfmon"},
            {".pmc",	"application/x-perfmon"},
            {".pml",	"application/x-perfmon"},
            {".pmr",	"application/x-perfmon"},
            {".pmw",	"application/x-perfmon"},
            {".p12",	"application/x-pkcs12"},
            {".pfx",	"application/x-pkcs12"},
            {".p7b",	"application/x-pkcs7-certificates"},
            {".spc",	"application/x-pkcs7-certificates"},
            {".p7r",	"application/x-pkcs7-certreqresp"},
            {".p7c",	"application/x-pkcs7-mime"},
            {".p7m",	"application/x-pkcs7-mime"},
            {".p7s",	"application/x-pkcs7-signature"},
            {".qtl",	"application/x-quicktimeplayer"},
            {".shar",	"application/x-shar"},
            {".swf",	"application/x-shockwave-flash"},
            {".sit",	"application/x-stuffit"},
            {".sv4cpio",	"application/x-sv4cpio"},
            {".sv4crc",	"application/x-sv4crc"},
            {".roff",	"application/x-troff"},
            {".t",	"application/x-troff"},
            {".tr",	"application/x-troff"},
            {".man",	"application/x-troff-man"},
            {".me",	"application/x-troff-me"},
            {".ms",	"application/x-troff-ms"},
            {".ustar",	"application/x-ustar"},
            {".src",	"application/x-wais-source"},
            {".webarchive",	"application/x-webarchive"},
            {".webarchivexml",	"application/x-webarchive-xml"},
            {".wz",	"application/x-wingz"},
            {".cer",	"application/x-x509-ca-cert"},
            {".crt",	"application/x-x509-ca-cert"},
            {".der",	"application/x-x509-ca-cert"},
            {".xcf",	"application/x-xcf"},
            {".fig",	"application/x-xfig"},
            {".pko",	"application/ynd.ms-pkipko"},
            {".iges",	"model/iges"},
            {".igs",	"model/iges"},
            {".mesh",	"model/mesh"},
            {".msh",	"model/mesh"},
            {".silo",	"model/mesh"},
            {".ice",	"x-conference/x-cooltalk"},
            {".sisx",	"x-epoc/x-sisx-app"},
            {".flr",	"x-world/x-vrml"},
            {".vrml",	"x-world/x-vrml"},
            {".wrl",	"x-world/x-vrml"},
            {".wrz",	"x-world/x-vrml"},
            {".xaf",	"x-world/x-vrml"},
            {".xof",	"x-world/x-vrml"},
            {".mpc",	"application/vnd.mpohun.certificate"},
            //unknown type to binary common mime
            {"", "application/octet-stream"}
    };


}