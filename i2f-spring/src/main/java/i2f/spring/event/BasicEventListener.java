package i2f.spring.event;

import org.springframework.context.ApplicationListener;

/**
 * @author ltb
 * @date 2022/4/15 9:46
 * @desc
 */
public abstract class BasicEventListener<T extends Event> implements ApplicationListener<T> {
    @Override
    final public void onApplicationEvent(T event) {
        handle(event.getCode(), event.getMsg(), event.getData(),event);
    }

    public abstract void handle(int code,String msg,Object data,T event);
}
