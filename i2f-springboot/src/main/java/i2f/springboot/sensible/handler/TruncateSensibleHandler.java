package i2f.springboot.sensible.handler;


import i2f.springboot.sensible.ISensibleHandler;
import i2f.springboot.sensible.Sensible;
import i2f.springboot.sensible.SensibleType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TruncateSensibleHandler implements ISensibleHandler {
    @Override
    public Set<String> accept() {
        return new HashSet<>(Arrays.asList(
                SensibleType.PHONE,
                SensibleType.EMAIL,
                SensibleType.ID_CARD,
                SensibleType.PASSWORD,
                SensibleType.SEQUENCE,
                SensibleType.TRUNC
        ));
    }

    @Override
    public Set<Class<?>> type() {
        return new HashSet<>(Arrays.asList(String.class));
    }

    @Override
    public Object handle(Object obj, Sensible ann) {
        if (obj == null) {
            return obj;
        }
        String str = (String) obj;
        String type = ann.type();
        if (type.equals(SensibleType.PHONE)) {
            return str.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        } else if (type.equals(SensibleType.EMAIL)) {
            return str.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4");
        } else if (type.equals(SensibleType.ID_CARD)) {
            return str.replaceAll("(\\d{4})\\d{10}(\\w{4})", "$1*****$2");
        } else if (type.equals(SensibleType.PASSWORD)) {
            return "******";
        } else if (type.equals(SensibleType.SEQUENCE)) {
            return str.replaceAll("(\\d{1})\\d+(\\d{2})", "$1****$2");
        }
        return hide(str, ann.prefix(), ann.suffix(), ann.fill());
    }

    public String hide(String str, int prefix, int suffix, String fill) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0, n = str.length(); i < n; i++) {
            if (i < prefix) {
                sb.append(str.charAt(i));
                continue;
            }
            if (i > (n - suffix - 1)) {
                sb.append(str.charAt(i));
                continue;
            }
            sb.append(fill);
        }
        return sb.toString();
    }
}
