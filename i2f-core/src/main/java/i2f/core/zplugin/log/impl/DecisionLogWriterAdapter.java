package i2f.core.zplugin.log.impl;

import i2f.core.zplugin.log.ILogWriter;
import i2f.core.zplugin.log.context.LogLevelMappingHolder;
import i2f.core.zplugin.log.controls.LogDecision;
import i2f.core.zplugin.log.data.LogData;

/**
 * @author ltb
 * @date 2022/4/1 16:51
 * @desc
 */
public class DecisionLogWriterAdapter implements ILogWriter {
    private ILogWriter writer;
    private LogDecision decision;
    public DecisionLogWriterAdapter(ILogWriter writer){
        this.writer=writer;
        this.decision= LogLevelMappingHolder.getDecision();
    }
    public DecisionLogWriterAdapter(ILogWriter writer, LogDecision decision){
        this.writer=writer;
        this.decision= decision;
    }
    @Override
    public void write(LogData data) {
        if(decision.decision(data)){
            writer.write(data);
        }
    }
}
