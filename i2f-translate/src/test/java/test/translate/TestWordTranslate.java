package test.translate;

import i2f.core.type.tuple.impl.Tuple2;
import i2f.translate.impl.WordTranslator;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023/5/13 15:41
 * @desc
 */
public class TestWordTranslate {
    public static void main(String[] args) throws Exception {
        String str = "Hello,this is an test for split an string to letters,which own bill's project,and it's support camelMergeLetter,PascalMergeLetter,underscore_merge_letter,snack-merge-letter.";
        List<Tuple2<String, Boolean>> rs = WordTranslator.splitLetter(str, true, false);
        System.out.println(rs);
        String cn = WordTranslator.en2cn(str, true);
        System.out.println(cn);

        System.out.println(WordTranslator.en2cn("createUserName", true));
        System.out.println(WordTranslator.en2cn("createBy", true));
        System.out.println(WordTranslator.en2cn("sys_user", true));
        System.out.println(WordTranslator.en2cn("patrol_task_order", true));

    }
}
