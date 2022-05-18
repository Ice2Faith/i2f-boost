package test.match;

import i2f.core.match.impl.AntMatcher;

/**
 * @author ltb
 * @date 2022/4/26 9:32
 * @desc
 */
public class TestAntMatcher {

    public static void main(String[] args){
        String str="com.newland.modules.machine.controller.MachineController";
        ivk(str,"com.*.modules.**.*Controller",".");

        ivk(str,"*.newland.**.controller.Machine*",".");

        ivk(str,"com.new*.**.Machine*er",".");

        ivk(str,"c*m.newland.**.service.*Service*","sep");

        ivk("auth/user/findUserDetail","auth/*/find*","/");

        ivk("auth/user/detail/findUserDetail","auth/**/find*","/");

        ivk("auth/user/findUserDetail","auth/*/find*il","/");

        ivk("auth/user/detail/findUserDetail","auth/**/find*Detail","/");

        ivk("auth/user/detail/findUserDetail","auth/user/detail/findUserDetail*","/");
        ivk("auth/user/detail/findUserDetail","auth/user/detail/*findUserDetail","/");
        ivk("auth/user/detail/findUserDetail","auth/user/de*tail/findUserDetail","/");

        ivk("auth/user/lo*in","auth/user/lo\\*in","/");
        ivk("auth/u?er/lo*in","auth/u\\?er/lo\\*in","/");
    }

    public static void ivk(String str,String patten,String sep){
        AntMatcher matcher=new AntMatcher(sep);
        System.out.println("-------------------------------");
        System.out.println("str\t:"+str);
        System.out.println("ptn\t:"+patten);
        System.out.println("sep\t:"+sep);
        double ret= matcher.match(str,patten);
        System.out.println("mth\t:"+ret);
    }
}
