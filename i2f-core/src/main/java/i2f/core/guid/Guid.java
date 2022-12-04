package i2f.core.guid;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;


public class Guid {
    private static GuidSequence IDENTIFIER_GENERATOR = new GuidSequence();
    public static final DateTimeFormatter MILLISECOND = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    public Guid() {
    }

    public static long getId() {
        return getId((Object) null);
    }

    public static long getId(Object entity) {
        return IDENTIFIER_GENERATOR.nextId();
    }

    public static String getIdStr() {
        return getIdStr((Object) null);
    }

    public static String getIdStr(Object entity) {
        return String.valueOf(IDENTIFIER_GENERATOR.nextId());
    }

    public static String getMillisecond() {
        return LocalDateTime.now().format(MILLISECOND);
    }

    public static String getTimeId() {
        return getMillisecond() + getIdStr();
    }

    public static String get32UUID() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return (new UUID(random.nextLong(), random.nextLong())).toString().replace("-", "");
    }
}
