package i2f.core.check;


import i2f.core.annotations.remark.Author;
import i2f.core.annotations.remark.Usage;
import i2f.core.lang.functional.common.IFilter;
import i2f.core.lang.functional.jvf.BiSupplier;
import i2f.core.lang.functional.supplier.ISupplier2;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * @author ltb
 * @date 2022/3/15 8:33
 * @desc
 */
@Author("i2f")
@Usage({
        "boolean ok=Checker.chk().isNull(obj).isNumLower(num,12).rs();",
        "Checker.thr().isNullMsg(\"obj parameter can't be null\",obj);"
})
public class Checker {
    public static ConcurrentHashMap<String, IFilter> validators = new ConcurrentHashMap<>();

    public static void registerValidator(String name, IFilter filter) {
        validators.put(name, filter);
    }

    public static void registerValidator(IFilter filter) {
        String cname = filter.getClass().getSimpleName();
        String name = cname.substring(0, 1).toLowerCase() + cname.substring(1);
        validators.put(name, filter);
    }

    public static IFilter getValidator(String validatorName) {
        return validators.get(validatorName);
    }

    public static IFilter getValidator(Class<IFilter> validatorClass) {
        String cname = validatorClass.getSimpleName();
        String name = cname.substring(0, 1).toLowerCase() + cname.substring(1);
        return validators.get(name);
    }

    public static Checker begin(boolean thr) {
        return new Checker(thr);
    }

    public static Checker begin() {
        return new Checker(true);
    }

    protected boolean thr = false;
    protected boolean ok = true;
    protected String message;
    protected Throwable ex;
    protected ISupplier2<? extends RuntimeException, String, Throwable> thrower;

    public Checker(boolean thr) {
        this.thr = thr;
    }

    public Checker thr() {
        this.thr = true;
        return chk();
    }

    public Checker thr(ISupplier2<? extends RuntimeException, String, Throwable> thrower) {
        this.thrower = thrower;
        this.thr = true;
        return chk();
    }

    public boolean rs() {
        return ok;
    }

    public Checker chk() {
        if (!ok) {
            if (thr) {
                if (thrower != null) {
                    RuntimeException tmp = thrower.get(this.message, this.ex);
                    throw tmp;
                } else {
                    if (ex != null) {
                        throw new CheckException(message, ex);
                    } else {
                        throw new CheckException(message);
                    }
                }
            }
        }
        return this;
    }

    public Checker message(String message) {
        this.message = message;
        return chk();
    }

    public <T> Checker when(T obj, Predicate<T> errorPredicate, BiSupplier<String, T> errorSupplier) {
        if (!ok) {
            return chk();
        }
        if (errorPredicate.test(obj)) {
            this.ok = false;
            this.message = errorSupplier.apply(obj);
        }
        return chk();
    }

    public <T> Checker when(Predicate<T[]> errorPredicate, BiSupplier<String, T[]> errorSupplier, T... vals) {
        if (!ok) {
            return chk();
        }
        if (errorPredicate.test(vals)) {
            this.ok = false;
            this.message = errorSupplier.apply(vals);
        }
        return chk();
    }

    public Checker when(Predicate<boolean[]> errorPredicate, BiSupplier<String, boolean[]> errorSupplier, boolean... vals) {
        if (!ok) {
            return chk();
        }
        if (errorPredicate.test(vals)) {
            this.ok = false;
            this.message = errorSupplier.apply(vals);
        }
        return chk();
    }

    public Checker isNull(Object obj) {
        return when(obj, CheckUtil::isNull, (val) -> "argument not allow is null!");
    }

    public Checker isNullMsg(Object obj, String message) {
        return when(obj, CheckUtil::isNull, (val) -> message);
    }

    public Checker isExNull(Object... objs) {
        return when(CheckUtil::isExNull, (vals) -> "argument not allow is null!", objs);
    }

    public Checker isExNullMsg(String message, Object... objs) {
        return when(CheckUtil::isExNull, (vals) -> message, objs);
    }

    public Checker isEmptyStr(String obj) {
        return when(obj, CheckUtil::isEmptyStr, (val) -> "argument not allow is empty!");
    }

    public Checker isEmptyStrMsg(String obj, String message) {
        return when(obj, CheckUtil::isEmptyStr, (val) -> message);
    }

    public Checker isExEmptyStr(String... objs) {
        return when(CheckUtil::isExEmptyStr, (vals) -> "argument not allow is empty!", objs);
    }

    public Checker isExEmptyStrMsg(String message, String... objs) {
        return when(CheckUtil::isExEmptyStr, (vals) -> message, objs);
    }

    public Checker isExTrue(boolean... conds) {
        return when(CheckUtil::isExTrue, (vals) -> "exists condition is true", conds);
    }

