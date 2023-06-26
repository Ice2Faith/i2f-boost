package i2f.core.io.file;

import i2f.core.container.array.ArrayUtil;
import i2f.core.io.stream.StreamUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023/6/21 9:58
 * @desc
 */
public class FileTypeUtil {
    public static boolean isType(File file, FileType type) throws IOException {
        byte[] magicHeader = type.getMagicNumber();
        byte[] fileHeader = StreamUtil.readBytes(file, type.getJumpBytes(), magicHeader.length);
        int rs = ArrayUtil.compare(magicHeader, fileHeader);
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
            if (ArrayUtil.compare(magicHeader, item.getMagicNumber()) == 0) {
                return item;
            }
        }

        return FileType.UNKNOWN;
    }

    public static List<FileType> matchTypes(File file) throws IOException {
        List<FileType> ret = new ArrayList<>();

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
            if (ArrayUtil.compare(magicHeader, item.getMagicNumber()) == 0) {
                ret.add(item);
            }
        }

        return ret;
    }

}
