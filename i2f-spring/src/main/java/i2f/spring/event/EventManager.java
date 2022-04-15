package i2f.spring.event;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

/**
 * @author ltb
 * @date 2022/4/15 9:22
 * @desc
 */
public class EventManager {
    private ApplicationEventPublisher publisher;
    private ApplicationContext context;
    public EventManager(ApplicationContext context){
        this.context=context;
    }
    public EventManager(ApplicationEventPublisher publisher){
        this.publisher=publisher;
    }
    public void publish(Event event){
        publish(event);
    }
    public void publish(ApplicationEvent event){
        if(publisher!=null){
            publisher.publishEvent(event);
        }
        context.publishEvent(event);
    }
    public void publish(int code,String msg,Object data){
        publish(Event.builder(code, msg, data));
    }
    public void publish(int code,String msg){
        publish(Event.builder(code, msg));
    }
    public void publish(int code,Object data){
        publish(Event.builder(code,data));
    }
}
