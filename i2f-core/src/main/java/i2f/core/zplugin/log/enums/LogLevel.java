package i2f.core.zplugin.log.enums;

/**
 * @author ltb
 * @date 2022/3/28 16:58
 * @desc
 */
public enum LogLevel {
    OFF(0),FATAL(1),ERROR(2),WARN(3),INFO(4),DEBUG(5),TRACE(6),ALL(99);
    private int level;
    private LogLevel(int level){
        this.level=level;
    }
    public int level(){
        return this.level;
    }

    public static LogLevel parse(String lvl){
        if(lvl==null || "".equals(lvl)){
            return OFF;
        }
        lvl=lvl.trim().toUpperCase();
        if("OFF".equals(lvl)){
            return OFF;
        } else if("FATAL".equals(lvl)){
            return FATAL;
        } else if("ERROR".equals(lvl)){
            return ERROR;
        } else if("WARN".equals(lvl)){
            return WARN;
        } else if("INFO".equals(lvl)){
            return INFO;
        } else if("DEBUG".equals(lvl)){
            return DEBUG;
        } else if("TRACE".equals(lvl)){
            return TRACE;
        } else if("ALL".equals(lvl)){
            return ALL;
        }
        return OFF;
    }
}