    public Checker isExTrueMsg(String message, boolean... conds) {
        return when(CheckUtil::isExTrue, (vals) -> message, conds);
    }

    public Checker isExFalse(boolean... conds) {
        return when(CheckUtil::isExFalse, (vals) -> "exist condition is false", conds);
    }

    public Checker isExFalseMsg(String message, boolean... conds) {
        return when(CheckUtil::isExFalse, (vals) -> message, conds);
    }

    public Checker isEmptyArray(Object obj) {
        return when(obj, CheckUtil::isEmptyArray, (val) -> "argument is empty array!");
    }

    public Checker isEmptyArrayMsg(Object obj, String message) {
        return when(obj, CheckUtil::isEmptyArray, (val) -> message);
    }

    public Checker isExEmptyArray(Object... objs) {
        return when(CheckUtil::isExEmptyArray, (vals) -> "argument exists empty array!", objs);
    }

    public Checker isExEmptyArrayMsg(String message, Object... objs) {
        return when(CheckUtil::isExEmptyArray, (vals) -> message, objs);
    }

    public Checker isEmptyCollection(Collection obj) {
        return when(obj, CheckUtil::isEmptyCollection, (val) -> "argument is empty collection!");
    }

    public Checker isEmptyCollectionMsg(Collection obj, String message) {
        return when(obj, CheckUtil::isEmptyCollection, (val) -> message);
    }

    public Checker isExEmptyCollection(Collection... objs) {
        return when(CheckUtil::isExEmptyCollection, (vals) -> "argument exists empty collection!", objs);
    }

    public Checker isExEmptyCollectionMsg(String message, Collection... objs) {
        return when(CheckUtil::isExEmptyCollection, (vals) -> message, objs);
    }

    public Checker isEmptyMap(Map obj) {
        return when(obj, CheckUtil::isEmptyMap, (val) -> "argument is empty map!");
    }

    public Checker isEmptyMapMsg(Map obj, String message) {
        return when(obj, CheckUtil::isEmptyMap, (val) -> message);
    }

    public Checker isExEmptyMap(Map... objs) {
        return when(CheckUtil::isExEmptyMap, (vals) -> "argument exists empty map!", objs);
    }

    public Checker isExEmptyMapMsg(String message, Map... objs) {
        return when(CheckUtil::isExEmptyMap, (vals) -> message, objs);
    }

    public Checker isIn(Object tar, Object... vals) {
        return when(tar, (val) -> CheckUtil.isIn(val, vals), (val) -> "exist tar object in vals");
    }

    public Checker isInMsg(String message, Object tar, Object... vals) {
        return when(tar, (val) -> CheckUtil.isIn(val, vals), (val) -> message);
    }

    public Checker notIn(Object tar, Object... vals) {
        return when(tar, (val) -> CheckUtil.notIn(val, vals), (val) -> "exist tar object in vals");
    }

    public Checker notInMsg(String message, Object tar, Object... vals) {
        return when(tar, (val) -> CheckUtil.notIn(val, vals), (val) -> message);
    }

    public Checker isNumLower(Number targetNum, Number cmpNum) {
        return when(targetNum, (val) -> CheckUtil.isNumLower(val, cmpNum), (val) -> "target number lower than compare number");
    }

    public Checker isNumLowerMsg(String message, Number targetNum, Number cmpNum) {
        return when(targetNum, (val) -> CheckUtil.isNumLower(val, cmpNum), (val) -> message);
    }


    public Checker isExNumLower(Number cmpNum, Number... nums) {
        return when((vals) -> CheckUtil.isExNumLower(cmpNum, vals), (vals) -> "nums number exists lower than compare number", nums);
    }

    public Checker isExNumLowerMsg(String message, Number cmpNum, Number... nums) {
        return when((vals) -> CheckUtil.isExNumLower(cmpNum, vals), (vals) -> message, nums);
    }

    public Checker isNumGather(Number targetNum, Number cmpNum) {
        return when(targetNum, (val) -> CheckUtil.isNumGather(val, cmpNum), (val) -> "target number gather than compare number");
    }

    public Checker isNumGatherMsg(String message, Number targetNum, Number cmpNum) {
        return when(targetNum, (val) -> CheckUtil.isNumGather(val, cmpNum), (val) -> message);
    }


    public Checker isExNumGather(Number cmpNum, Number... nums) {
        return when((vals) -> CheckUtil.isExNumGather(cmpNum, vals), (vals) -> "nums number exists gather than compare number", nums);
    }

    public Checker isExNumGatherMsg(String message, Number cmpNum, Number... nums) {
        return when((vals) -> CheckUtil.isExNumGather(cmpNum, vals), (vals) -> message, nums);
    }


    public Checker isNumLowerEqu(Number targetNum, Number cmpNum) {
        return when(targetNum, (val) -> CheckUtil.isNumLowerEqu(val, cmpNum), (val) -> "target number lower/equal than compare number");
    }

