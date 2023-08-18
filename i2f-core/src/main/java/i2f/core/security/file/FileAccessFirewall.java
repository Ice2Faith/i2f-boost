package i2f.core.security.file;

import java.io.File;
import java.net.URLEncoder;
import java.util.Stack;

/**
 * @author Ice2Faith
 * @date 2023/8/18 9:46
 * @desc
 */
public class FileAccessFirewall {

    /**
     * 判断访问路径是否安全
     * 是否存在路径注入
     * 是否存在路径截断
     * 是否存在命令执行
     * storePath最终访问应该在rootPath之下
     *
     * @param rootPath  路径，绝对或相对路径都可
     * @param storePath 路径，绝对或相对路径都可
     * @return
     */
    public static boolean isSafePath(String rootPath, String storePath) {
        if (storePath == null || "".equals(storePath)) {
            return true;
        }
        if (rootPath == null || "".equals(rootPath)) {
            return false;
        }

        if (!isAbsPath(rootPath)) {
            rootPath = new File(rootPath).getAbsolutePath();
        }

        rootPath = rootPath.replaceAll("\\\\", "/");
        storePath = storePath.replaceAll("\\\\", "/");

        if (!isAbsPath(storePath)) {
            storePath = combinePath(rootPath, storePath, "/");
        }
        storePath = getRoutePath(storePath);
        if (storePath == null) {
            return true;
        }
        if (!isSafePath(storePath)) {
            return false;
        }
        rootPath = getRoutePath(rootPath);
        if (rootPath == null) {
            return true;
        }
        if (!isSafePath(rootPath)) {
            return false;
        }

        if (!rootPath.endsWith("/")) {
            rootPath = rootPath + "/";
        }

        return storePath.startsWith(rootPath);
    }

    /**
     * 拼接路径
     *
     * @param prefix 前缀
     * @param suffix 后缀
     * @param sep    路径分隔符
     * @return
     */
    public static String combinePath(String prefix, String suffix, String sep) {
        if (prefix == null || "".equals(prefix)) {
            return suffix;
        }
        if (suffix == null || "".equals(suffix)) {
            return prefix;
        }
        if (prefix.endsWith(sep)) {
            if (suffix.startsWith(sep)) {
                return prefix + suffix.substring(sep.length());
            } else {
                return prefix + suffix;
            }
        } else {
            if (suffix.startsWith(sep)) {
                return prefix + suffix;
            } else {
                return prefix + sep + suffix;
            }
        }
    }

    /**
     * 判断是否是绝对路径
     *
     * @param path 路径
     * @return
     */
    public static boolean isAbsPath(String path) {
        if (path.startsWith("/")) {
            return true;
        }
        if (path.startsWith("\\")) {
            return true;
        }
        String rootDisk = null;
        if (path.length() >= 2) {
            String diskName = path.substring(0, 2);
            if (diskName.matches("^[a-zA-Z]:$")) {
                rootDisk = diskName;
            }
        }
        if (rootDisk != null) {
            return true;
        }
        return false;
    }

    /**
     * 判断路径是否是安全的路径
     *
     * @param path
     * @return
     */
    public static boolean isSafePath(String path) {
        if (path == null || "".equals(path)) {
            return true;
        }
        if (path.contains("\\")) {
            path = path.replaceAll("\\\\", "/");
        }
        return !isInjectCommand(path);
    }

    /**
     * 判断是否存在命令注入
     *
     * @param cmd
     * @return
     */
    public static boolean isInjectCommand(String cmd) {
        if (cmd == null || "".equals(cmd)) {
            return false;
        }
        cmd = cmd.toLowerCase();
        char[] keyChars = new char[]{'|', '&', ';', '>', '<', '`', '$', '\r', '\n', (char) 0};
        for (char ch : keyChars) {
            String str = "" + ch;
            if (cmd.contains(str)) {
                return true;
            }
            try {
                String url = URLEncoder.encode(str, "UTF-8").toLowerCase();
                if (cmd.contains(url)) {
                    return true;
                }
            } catch (Exception e) {

            }
            String hex = String.format("0x%02x", (int) ch);
            if (cmd.contains(hex)) {
                return true;
            }

        }
        return false;
    }

    /**
     * 移除路径中的相对路径
     * 如果因为相对路径移除之后，无法正确解析
     * 将返回null
     * 比如，一个路径：/a/../../ 这将会返回null
     *
     * @param path
     * @return
     */
    public static String getRoutePath(String path) {
        if (path == null || "".equals(path)) {
            return path;
        }
        String originSep = "/";
        if (path.contains("\\")) {
            originSep = "\\";
            path = path.replaceAll("\\\\", "/");
        }
        boolean isStartWith = path.startsWith("/");
        boolean isEndWith = path.endsWith("/");

        String[] arr = path.split("/");
        Stack<String> stack = new Stack<>();
        for (String item : arr) {
            if (".".equals(item)) {
                // do nothing
            } else if ("..".equals(item)) {
                if (stack.isEmpty()) {
                    return null;
                }
                stack.pop();
            } else {
                stack.push(item);
            }
        }

        Stack<String> reverseStack = new Stack<>();
        while (!stack.isEmpty()) {
            reverseStack.push(stack.pop());
        }

        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        while (!reverseStack.isEmpty()) {
            if (!isFirst) {
                builder.append(originSep);
            }
            builder.append(reverseStack.pop());
            isFirst = false;
        }
        String ret = builder.toString();
        if (isStartWith && !ret.startsWith(originSep)) {
            ret = originSep + ret;
        }
        if (isEndWith && !ret.endsWith(originSep)) {
            ret = ret + originSep;
        }
        return ret;
    }

}
