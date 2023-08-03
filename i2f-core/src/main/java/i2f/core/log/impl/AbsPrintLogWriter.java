package i2f.core.log.impl;

import i2f.core.check.CheckUtil;
import i2f.core.log.consts.LogLevel;
import i2f.core.log.core.ILogWriter;
import i2f.core.log.data.LogDto;

import java.text.SimpleDateFormat;

/**
 * @author Ice2Faith
 * @date 2023/8/2 10:38
 * @desc
 */

public abstract class AbsPrintLogWriter implements ILogWriter {
    protected ThreadLocal<SimpleDateFormat> sfmt = ThreadLocal.withInitial(() -> {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
    });

    @Override
    public void write(LogDto dto) {

        StringBuilder builder = new StringBuilder();
        builder.append(dto.getContent()).append("\n");
        builder.append(sfmt.get().format(dto.getTime()))
                .append(" ")
                .append("[").append(dto.getThread()).append("]")
                .append(" ")
                .append("[").append(String.format("%5s", String.valueOf(dto.getLevel()))).append("]")
                .append(" ")
                .append(dto.getLocation())
                .append(" : ")
                .append(dto.getContent())
                .append("\n");
        builder.append("\t")
                .append("[").append(dto.getSystem()).append("]")
                .append(" ")
                .append("[").append(dto.getModule()).append("]")
                .append(" ")
                .append("[").append(dto.getLabel()).append("]")
                .append(" ")
                .append("[").append(dto.getSource()).append("]")
                .append("\n");


        if (!CheckUtil.isEmptyStr(dto.getMethod())) {
            builder.append("\t")
                    .append("@")
                    .append(dto.getClazz())
                    .append("\n")
                    .append("\t")
                    .append("call")
                    .append(" ").append(dto.getMethod())
                    .append("\n");
        }


        if (dto.getUseMillSeconds() >= 0) {
            builder.append("\t")
                    .append("useMillSeconds")
                    .append(" ").append(dto.getUseMillSeconds())
                    .append("\n");
        }


        if (!CheckUtil.isEmptyStr(dto.getExceptClass())) {
            builder.append("\t")
                    .append("exception")
                    .append(" ").append(dto.getExceptClass())
                    .append(" : ").append(dto.getExceptMsg())
                    .append("\n")
                    .append(dto.getExceptStack())
                    .append("\n");
        }

        if(!CheckUtil.isEmptyStr(dto.getArgs())){
            builder.append("\t")
                    .append("args")
                    .append(" ").append(dto.getArgs())
                    .append("\n");
        }

        if(!CheckUtil.isEmptyStr(dto.getRet())){
            builder.append("\t")
                    .append("ret")
                    .append(" ").append(dto.getRet())
                    .append("\n");
        }

        String str = builder.toString();

        print(str,dto.getLevel(),dto);
    }

    public abstract void print(String str,LogLevel level,LogDto dto);
}