    public Checker isNumLowerEquMsg(String message, Number targetNum, Number cmpNum) {
        return when(targetNum, (val) -> CheckUtil.isNumLowerEqu(val, cmpNum), (val) -> message);
    }


    public Checker isExNumLowerEqu(Number cmpNum, Number... nums) {
        return when((vals) -> CheckUtil.isExNumLowerEqu(cmpNum, vals), (vals) -> "nums number exists lower/equal than compare number", nums);
    }

    public Checker isExNumLowerEquMsg(String message, Number cmpNum, Number... nums) {
        return when((vals) -> CheckUtil.isExNumLowerEqu(cmpNum, vals), (vals) -> message, nums);
    }

    public Checker isNumGatherEqu(Number targetNum, Number cmpNum) {
        return when(targetNum, (val) -> CheckUtil.isNumGatherEqu(val, cmpNum), (val) -> "target number gather/equal than compare number");
    }

    public Checker isNumGatherEquMsg(String message, Number targetNum, Number cmpNum) {
        return when(targetNum, (val) -> CheckUtil.isNumGatherEqu(val, cmpNum), (val) -> message);
    }


    public Checker isExNumGatherEqu(Number cmpNum, Number... nums) {
        return when((vals) -> CheckUtil.isExNumGatherEqu(cmpNum, vals), (vals) -> "nums number exists gather/equal than compare number", nums);
    }

    public Checker isExNumGatherEquMsg(String message, Number cmpNum, Number... nums) {
        return when((vals) -> CheckUtil.isExNumGatherEqu(cmpNum, vals), (vals) -> message, nums);
    }


    public Checker isNumBetweenBoth(Number targetNum, Number min, Number max) {
        return when(targetNum, (val) -> CheckUtil.isNumBetweenBoth(val, min, max), (val) -> "target number between [min,max]");
    }

    public Checker isNumBetweenBothMsg(String message, Number targetNum, Number min, Number max) {
        return when(targetNum, (val) -> CheckUtil.isNumBetweenBoth(val, min, max), (val) -> message);
    }


    public Checker isExNumBetweenBoth(Number min, Number max, Number... nums) {
        return when((vals) -> CheckUtil.isExNumBetweenBoth(min, max, vals), (vals) -> "nums number exists between [min,max]", nums);
    }

    public Checker isExNumBetweenBothMsg(String message, Number min, Number max, Number... nums) {
        return when((vals) -> CheckUtil.isExNumBetweenBoth(min, max, vals), (vals) -> message, nums);
    }


    public Checker isNumBetweenLeft(Number targetNum, Number min, Number max) {
        return when(targetNum, (val) -> CheckUtil.isNumBetweenLeft(val, min, max), (val) -> "target number between (min,max]");
    }

    public Checker isNumBetweenLeftMsg(String message, Number targetNum, Number min, Number max) {
        return when(targetNum, (val) -> CheckUtil.isNumBetweenLeft(val, min, max), (val) -> message);
    }


    public Checker isExNumBetweenLeft(Number min, Number max, Number... nums) {
        return when((vals) -> CheckUtil.isExNumBetweenLeft(min, max, vals), (vals) -> "nums number exists between (min,max]", nums);
    }

    public Checker isExNumBetweenLeftMsg(String message, Number min, Number max, Number... nums) {
        return when((vals) -> CheckUtil.isExNumBetweenLeft(min, max, vals), (vals) -> message, nums);
    }

    public Checker isNumBetweenOpen(Number targetNum, Number min, Number max) {
        return when(targetNum, (val) -> CheckUtil.isNumBetweenOpen(val, min, max), (val) -> "target number between (min,max)");
    }

    public Checker isNumBetweenOpenMsg(String message, Number targetNum, Number min, Number max) {
        return when(targetNum, (val) -> CheckUtil.isNumBetweenOpen(val, min, max), (val) -> message);
    }


    public Checker isExNumBetweenOpen(Number min, Number max, Number... nums) {
        return when((vals) -> CheckUtil.isExNumBetweenOpen(min, max, vals), (vals) -> "nums number exists between (min,max)", nums);
    }

    public Checker isExNumBetweenOpenMsg(String message, Number min, Number max, Number... nums) {
        return when((vals) -> CheckUtil.isExNumBetweenOpen(min, max, vals), (vals) -> message, nums);
    }

    public Checker isExNumEqu(Number cmpNum, Number... nums) {
        return when((vals) -> CheckUtil.isExNumEqu(cmpNum, vals), (vals) -> "exists nums equal compare number", nums);
    }

    public Checker isExNumEquMsg(String message, Number cmpNum, Number... nums) {
        return when((vals) -> CheckUtil.isExNumEqu(cmpNum, vals), (vals) -> message, nums);
    }


