package i2f.extension.quartz.driven.enums;

/**
 * @author ltb
 * @date 2022/4/18 14:42
 * @desc
 */
public enum ScheduleType {
    Interval("Interval"),Cron("Cron");
    private String value;
    private ScheduleType(String value){
        this.value=value;
    }
    public String value(){
        return value;
    }
    public static ScheduleType parse(String type){
        if(type==null){
            type="";
        }
        type=type.trim().toLowerCase();
        if("interval".equals(type)){
            return Interval;
        }else if("cron".equals(type)){
            return Cron;
        }
        return Interval;
    }

}
