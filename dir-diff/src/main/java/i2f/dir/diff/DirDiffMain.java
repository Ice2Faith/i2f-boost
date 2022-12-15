package i2f.dir.diff;


import java.io.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DirDiffMain {
    public static class Pair<K, V> {
        public K key;
        public V val;

        public Pair() {
        }

        public Pair(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length <= 0) {
            help();
            return;
        }
        String option = args[0];
        if ("list".equals(option)) {
            String path = args[1];
            String out = args[2];
            listPathFiles(path, out);
        } else if ("diff".equals(option)) {
            String file1 = args[1];
            String file2 = args[2];
            String out = args[3];
            diffFile(file1, file2, out);
        } else if ("copy".equals(option)) {
            String path = args[1];
            String file = args[2];
            String out = args[3];
            copyFile(path, file, out);
        } else {
            help();
        }
    }

    public static void help() {
        System.out.println("dir diff tool");
        System.out.println("args:");
        System.out.println("[option] [...args]");
        System.out.println("[option]");
        System.out.println("    help : print help");
        System.out.println("    list : list an dir to file");
        System.out.println("       list [listPath] [outFile]");
        System.out.println("       list ./apps list.txt");
        System.out.println("    diff : compare double file diff line.");
        System.out.println("       diff [file1] [file2] [outFile]");
        System.out.println("       diff server.txt local.txt diff.txt");
        System.out.println("    copy : copy file with file diff line.");
        System.out.println("       copy [listPath] [file] [outPath]");
        System.out.println("       copy ./apps server.txt ./pkg");


    }

    public static void listPathFiles(String path, String out) throws IOException {
        List<String> lines = listPathFiles(path);
        writeFile(out, lines);
    }

    public static List<String> listPathFiles(String path) throws IOException {
        File srcDir = new File(path);
        List<String> lines = new LinkedList<String>();
        listPathFile2File(srcDir, srcDir.getAbsolutePath(), lines);
        return lines;
    }

    public static void listPathFile2File(File file, String relativePath, List<String> lines) throws IOException {
        if (!file.exists()) {
            return;
        }
        String absolutePath = file.getAbsolutePath();
        if (absolutePath.startsWith(relativePath)) {
            absolutePath = absolutePath.substring(relativePath.length());
        }
        absolutePath = absolutePath.replaceAll("\\\\", "/");
        if (!"".equals(absolutePath)) {
            lines.add(absolutePath);
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File item : files) {
                listPathFile2File(item, relativePath, lines);
            }
        }
    }


    public static void copyFile(String path, String file, String out) throws IOException {
        List<String> lines1 = listPathFiles(path);
        List<String> lines2 = readFile(file);
        List<Pair<Integer, String>> diffLines = diffFile(lines1, lines2);

        List<String> shellLines = new LinkedList<>();
        List<String> batchLines = new LinkedList<>();

        for (Pair<Integer, String> item : diffLines) {
            int type = item.key;
            String suffix = item.val;
            if (type == 1) {
                File srcFile = new File(path, suffix);
                File dstFile = new File(out, suffix);
                copyFile(srcFile, dstFile);
            } else if (type == -1) {
                String rpath = suffix.startsWith("/") ? suffix : "/" + suffix;
                shellLines.add("rm -rf ." + rpath);
                batchLines.add("del /s /f /q ." + rpath.replaceAll("/", "\\\\"));
            }
        }

        if (!shellLines.isEmpty()) {
            writeFile(new File(out, "clean-file.sh").getAbsolutePath(), shellLines);
        }
        if (!batchLines.isEmpty()) {
            writeFile(new File(out, "clean-file.bat").getAbsolutePath(), batchLines);
        }
    }

    public static void copyFile(File srcFile, File dstFile) throws IOException {
        if (!dstFile.getParentFile().exists()) {
            dstFile.getParentFile().mkdirs();
        }
        FileInputStream is = new FileInputStream(srcFile);
        FileOutputStream os = new FileOutputStream(dstFile);
        byte[] buf = new byte[8192];
        int len = 0;
        while ((len = is.read(buf)) > 0) {
            os.write(buf, 0, len);
        }
        os.flush();
        os.close();
        is.close();
    }

    public static void diffFile(String file1, String file2, String out) throws IOException {
        List<Pair<Integer, String>> lines = diffFile(file1, file2);
        List<String> wlines = lines.stream().map(e -> {
            if (e.key == 0) {
                return "* " + e.val;
            }
            if (e.key == 1) {
                return "+ " + e.val;
            }
            if (e.key == -1) {
                return "- " + e.val;
            }
            return e.val;
        }).collect(Collectors.toList());
        writeFile(out, wlines);
    }


    public static List<Pair<Integer, String>> diffFile(String file1, String file2) throws IOException {
        List<String> file1Lines = readFile(file1);
        List<String> file2Lines = readFile(file2);
        return diffFile(file1Lines, file2Lines);
    }

    public static List<Pair<Integer, String>> diffFile(List<String> file1Lines, List<String> file2Lines) throws IOException {
        List<Pair<Integer, String>> ret = new LinkedList<>();

        Set<String> file1Set = new HashSet<String>(Math.max(file1Lines.size(), 32));
        Set<String> file2Set = new HashSet<String>(Math.max(file2Lines.size(), 32));

        for (String item : file1Lines) {
            if (!"".equals(item)) {
                file1Set.add(item);
            }
        }

        for (String item : file2Lines) {
            if (!"".equals(item)) {
                file2Set.add(item);
            }
        }

        for (String item : file1Set) {
            if (file2Set.contains(item)) {
                ret.add(new Pair<>(0, item));
            } else {
                ret.add(new Pair<>(1, item));
            }
        }
        for (String item : file2Set) {
            if (file1Set.contains(item)) {
                ret.add(new Pair<>(0, item));
            } else {
                ret.add(new Pair<>(-1, item));
            }
        }

        return ret;
    }

    public static void writeFile(String filePath, List<String> lines) throws IOException {
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
        for (String item : lines) {
            writer.write(item);
            writer.write("\n");
        }
        writer.flush();
        writer.close();
    }

    public static List<String> readFile(String filePath) throws IOException {
        File file = new File(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        List<String> lines = new LinkedList<>();
        String line = null;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();
        return lines;
    }

}
