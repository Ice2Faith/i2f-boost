package i2f.core.command;

import java.util.ArrayList;

/**
 * @author Ice2Faith
 * @date 2023/4/3 10:23
 * @desc
 */
public class CommandParser {
    private static ArrayList<String> cmdArgs = new ArrayList<>();

    // 初始化CMD全局变量
    public static void init(String... args) {
        free();
        for (String arg : args) {
            cmdArgs.add(arg);
        }
    }

    // 释放CMD全局变量
    public static void free() {
        cmdArgs.clear();
    }

    // 获取原始的入口参数个数
    public static int getCount() {
        return cmdArgs.size();
    }

    // 获取原始的入口参数
    public static ArrayList<String> getArgs() {
        return new ArrayList<>(cmdArgs);
    }

    // 是否存在CMD开关选项，开关选项是以-或/开头的CMD参数，例如：-f -n /s等
    // 在命令行中，使用/f -n的选项，此函数则使用f n进行检测，注意，没有/或-的前缀
    // 函数会自动添加
    public static boolean optionExists(String option) {
        boolean ret = false;
        for (String arg : cmdArgs) {
            if (arg.startsWith("-" + option) || arg.startsWith("/" + option)) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    // 获取指定选项后跟随的参数
    // 例如命令行：-i test.txt readme.txt -o output.txt
    // 则查找-i之后的参数，则入参为i,返回test.txt 和 readme.txt两个字符串
    // 也就是查找指定选项之后的所有非选项参数
    public static ArrayList<String> optionArgs(String option) {
        ArrayList<String> ret = new ArrayList<>();
        boolean begin = false;
        for (String arg : cmdArgs) {
            if (arg.startsWith("-" + option) || arg.startsWith("/" + option)) {
                begin = true;
                continue;
            }
            if (arg.startsWith("-") || arg.startsWith("/")) {
                break;
            }
            if (begin) {
                ret.add(arg);
            }
        }
        return ret;
    }
}
