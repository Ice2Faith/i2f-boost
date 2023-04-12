package i2f.extension.zookeeper.cluster;

import i2f.extension.zookeeper.ZookeeperConfig;
import i2f.extension.zookeeper.ZookeeperManager;
import i2f.extension.zookeeper.cluster.impl.ZookeeperClusterProvider;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @author Ice2Faith
 * @date 2023/4/11 19:13
 * @desc
 */
@Data
@NoArgsConstructor
@ConditionalOnExpression("${app.cluster.enable:false}")
@Configuration
@ConfigurationProperties(prefix = "app.cluster")
public class ClusterUtil implements ApplicationContextAware {
    private static ApplicationContext context;
    private static ClusterProvider clusterProvider;

    private static boolean enable = false;

    private ZookeeperConfig zkConfig;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
        enable = true;
    }

    public static boolean isMyTask(Object obj) {
        if (obj == null) {
            obj = 0;
        }
        int hcode = obj.hashCode();
        return isMyTask((long) hcode);
    }

    public static boolean isMyTask(long domainId) {
        if (!enable) {
            return true;
        }
        if (clusterProvider == null) {
            synchronized (ClusterUtil.class) {
                if (clusterProvider == null) {
                    clusterProvider = context.getBean(ClusterProvider.class);
                }
            }
        }
        return clusterProvider.isMy(domainId);
    }


    @Bean
    public ZookeeperManager zookeeperManager() {
        return new ZookeeperManager(zkConfig);
    }

    @Bean
    public ClusterProvider clusterProvider(@Autowired ZookeeperManager zookeeperManager) throws Exception {
        String appName = context.getEnvironment().getProperty("spring.application.name");
        if (StringUtils.isEmpty(appName)) {
            appName = "noappname";
        }
        String listenPath = "/apps/" + appName + "/cluster";
        ZookeeperClusterProvider clusterProvider = new ZookeeperClusterProvider(listenPath, zookeeperManager);
        return clusterProvider;
    }

}
