package i2f.core.io.file;

import i2f.core.container.array.Arrays;
import i2f.core.io.stream.StreamUtil;
import i2f.core.security.jce.codec.bytes.raw.HexStringByteCodec;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2023/6/21 9:58
 * @desc
 */
public class FileTypeUtil {
    public static boolean isType(File file, FileType type) throws IOException {
        byte[] magicHeader = type.getMagicNumber();
        byte[] fileHeader = StreamUtil.readBytes(file, type.getJumpBytes(), magicHeader.length);
        int rs = Arrays.compare(magicHeader, fileHeader);
        return rs == 0;
    }

    public static FileType matchType(File file) throws IOException {
        FileType[] values = FileType.values();
        List<FileType> list = new ArrayList<>(values.length);
        int maxLen = 0;
        for (FileType value : values) {
            list.add(value);
            int len = value.getMagicNumber().length + value.getJumpBytes();
            if (len > maxLen) {
                maxLen = len;
            }
        }

        list.sort(new Comparator<FileType>() {
            @Override
            public int compare(FileType o1, FileType o2) {
                return Integer.compare(o1.getMagicNumber().length, o2.getMagicNumber().length);
            }
        }.reversed());

        byte[] fileHeader = StreamUtil.readBytes(file, maxLen);
        for (FileType item : list) {
            byte[] magicHeader = StreamUtil.readBytes(new ByteArrayInputStream(fileHeader), item.getJumpBytes(), item.getMagicNumber().length, true);
            if (Arrays.compare(magicHeader, item.getMagicNumber()) == 0) {
                return item;
            }
        }

        return FileType.UNKNOWN;
    }

    private static File dir = new File("E:\\MySystemDefaultFiles\\Desktop");
    private static Map<String, File> suffixMap = new LinkedHashMap<>();

    private static void findSingleSuffixFiles(File path) {
        if (!path.exists()) {
            return;
        }
        if (path.isFile()) {
            String name = path.getName();
            int idx = name.lastIndexOf(".");
            if (idx >= 0) {
                String suffix = name.substring(idx);
                if (!suffixMap.containsKey(suffix)) {
                    suffixMap.put(suffix, path);
                } else {
                    File exFile = suffixMap.get(suffix);
                    if (path.length() < exFile.length() && path.length() > 128) {
                        suffixMap.put(suffix, path);
                    }
                }
            }
        } else if (path.isDirectory()) {
            File[] files = path.listFiles();
            if (files == null) {
                return;
            }
            for (File file : files) {
                findSingleSuffixFiles(file);
            }
        }
    }

    private static Set<String> jumpSuffix = new HashSet<>(java.util.Arrays.asList(
            ".txt", ".sql", ".html", ".htm", ".vue", ".log", ".js", ".css", ".jsp", ".cpp", ".hpp",
            ".h", ".md", ".bat", ".sh", ".cmd", ".vbs", ".c", ".conf", ".ini", ".inf", ".config",
            ".cs", ".csproj", ".csv", ".gitignore", ".go", ".java", ".py", ".php", ".gradle",
            ".xml", ".json", ".grd", ".markdown", ".lua", ".mjs", ".name", ".npmignore",
            ".zip", ".pdf", ".png", ".jpg", ".ppt", ".pptx", ".doc", ".docx", ".xls", ".xlsx",
            ".properties", ".yml", ".yaml", ".vm", ".scss", ".ts",

            ".class", ".pdf", ".rar", ".tar", ".gz", ".tgz", ".7z", ".class", ".smali",
            ".wps", ".png", ".jpg", ".jpeg", ".bmp", ".gif", ".ico", ".bsd", ".mp3", ".wav",
            ".flv", ".avi", ".mp4", ".exe", ".lib", ".elf", ".so", ".o", ".luac", ".pyc",
            ".cat", ".cdf-ms", ".chm", ".bdr"
    ));
    private static Map<String, Set<String>> suffixHeaderMap = new TreeMap<>();
    private static Map<String, Set<String>> headerSuffixMap = new TreeMap<>();
    private static HexStringByteCodec hexCodec = new HexStringByteCodec(null);

    private static void findSuffixAndHeaderMap(File path, int level) {
        if (level < 0) {
            return;
        }
        if (!path.exists()) {
            return;
        }
        if (path.isFile()) {
            String name = path.getName();
            int idx = name.lastIndexOf(".");
            if (idx >= 0) {
                String suffix = name.substring(idx);
                suffix = suffix.toLowerCase();
                if (jumpSuffix.contains(suffix)) {
//                    try{
//                        FileType fileType = matchType(path);
//                        System.out.println("ignore:"+fileType+":"+path.getName());
//                    }catch(Exception e){
//
//                    }
                    return;
                }
                try {
                    byte[] data = StreamUtil.readBytes(path, 20);
                    String hex = hexCodec.encode(data);
                    if (!suffixHeaderMap.containsKey(suffix)) {
                        suffixHeaderMap.put(suffix, new TreeSet<>());
                    }
                    suffixHeaderMap.get(suffix).add(hex);

                    if (!headerSuffixMap.containsKey(hex)) {
                        headerSuffixMap.put(hex, new TreeSet<>());
                    }
                    headerSuffixMap.get(hex).add(suffix);
                } catch (Exception e) {

                }
            }
        } else if (path.isDirectory()) {
            File[] files = path.listFiles();
            if (files == null) {
                return;
            }
            for (File file : files) {
                findSuffixAndHeaderMap(file, level - 1);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        findSingleSuffixFiles(new File("D:\\"));

        for (File file : suffixMap.values()) {
            FileType fileType = matchType(file);
            System.out.println(fileType + ":" + file.getName() + "    >>>>    " + file.getAbsolutePath());
        }
//
//        findSuffixAndHeaderMap(new File("c:\\"),5);
//        for (Map.Entry<String, Set<String>> entry : suffixHeaderMap.entrySet()) {
//            String suffix = entry.getKey();
//            Set<String> headers = entry.getValue();
//            for (String header : headers) {
//                System.out.println(suffix+" \t: "+header);
//            }
//        }
//
//        System.out.println("\n\n\n\n");
//        for (Map.Entry<String, Set<String>> entry : headerSuffixMap.entrySet()) {
//            String header = entry.getKey();
//            Set<String> suffixes = entry.getValue();
//            for (String suffix : suffixes) {
//                System.out.println(header+" \t: "+suffix);
//            }
//        }
    }
}
