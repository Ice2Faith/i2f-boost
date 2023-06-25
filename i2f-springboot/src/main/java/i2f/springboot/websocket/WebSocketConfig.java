package i2f.springboot.websocket;

import i2f.core.reflection.reflect.core.ReflectResolver;
import i2f.spring.context.SpringUtil;
import i2f.spring.environment.EnvironmentUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.Map;

/**
 * @author ltb
 * @date 2022/2/25 8:48
 * @desc
 * https://www.cnblogs.com/xuwenjin/p/12664650.html
 *
 * https://blog.csdn.net/java_mindmap/article/details/105898152
 */
@ConditionalOnExpression("${i2f.springboot.config.websocket.enable:true}")
@Slf4j
@Data
@NoArgsConstructor
@EnableWebSocket
@Configuration
@ConfigurationProperties(prefix = "i2f.springboot.config.websocket")
public class WebSocketConfig implements WebSocketConfigurer {
    /**
     * 注入一个ServerEndpointExporter,该Bean会自动注册使用@ServerEndpoint注解申明的websocket endpoint
     * 提供给传统方式使用
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        log.info("websocket config done.");
        return new ServerEndpointExporter();
    }

    /**
     * 实现 WebSocketConfigurer 接口，使用springboot的方式注册
     * @param registry
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        Map<String,Map<String,Object>> map=getRegistryWebsocketConfigs(environment);

        for(Map.Entry<String,Map<String,Object>> entry : map.entrySet()){
            Map<String,Object> item= entry.getValue();
            String useContextBean=(String)item.get("use-context-bean");

            boolean useBean=false;
            if(useContextBean!=null && !"".equals(useContextBean)){
                try{
                    useBean=Boolean.parseBoolean(useContextBean);
                }catch(Exception e){

                }
            }

            String handlerName=(String)item.get("handler");
            String interceptorName=(String)item.get("interceptor");
            String path=(String)item.get("path");
            String allowOrigin=(String)item.get("allow-origin");

            if(allowOrigin==null || "".equals(allowOrigin)){
                allowOrigin="*";
            }
            if(path==null || "".equals(path)){
                log.warn("websocket registry bad find path:"+path+" in id:"+entry.getKey());
                continue;
            }

            if(handlerName==null || "".equals(handlerName)){
                log.warn("websocket registry bad find handler-name:"+handlerName+" in id:"+entry.getKey());
                continue;
            }

            if(interceptorName==null || "".equals(interceptorName)){
                log.warn("websocket registry bad find interceptor-name:"+interceptorName+" in id:"+entry.getKey());
                continue;
            }

            WebSocketHandler handler=null;
            HandshakeInterceptor interceptor=null;
            if(useBean){
                handler= SpringUtil.getBean(handlerName,WebSocketHandler.class);
                interceptor= SpringUtil.getBean(interceptorName,HandshakeInterceptor.class);
            }else{
                Class handlerClass= ReflectResolver.getClazz(handlerName);
                Class interceptorClass=ReflectResolver.getClazz(interceptorName);

                handler=(WebSocketHandler) ReflectResolver.instance(handlerClass);
                interceptor=(HandshakeInterceptor)ReflectResolver.instance(interceptorClass);
            }

            if(handler==null){
                log.warn("websocket handler not found bean with name:"+handlerName+" in id:"+entry.getKey());
                continue;
            }

            if(interceptor==null){
                log.warn("websocket interceptor not found bean with name:"+interceptorName+" in id:"+entry.getKey());
            }

            registry.addHandler(handler,path)
                    .setAllowedOrigins(allowOrigin)
                    .addInterceptors(interceptor);
            log.info("websocket registry find:"+handlerName+" with path:"+path);
        }
    }

    @Autowired
    private Environment environment;

    public static final String REGISTRY_WEBSOCKET_PREFIX="i2f.springboot.config.websocket.registry.";

    public static Map<String, Map<String,Object>> getRegistryWebsocketConfigs(Environment env){
        return EnvironmentUtil.of(env).getGroupMapConfigs(REGISTRY_WEBSOCKET_PREFIX);
    }

}