    public Checker isExNumNotEqu(Number cmpNum, Number... nums) {
        return when((vals) -> CheckUtil.isExNumNotEqu(cmpNum, vals), (vals) -> "exists nums not equal compare number", nums);
    }

    public Checker isExNumNotEquMsg(String message, Number cmpNum, Number... nums) {
        return when((vals) -> CheckUtil.isExNumNotEqu(cmpNum, vals), (vals) -> message, nums);
    }

    public Checker isMatched(String str, String regex) {
        return when(str, (val) -> CheckUtil.isMatched(val, regex), (val) -> "string matched regex");
    }

    public Checker isMatchedMsg(String message, String str, String regex) {
        return when(str, (val) -> CheckUtil.isMatched(val, regex), (val) -> message);
    }

    public Checker notMatched(String str, String regex) {
        return when(str, (val) -> CheckUtil.notMatched(val, regex), (val) -> "string not matched regex");
    }

    public Checker notMatchedMsg(String message, String str, String regex) {
        return when(str, (val) -> CheckUtil.notMatched(val, regex), (val) -> message);
    }

    public Checker isIntNumber(String str) {
        return when(str, CheckUtil::isIntNumber, (val) -> "string is int form");
    }

    public Checker isIntNumberMsg(String message, String str) {
        return when(str, CheckUtil::isIntNumber, (val) -> message);
    }

    public Checker isFloatNumber(String str) {
        return when(str, CheckUtil::isFloatNumber, (val) -> "string is float form");
    }

    public Checker isFloatNumberMsg(String message, String str) {
        return when(str, CheckUtil::isFloatNumber, (val) -> message);
    }

    public Checker isScientificNumber(String str) {
        return when(str, CheckUtil::isScientificNumber, (val) -> "string is scientific number form");
    }

    public Checker isScientificNumberMsg(String message, String str) {
        return when(str, CheckUtil::isScientificNumber, (val) -> message);
    }

    public Checker isPhoneNumber(String str) {
        return when(str, CheckUtil::isPhoneNumber, (val) -> "string is phone number form");
    }

    public Checker isPhoneNumberMsg(String message, String str) {
        return when(str, CheckUtil::isPhoneNumber, (val) -> message);
    }

    public Checker isEmailAddr(String str) {
        return when(str, CheckUtil::isEmailAddr, (val) -> "string is email form");
    }

    public Checker isEmailAddrMsg(String message, String str) {
        return when(str, CheckUtil::isEmailAddr, (val) -> message);
    }

    public Checker notIntNumber(String str) {
        return when(str, CheckUtil::notIntNumber, (val) -> "string not is int form");
    }

    public Checker notIntNumberMsg(String message, String str) {
        return when(str, CheckUtil::notIntNumber, (val) -> message);
    }

    public Checker notFloatNumber(String str) {
        return when(str, CheckUtil::notFloatNumber, (val) -> "string not is float form");
    }

    public Checker notFloatNumberMsg(String message, String str) {
        return when(str, CheckUtil::notFloatNumber, (val) -> message);
    }

    public Checker notScientificNumber(String str) {
        return when(str, CheckUtil::notScientificNumber, (val) -> "string not is scientific number form");
    }

    public Checker notScientificNumberMsg(String message, String str) {
        return when(str, CheckUtil::notScientificNumber, (val) -> message);
    }

    public Checker notPhoneNumber(String str) {
        return when(str, CheckUtil::notPhoneNumber, (val) -> "string not is phone number form");
    }

    public Checker notPhoneNumberMsg(String message, String str) {
        return when(str, CheckUtil::notPhoneNumber, (val) -> message);
    }

    public Checker notEmailAddr(String str) {
        return when(str, CheckUtil::notEmailAddr, (val) -> "string not is email form");
    }

    public Checker notEmailAddrMsg(String message, String str) {
        return when(str, CheckUtil::notEmailAddr, (val) -> message);
    }

    public Checker except(Throwable ex) {
        if (!ok) {
            return chk();
        }
        this.ok = false;
        this.message = ex.getMessage();
        this.ex = ex;
        return chk();
    }

    public Checker exceptMsg(String message, Throwable ex) {
        if (!ok) {
            return chk();
        }
        this.ok = false;
        this.message = message;
        this.ex = ex;
        return chk();
    }

    public Checker validate(String validatorName, Object obj, String message) {
        if (!ok) {
            return chk();
        }
        if (getValidator(validatorName).test(obj)) {
            this.ok = false;
            this.message = message;
        }
        return chk();
    }

    public Checker validate(Class<IFilter> validatorClass, Object obj, String message) {
        if (!ok) {
            return chk();
        }
        if (getValidator(validatorClass).test(obj)) {
            this.ok = false;
            this.message = message;
        }
        return chk();
    }


}
