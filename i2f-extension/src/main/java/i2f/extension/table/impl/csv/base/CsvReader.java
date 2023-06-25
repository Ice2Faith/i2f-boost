package i2f.extension.table.impl.csv.base;

import i2f.core.generate.RegexGenerator;
import i2f.core.type.str.Strings;
import i2f.core.type.str.data.RegexFindPartMeta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023/5/30 16:42
 * @desc
 */
public class CsvReader {

    public static List<List<Object>> read(InputStream is,
                                          String charset) throws IOException {
        List<List<Object>> ret = new LinkedList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset));
        String line = null;
        while ((line = reader.readLine()) != null) {
            List<RegexFindPartMeta> items = Strings.regexFindParts(line, RegexGenerator.QUOTE_STRING_REGEX);
            String rep = "";
            List<String> matches = new LinkedList<>();
            for (RegexFindPartMeta item : items) {
                if (item.isMatch) {
                    rep += "\n";
                    matches.add(item.part);
                } else {
                    rep += item.part;
                }
            }

            String[] arr = rep.split(",");

            List<Object> row = new LinkedList<>();
            Iterator<String> iter = matches.iterator();
            for (int i = 0; i < arr.length; i++) {
                String pstr = arr[i].trim();
                if (arr[i].indexOf("\n") >= 0) {
                    pstr = iter.next();
                }
                Object val = pstr;
                if (pstr.startsWith("\"")) {
                    try {
                        val = CsvUtils.mapper.readValue(pstr, String.class);
                    } catch (Exception e) {
                    }
                }
                row.add(val);
            }

            ret.add(row);
        }
        reader.close();
        return ret;
    }
}
