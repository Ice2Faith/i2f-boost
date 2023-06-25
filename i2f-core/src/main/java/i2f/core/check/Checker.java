package i2f.core.check;


import i2f.core.annotations.remark.Author;
import i2f.core.annotations.remark.Usage;
import i2f.core.lang.functional.common.IFilter;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    public static ConcurrentHashMap<String, IFilter> validators=new ConcurrentHashMap<>();

    public static void registerValidator(String name, IFilter filter) {
        validators.put(name, filter);
    }

    public static void registerValidator(IFilter filter) {
        String cname = filter.getClass().getSimpleName();
        String name = cname.substring(0, 1).toLowerCase() + cname.substring(1);
        validators.put(name, filter);
    }
    public static IFilter getValidator(String validatorName){
        return validators.get(validatorName);
    }
    public static IFilter getValidator(Class<IFilter> validatorClass){
        String cname=validatorClass.getSimpleName();
        String name = cname.substring(0,1).toLowerCase()+cname.substring(1);
        return validators.get(name);
    }

    public static CheckWorker thr(){
        return new CheckWorker(true);
    }
    public static CheckWorker chk(){
        return new CheckWorker(false);
    }

    public static class CheckWorker{
        protected boolean thr=false;
        protected boolean ok=true;
        protected String message;
        protected Throwable ex;
        public CheckWorker(boolean thr){
            this.thr=thr;
        }

        public CheckWorker thr(){
            this.thr=true;
            return chk();
        }
        public boolean rs(){
            return ok;
        }
        public CheckWorker chk(){
            if(!ok){
                if(thr){
                    throw new CheckException(message,ex);
                }
            }
            return this;
        }
        public CheckWorker message(String message){
            this.message=message;
            return chk();
        }

        public CheckWorker isNull(Object obj){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isNull(obj)){
                this.ok=false;
                this.message="argument not allow is null!";
            }
            return chk();
        }
        public CheckWorker isNullMsg(Object obj,String message){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isNull(obj)){
                ok=false;
                this.message=message;
            }
            return chk();
        }
        public CheckWorker isExNull(Object ... objs){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExNull(objs)){
                this.ok=false;
                this.message="argument not allow is null!";
            }
            return chk();
        }
        public CheckWorker isExNullMsg(String message,Object ... objs){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExNull(objs)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }

        public CheckWorker isEmptyStr(String obj){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isEmptyStr(obj)){
                ok=false;
                this.message="argument not allow is empty!";
            }
            return chk();
        }
        public CheckWorker isEmptyStrMsg(String obj,String message){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isEmptyStr(obj)){
                ok=false;
                this.message=message;
            }
            return chk();
        }
        public CheckWorker isExEmptyStr(String ... objs){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExEmptyStr(objs)){
                ok=false;
                this.message="argument not allow is empty!";
            }
            return chk();
        }
        public CheckWorker isExEmptyStrMsg(String message,String ... objs){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExEmptyStr(objs)){
                ok=false;
                this.message=message;
            }
            return chk();
        }

        public CheckWorker isExTrue(boolean ... conds){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExTrue(conds)){
                ok=false;
                this.message="exists condition is true";
            }
            return chk();
        }
        public CheckWorker isExTrueMsg(String message,boolean ... conds){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExTrue(conds)){
                ok=false;
                this.message=message;
            }
            return chk();
        }

        public CheckWorker isExFalse(boolean ... conds){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExFalse(conds)){
                ok=false;
                this.message="exist condition is false";
            }
            return chk();
        }

        public CheckWorker isExFalseMsg(String message,boolean ... conds){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExFalse(conds)){
                ok=false;
                this.message=message;
            }
            return chk();
        }

        public CheckWorker isEmptyArray(Object obj){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isEmptyArray(obj)){
                ok=false;
                this.message="argument is empty array!";
            }
            return chk();
        }
        public CheckWorker isEmptyArrayMsg(Object obj,String message){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isEmptyArray(obj)){
                ok=false;
                this.message=message;
            }
            return chk();
        }
        public CheckWorker isExEmptyArray(Object ... objs){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExEmptyArray(objs)){
                ok=false;
                this.message="argument exists empty array!";
            }
            return chk();
        }
        public CheckWorker isExEmptyArrayMsg(String message,Object ... objs){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExEmptyArray(objs)){
                ok=false;
                this.message=message;
            }
            return chk();
        }

        public CheckWorker isEmptyCollection(Collection obj){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isEmptyCollection(obj)){
                ok=false;
                this.message="argument is empty collection!";
            }
            return chk();
        }
        public CheckWorker isEmptyCollectionMsg(Collection obj,String message){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isEmptyArray(obj)){
                ok=false;
                this.message=message;
            }
            return chk();
        }
        public CheckWorker isExEmptyCollection(Collection ... objs){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExEmptyCollection(objs)){
                ok=false;
                this.message="argument exists empty collection!";
            }
            return chk();
        }
        public CheckWorker isExEmptyCollectionMsg(String message,Collection ... objs){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExEmptyCollection(objs)){
                ok=false;
                this.message=message;
            }
            return chk();
        }

        public CheckWorker isEmptyMap(Map obj){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isEmptyMap(obj)){
                ok=false;
                this.message="argument is empty map!";
            }
            return chk();
        }
        public CheckWorker isEmptyMapMsg(Map obj,String message){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isEmptyMap(obj)){
                ok=false;
                this.message=message;
            }
            return chk();
        }
        public CheckWorker isExEmptyMap(Map ... objs){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExEmptyMap(objs)){
                ok=false;
                this.message="argument exists empty map!";
            }
            return chk();
        }
        public CheckWorker isExEmptyMapMsg(String message,Map ... objs){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExEmptyMap(objs)){
                ok=false;
                this.message=message;
            }
            return chk();
        }

        public CheckWorker isIn(Object tar,Object ... vals){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isIn(tar,vals)){
                this.ok=false;
                this.message="exist tar object in vals";
            }
            return chk();
        }
        public CheckWorker isInMsg(String message,Object tar,Object ... vals){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isIn(tar,vals)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }
        public CheckWorker notIn(Object tar,Object ... vals){
            if(!ok){
                return chk();
            }
            if(CheckUtil.notIn(tar,vals)){
                this.ok=false;
                this.message="exist tar object in vals";
            }
            return chk();
        }
        public CheckWorker notInMsg(String message,Object tar,Object ... vals){
            if(!ok){
                return chk();
            }
            if(CheckUtil.notIn(tar,vals)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }

        public CheckWorker isNumLower(Number targetNum,Number cmpNum){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isNumLower(targetNum, cmpNum)){
                this.ok=false;
                this.message="target number lower than compare number";
            }
            return chk();
        }
        public CheckWorker isNumLowerMsg(String message,Number targetNum,Number cmpNum){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isNumLower(targetNum, cmpNum)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }


        public CheckWorker isExNumLower(Number cmpNum,Number ... nums){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExNumLower(cmpNum, nums)){
                this.ok=false;
                this.message="nums number exists lower than compare number";
            }
            return chk();
        }
        public CheckWorker isExNumLowerMsg(String message,Number cmpNum,Number ... nums){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExNumLower(cmpNum, nums)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }

        public CheckWorker isNumGather(Number targetNum,Number cmpNum){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isNumGather(targetNum, cmpNum)){
                this.ok=false;
                this.message="target number gather than compare number";
            }
            return chk();
        }
        public CheckWorker isNumGatherMsg(String message,Number targetNum,Number cmpNum){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isNumGather(targetNum, cmpNum)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }


        public CheckWorker isExNumGather(Number cmpNum,Number ... nums){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExNumGather(cmpNum, nums)){
                this.ok=false;
                this.message="nums number exists gather than compare number";
            }
            return chk();
        }
        public CheckWorker isExNumGatherMsg(String message,Number cmpNum,Number ... nums){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExNumGather(cmpNum, nums)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }


        public CheckWorker isNumLowerEqu(Number targetNum,Number cmpNum){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isNumLowerEqu(targetNum, cmpNum)){
                this.ok=false;
                this.message="target number lower/equal than compare number";
            }
            return chk();
        }
        public CheckWorker isNumLowerEquMsg(String message,Number targetNum,Number cmpNum){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isNumLowerEqu(targetNum, cmpNum)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }


        public CheckWorker isExNumLowerEqu(Number cmpNum,Number ... nums){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExNumLowerEqu(cmpNum, nums)){
                this.ok=false;
                this.message="nums number exists lower/equal than compare number";
            }
            return chk();
        }
        public CheckWorker isExNumLowerEquMsg(String message,Number cmpNum,Number ... nums){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExNumLowerEqu(cmpNum, nums)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }

        public CheckWorker isNumGatherEqu(Number targetNum,Number cmpNum){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isNumGatherEqu(targetNum, cmpNum)){
                this.ok=false;
                this.message="target number gather/equal than compare number";
            }
            return chk();
        }
        public CheckWorker isNumGatherEquMsg(String message,Number targetNum,Number cmpNum){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isNumGatherEqu(targetNum, cmpNum)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }


        public CheckWorker isExNumGatherEqu(Number cmpNum,Number ... nums){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExNumGatherEqu(cmpNum, nums)){
                this.ok=false;
                this.message="nums number exists gather/equal than compare number";
            }
            return chk();
        }
        public CheckWorker isExNumGatherEquMsg(String message,Number cmpNum,Number ... nums){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExNumGatherEqu(cmpNum, nums)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }


        public CheckWorker isNumBetweenBoth(Number targetNum,Number min,Number max){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isNumBetweenBoth(targetNum, min,max)){
                this.ok=false;
                this.message="target number between [min,max]";
            }
            return chk();
        }
        public CheckWorker isNumBetweenBothMsg(String message,Number targetNum,Number min,Number max){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isNumBetweenBoth(targetNum, min,max)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }


        public CheckWorker isExNumBetweenBoth(Number min,Number max,Number ... nums){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExNumBetweenBoth(min,max, nums)){
                this.ok=false;
                this.message="nums number exists between [min,max]";
            }
            return chk();
        }
        public CheckWorker isExNumBetweenBothMsg(String message,Number min,Number max,Number ... nums){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExNumBetweenBoth(min,max, nums)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }


        public CheckWorker isNumBetweenLeft(Number targetNum,Number min,Number max){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isNumBetweenLeft(targetNum, min,max)){
                this.ok=false;
                this.message="target number between (min,max]";
            }
            return chk();
        }
        public CheckWorker isNumBetweenLeftMsg(String message,Number targetNum,Number min,Number max){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isNumBetweenLeft(targetNum, min,max)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }


        public CheckWorker isExNumBetweenLeft(Number min,Number max,Number ... nums){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExNumBetweenLeft(min,max, nums)){
                this.ok=false;
                this.message="nums number exists between (min,max]";
            }
            return chk();
        }
        public CheckWorker isExNumBetweenLeftMsg(String message,Number min,Number max,Number ... nums){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExNumBetweenLeft(min,max, nums)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }

        public CheckWorker isNumBetweenOpen(Number targetNum,Number min,Number max){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isNumBetweenOpen(targetNum, min,max)){
                this.ok=false;
                this.message="target number between (min,max)";
            }
            return chk();
        }
        public CheckWorker isNumBetweenOpenMsg(String message,Number targetNum,Number min,Number max){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isNumBetweenOpen(targetNum, min,max)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }


        public CheckWorker isExNumBetweenOpen(Number min,Number max,Number ... nums){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExNumBetweenOpen(min,max, nums)){
                this.ok=false;
                this.message="nums number exists between (min,max)";
            }
            return chk();
        }
        public CheckWorker isExNumBetweenOpenMsg(String message,Number min,Number max,Number ... nums){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExNumBetweenOpen(min,max, nums)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }

        public CheckWorker isExNumEqu(Number cmpNum,Number ... nums){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExNumEqu(cmpNum, nums)){
                this.ok=false;
                this.message="exists nums equal compare number";
            }
            return chk();
        }
        public CheckWorker isExNumEquMsg(String message,Number targetNum,Number min,Number max){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExNumEqu(targetNum, min,max)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }


        public CheckWorker isExNumNotEqu(Number cmpNum,Number ... nums){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExNumNotEqu(cmpNum, nums)){
                this.ok=false;
                this.message="exists nums not equal compare number";
            }
            return chk();
        }
        public CheckWorker isExNumNotEquMsg(String message,Number cmpNum,Number ... nums){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isExNumNotEqu(cmpNum, nums)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }

        public CheckWorker isMatched(String str,String regex){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isMatched(str,regex)){
                this.ok=false;
                this.message="string matched regex";
            }
            return chk();
        }
        public CheckWorker isMatchedMsg(String message,String str,String regex){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isMatched(str,regex)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }

        public CheckWorker notMatched(String str,String regex){
            if(!ok){
                return chk();
            }
            if(CheckUtil.notMatched(str,regex)){
                this.ok=false;
                this.message="string not matched regex";
            }
            return chk();
        }
        public CheckWorker notMatchedMsg(String message,String str,String regex){
            if(!ok){
                return chk();
            }
            if(CheckUtil.notMatched(str,regex)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }

        public CheckWorker isIntNumber(String str){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isIntNumber(str)){
                this.ok=false;
                this.message="string is int form";
            }
            return chk();
        }
        public CheckWorker isIntNumberMsg(String message,String str){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isIntNumber(str)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }

        public CheckWorker isFloatNumber(String str){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isFloatNumber(str)){
                this.ok=false;
                this.message="string is float form";
            }
            return chk();
        }
        public CheckWorker isFloatNumberMsg(String message,String str){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isFloatNumber(str)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }

        public CheckWorker isScientificNumber(String str){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isScientificNumber(str)){
                this.ok=false;
                this.message="string is scientific number form";
            }
            return chk();
        }
        public CheckWorker isScientificNumberMsg(String message,String str){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isScientificNumber(str)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }

        public CheckWorker isPhoneNumber(String str){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isPhoneNumber(str)){
                this.ok=false;
                this.message="string is phone number form";
            }
            return chk();
        }
        public CheckWorker isPhoneNumberMsg(String message,String str){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isPhoneNumber(str)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }

        public CheckWorker isEmailAddr(String str){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isEmailAddr(str)){
                this.ok=false;
                this.message="string is email form";
            }
            return chk();
        }
        public CheckWorker isEmailAddrMsg(String message,String str){
            if(!ok){
                return chk();
            }
            if(CheckUtil.isEmailAddr(str)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }

        public CheckWorker notIntNumber(String str){
            if(!ok){
                return chk();
            }
            if(CheckUtil.notIntNumber(str)){
                this.ok=false;
                this.message="string not is int form";
            }
            return chk();
        }
        public CheckWorker notIntNumberMsg(String message,String str){
            if(!ok){
                return chk();
            }
            if(CheckUtil.notIntNumber(str)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }

        public CheckWorker notFloatNumber(String str){
            if(!ok){
                return chk();
            }
            if(CheckUtil.notFloatNumber(str)){
                this.ok=false;
                this.message="string not is float form";
            }
            return chk();
        }
        public CheckWorker notFloatNumberMsg(String message,String str){
            if(!ok){
                return chk();
            }
            if(CheckUtil.notFloatNumber(str)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }

        public CheckWorker notScientificNumber(String str){
            if(!ok){
                return chk();
            }
            if(CheckUtil.notScientificNumber(str)){
                this.ok=false;
                this.message="string not is scientific number form";
            }
            return chk();
        }
        public CheckWorker notScientificNumberMsg(String message,String str){
            if(!ok){
                return chk();
            }
            if(CheckUtil.notScientificNumber(str)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }

        public CheckWorker notPhoneNumber(String str){
            if(!ok){
                return chk();
            }
            if(CheckUtil.notPhoneNumber(str)){
                this.ok=false;
                this.message="string not is phone number form";
            }
            return chk();
        }
        public CheckWorker notPhoneNumberMsg(String message,String str){
            if(!ok){
                return chk();
            }
            if(CheckUtil.notPhoneNumber(str)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }

        public CheckWorker notEmailAddr(String str){
            if(!ok){
                return chk();
            }
            if(CheckUtil.notEmailAddr(str)){
                this.ok=false;
                this.message="string not is email form";
            }
            return chk();
        }
        public CheckWorker notEmailAddrMsg(String message,String str){
            if(!ok){
                return chk();
            }
            if(CheckUtil.notEmailAddr(str)){
                this.ok=false;
                this.message=message;
            }
            return chk();
        }

        public CheckWorker except(Throwable ex){
            if(!ok){
                return chk();
            }
            this.ok=false;
            this.message=ex.getMessage();
            this.ex=ex;
            return chk();
        }
        public CheckWorker exceptMsg(String message,Throwable ex){
            if(!ok){
                return chk();
            }
            this.ok=false;
            this.message=message;
            this.ex=ex;
            return chk();
        }

        public CheckWorker validate(String validatorName,Object obj,String message){
            if (!ok) {
                return chk();
            }
            if (getValidator(validatorName).test(obj)) {
                this.ok = false;
                this.message = message;
            }
            return chk();
        }
        public CheckWorker validate(Class<IFilter> validatorClass,Object obj,String message){
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

}
