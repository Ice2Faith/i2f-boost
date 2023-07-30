package com.i2f.demo.console.drools;

import org.kie.api.KieBase;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023/7/30 19:56
 * @desc
 */
public class DroolsUtil {
    public static KieBase getBaseByDrl(String drl){
        KieHelper helper=new KieHelper();
        helper.addContent(drl, ResourceType.DRL);
        Results results = helper.verify();
        if(results.hasMessages(Message.Level.WARNING,Message.Level.ERROR)){
            StringBuilder builder=new StringBuilder();
            builder.append("drools error:").append("\n");
            List<Message> messages = results.getMessages(Message.Level.WARNING, Message.Level.ERROR);
            for (Message message : messages) {
                builder.append(message.getLevel()).append(" ").append(message.getPath()).append(":").append(message.getLine()).append("\n")
                        .append("\t msg: ").append(message.getText()).append("\n");
            }
            throw new IllegalStateException(builder.toString());
        }
        return helper.build();
    }
    public static KieSession getSessionByDrl(String drl){
        return getBaseByDrl(drl).newKieSession();
    }
}
