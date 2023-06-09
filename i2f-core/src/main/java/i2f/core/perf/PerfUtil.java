package i2f.core.perf;

import i2f.core.perf.data.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023/6/9 10:00
 * @desc
 */
public class PerfUtil {

    public static boolean isWindows() {
        return System.getProperty("os.name", "unknown").toLowerCase().contains("window");
    }

    public static boolean isLinux() {
        return System.getProperty("os.name", "unknown").toLowerCase().contains("linux");
    }

    public static String getCmdCharset() {
        String charset = System.getProperty("sun.jnu.encoding");
        if (charset == null || "".equals(charset)) {
            charset = System.getProperty("file.encoding");
        }
        if (charset == null || "".equals(charset)) {
            charset = Charset.defaultCharset().name();
        }
        return charset;
    }

    public static String subline(String str, int lineIndex, int lineCount) {
        String[] arr = str.split("\n");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < lineCount; i++) {
            if (i + lineIndex >= arr.length) {
                break;
            }
            builder.append(arr[i + lineIndex]);
            builder.append("\n");
        }
        return builder.toString();
    }


    public static LinuxIostatDto getLinuxIostatXk() {
        /**
         Linux 3.10.0-693.el7.x86_64 (dsj-hadoop-66)     02/13/2023      _x86_64_        (4 CPU)

         avg-cpu:  %user   %nice %system %iowait  %steal   %idle
         7.78    0.00    0.32    0.01    0.00   91.89

         Device:         rrqm/s   wrqm/s     r/s     w/s    rkB/s    wkB/s avgrq-sz avgqu-sz   await r_await w_await  svctm  %util
         sda               0.00     0.05    0.01    0.75     0.31     4.88    13.73     0.00    0.54   11.29    0.43   0.15   0.01
         sdb               0.00     0.87    0.04    0.91     0.63    35.04    75.39     0.00    1.41   10.29    1.02   0.33   0.03
         */
        if (!isLinux()) {
            return null;
        }

        LinuxIostatDto retDto = new LinuxIostatDto();
        List<LinuxIostatItemDto> ret = new ArrayList<>();
        retDto.items = ret;

        String str = runCmd("iostat -x -k");
        str = str.trim();
        String[] lines = str.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (i == 3) {
                String[] arr = line.trim().split("\\s+", 6);
                try {
                    retDto.userPer = Double.parseDouble(arr[0]);
                    retDto.nicePer = Double.parseDouble(arr[1]);
                    retDto.systemPer = Double.parseDouble(arr[2]);
                    retDto.iowaitPer = Double.parseDouble(arr[3]);
                    retDto.stealPer = Double.parseDouble(arr[4]);
                    retDto.idlePer = Double.parseDouble(arr[5]);
                } catch (Exception e) {

                }
            }
            if (i < 6) {
                continue;
            }

            String[] arr = line.trim().split("\\s+", 14);

            if (arr.length != 14) {
                continue;
            }

            LinuxIostatItemDto dto = new LinuxIostatItemDto();

            dto.device = arr[0];
            try {
                dto.rrqms = Double.parseDouble(arr[1]);
                dto.wrqms = Double.parseDouble(arr[2]);
                dto.rs = Double.parseDouble(arr[3]);
                dto.ws = Double.parseDouble(arr[4]);
                dto.rKbs = Double.parseDouble(arr[5]);
                dto.wKbs = Double.parseDouble(arr[6]);
                dto.avgRqSz = Double.parseDouble(arr[7]);
                dto.avgQuSz = Double.parseDouble(arr[8]);
                dto.await = Double.parseDouble(arr[9]);
                dto.rAwait = Double.parseDouble(arr[10]);
                dto.wAwait = Double.parseDouble(arr[11]);
                dto.svctm = Double.parseDouble(arr[12]);
                dto.utilPer = Double.parseDouble(arr[13]);
            } catch (Exception e) {

            }

            ret.add(dto);
        }

        return retDto;
    }


    public static List<LinuxDfDto> getLinuxDf() {
        /**
         Filesystem               1K-blocks     Used Available Use% Mounted on
         /dev/mapper/rhel-root    104806400 25646992  79159408  25% /
         devtmpfs                   8118132        0   8118132   0% /dev
         tmpfs                      8134020        0   8134020   0% /dev/shm
         */
        List<LinuxDfDto> ret = new ArrayList<>();
        if (!isLinux()) {
            return ret;
        }

        String str = runCmd("df");
        String[] lines = str.split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (i == 0) {
                continue;
            }
            String line = lines[i];
            String[] arr = line.trim().split("\\s+", 6);

            if (arr.length != 6) {
                continue;
            }
            LinuxDfDto dto = new LinuxDfDto();
            dto.fileSystem = arr[0];
            try {
                dto.blocks1k = Long.parseLong(arr[1]);
                dto.used = Long.parseLong(arr[2]);
                dto.available = Long.parseLong(arr[3]);
            } catch (Exception e) {

            }
            try {
                if (arr[4].endsWith("%")) {
                    String perc = arr[4].substring(0, arr[4].length() - 1);
                    dto.usePercent = Double.parseDouble(perc);
                }
            } catch (Exception e) {

            }
            dto.mountedOn = arr[5];

            ret.add(dto);
        }

        return ret;
    }

    public static String runCmd(String cmd) {
        try {
            Runtime runtime = Runtime.getRuntime();

            Process process = runtime.exec(cmd);
            InputStream is = process.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int len = 0;
            while ((len = is.read(buf)) > 0) {
                bos.write(buf, 0, len);
            }
            bos.flush();

            process.waitFor();

            String str = new String(bos.toByteArray(), getCmdCharset());
            return str;
        } catch (Exception e) {

        }
        return null;
    }


    public static LinuxTop5Dto getLinuxTop5() {
        if (!isLinux()) {
            return null;
        }
        String cmd = runCmd("top -c -b -n 1");
        cmd = subline(cmd, 0, 5);
        return getLinuxTop5(cmd);
    }


    public static LinuxTop5Dto getLinuxTop5(String cmd) {
        /**
         top - 10:28:31 up 228 days, 13:37,  5 users,  load average: 0.92, 0.51, 0.44
         Tasks: 268 total,   1 running, 267 sleeping,   0 stopped,   0 zombie
         %Cpu(s): 27.3 us,  4.5 sy,  0.0 ni, 68.2 id,  0.0 wa,  0.0 hi,  0.0 si,  0.0 st
         KiB Mem : 16268044 total,   169748 free, 13275500 used,  2822796 buff/cache
         KiB Swap:  8380412 total,  5365312 free,  3015100 used.  2360968 avail Mem
         */
        if (cmd == null) {
            return null;
        }
        LinuxTop5Dto ret = new LinuxTop5Dto();
        String cmdRs = cmd.trim();
        String[] lines = cmdRs.split("\n");
        for (String item : lines) {
            String line = item.toLowerCase().trim();
            if (line.startsWith("top")) {
                String[] arr = line.split("-", 2);
                if (arr.length == 2) {
                    String val = arr[1].trim();
                    String[] parts = val.split(",", 4);
                    if (parts.length == 4) {

                        String[] times = parts[0].trim().split("\\s*up\\s*", 2);
                        if (times.length == 2) {
                            ret.topDate = times[0].trim();
                            try {
                                ret.topDays = Integer.parseInt(times[1].trim().split("\\s+")[0]);
                            } catch (Exception e) {
                            }
                        }

                        ret.topTime = parts[1].trim();

                        try {
                            ret.topUsers = Integer.parseInt(parts[2].trim().split("\\s+")[0]);
                        } catch (Exception e) {
                        }

                        try {
                            String average = parts[3].trim().split(":", 2)[1].trim();
                            String[] avgArr = average.split("\\s*\\,\\s*");
                            ret.loadAverage1 = Double.parseDouble(avgArr[0]);
                            ret.loadAverage2 = Double.parseDouble(avgArr[1]);
                            ret.loadAverage3 = Double.parseDouble(avgArr[2]);
                        } catch (Exception e) {
                        }

                    }

                }

            } else if (line.startsWith("tasks:")) {
                String[] arr = line.split(":", 2);
                String val = arr[1].trim();
                String[] vals = val.split("\\s*,\\s*");
                String total = null;
                String running = null;
                String sleeping = null;
                String stopped = null;
                String zombie = null;
                for (String task : vals) {
                    String[] kv = task.trim().split("\\s+", 2);
                    if (kv.length != 2) {
                        continue;
                    }
                    if ("total".equals(kv[1])) {
                        total = kv[0];
                    }
                    if ("running".equals(kv[1])) {
                        running = kv[0];
                    }
                    if ("sleeping".equals(kv[1])) {
                        sleeping = kv[0];
                    }
                    if ("stopped".equals(kv[1])) {
                        stopped = kv[0];
                    }
                    if ("zombie".equals(kv[1])) {
                        zombie = kv[0];
                    }
                }
                try {
                    ret.tasksTotal = Integer.parseInt(total);
                } catch (Exception e) {
                }
                try {
                    ret.tasksRunning = Integer.parseInt(running);
                } catch (Exception e) {
                }
                try {
                    ret.tasksSleeping = Integer.parseInt(sleeping);
                } catch (Exception e) {
                }
                try {
                    ret.tasksStopped = Integer.parseInt(stopped);
                } catch (Exception e) {
                }
                try {
                    ret.tasksZombie = Integer.parseInt(zombie);
                } catch (Exception e) {
                }
            } else if (line.startsWith("%cpu(s):")) {
                String[] arr = line.split(":", 2);
                String val = arr[1].trim();
                String[] vals = val.split("\\s*,\\s*");
                String us = null;
                String sy = null;
                String ni = null;
                String id = null;
                String wa = null;
                String hi = null;
                String si = null;
                String st = null;
                for (String task : vals) {
                    String[] kv = task.trim().split("\\s+", 2);
                    if (kv.length != 2) {
                        continue;
                    }
                    if ("us".equals(kv[1])) {
                        us = kv[0];
                    }
                    if ("sy".equals(kv[1])) {
                        sy = kv[0];
                    }
                    if ("ni".equals(kv[1])) {
                        ni = kv[0];
                    }
                    if ("id".equals(kv[1])) {
                        id = kv[0];
                    }
                    if ("wa".equals(kv[1])) {
                        wa = kv[0];
                    }
                    if ("hi".equals(kv[1])) {
                        hi = kv[0];
                    }
                    if ("si".equals(kv[1])) {
                        si = kv[0];
                    }
                    if ("st".equals(kv[1])) {
                        st = kv[0];
                    }
                }
                try {
                    ret.cpuUs = Double.parseDouble(us);
                } catch (Exception e) {
                }
                try {
                    ret.cpuSy = Double.parseDouble(sy);
                } catch (Exception e) {
                }
                try {
                    ret.cpuNi = Double.parseDouble(ni);
                } catch (Exception e) {
                }
                try {
                    ret.cpuId = Double.parseDouble(id);
                } catch (Exception e) {
                }
                try {
                    ret.cpuWa = Double.parseDouble(wa);
                } catch (Exception e) {
                }
                try {
                    ret.cpuHi = Double.parseDouble(hi);
                } catch (Exception e) {
                }
                try {
                    ret.cpuSi = Double.parseDouble(si);
                } catch (Exception e) {
                }
                try {
                    ret.cpuSt = Double.parseDouble(st);
                } catch (Exception e) {
                }
            } else if (line.startsWith("kib mem :")) {
                String[] arr = line.split(":", 2);
                String val = arr[1].trim();
                String[] vals = val.split("\\s*,\\s*");
                String total = null;
                String free = null;
                String used = null;
                String buffCache = null;
                for (String task : vals) {
                    String[] kv = task.trim().split("\\s+", 2);
                    if (kv.length != 2) {
                        continue;
                    }
                    if ("total".equals(kv[1])) {
                        total = kv[0];
                    }
                    if ("free".equals(kv[1])) {
                        free = kv[0];
                    }
                    if ("used".equals(kv[1])) {
                        used = kv[0];
                    }
                    if ("buff/cache".equals(kv[1])) {
                        buffCache = kv[0];
                    }
                }
                try {
                    ret.kibMemTotal = Long.parseLong(total);
                } catch (Exception e) {
                }
                try {
                    ret.kibMemFree = Long.parseLong(free);
                } catch (Exception e) {
                }
                try {
                    ret.kibMemUsed = Long.parseLong(used);
                } catch (Exception e) {
                }
                try {
                    ret.kibMemBuffCache = Long.parseLong(buffCache);
                } catch (Exception e) {
                }
            } else if (line.startsWith("kib swap:")) {
                String[] arr = line.split(":", 2);
                String val = arr[1].trim();
                String[] vals = val.split("\\s*[,|\\.]\\s*");
                String total = null;
                String free = null;
                String used = null;
                String availMem = null;
                for (String task : vals) {
                    String[] kv = task.trim().split("\\s+", 2);
                    if (kv.length != 2) {
                        continue;
                    }
                    if ("total".equals(kv[1])) {
                        total = kv[0];
                    }
                    if ("free".equals(kv[1])) {
                        free = kv[0];
                    }
                    if ("used".equals(kv[1])) {
                        used = kv[0];
                    }
                    if ("avail mem".equals(kv[1])) {
                        availMem = kv[0];
                    }
                }
                try {
                    ret.kibSwapTotal = Long.parseLong(total);
                } catch (Exception e) {
                }
                try {
                    ret.kibSwapFree = Long.parseLong(free);
                } catch (Exception e) {
                }
                try {
                    ret.kibSwapUsed = Long.parseLong(used);
                } catch (Exception e) {
                }
                try {
                    ret.kibSwapAvailMem = Long.parseLong(availMem);
                } catch (Exception e) {
                }
            }

        }
        return ret;
    }

    public static LinuxFreeDto getLinuxFree() {
        if (!isLinux()) {
            return null;
        }
        String cmd = runCmd("free");
        return getLinuxFree(cmd);
    }

    public static LinuxFreeDto getLinuxFree(String cmd) {
        /**
         total        used        free      shared  buff/cache   available
         Mem:       16268044    13121288      225708      185272     2921048     2514952
         Swap:       8380412     3012444     5367968
         */
        if (cmd == null) {
            return null;
        }
        LinuxFreeDto ret = new LinuxFreeDto();
        String cmdRs = cmd.trim();
        String[] lines = cmdRs.split("\n");
        for (String item : lines) {
            String line = item.toLowerCase().trim();
            if (line.startsWith("mem:")) {
                String[] arr = line.split("\\s+");
                String total = arr.length >= 2 ? arr[1] : null;
                String used = arr.length >= 3 ? arr[2] : null;
                String free = arr.length >= 4 ? arr[3] : null;
                String shared = arr.length >= 5 ? arr[4] : null;
                String buffCache = arr.length >= 6 ? arr[5] : null;
                String available = arr.length >= 7 ? arr[6] : null;
                try {
                    ret.memTotal = Long.parseLong(total);
                } catch (Exception e) {

                }
                try {
                    ret.memUsed = Long.parseLong(used);
                } catch (Exception e) {

                }
                try {
                    ret.memFree = Long.parseLong(free);
                } catch (Exception e) {

                }
                try {
                    ret.memShared = Long.parseLong(shared);
                } catch (Exception e) {

                }
                try {
                    ret.memBuffCache = Long.parseLong(buffCache);
                } catch (Exception e) {

                }
                try {
                    ret.memAvailable = Long.parseLong(available);
                } catch (Exception e) {

                }

            } else if (line.startsWith("swap:")) {
                String[] arr = line.split("\\s+");
                String total = arr.length >= 2 ? arr[1] : null;
                String used = arr.length >= 3 ? arr[2] : null;
                String free = arr.length >= 4 ? arr[3] : null;

                try {
                    ret.swapTotal = Long.parseLong(total);
                } catch (Exception e) {

                }
                try {
                    ret.swapUsed = Long.parseLong(used);
                } catch (Exception e) {

                }
                try {
                    ret.swapFree = Long.parseLong(free);
                } catch (Exception e) {

                }
            }
        }
        return ret;
    }
}
