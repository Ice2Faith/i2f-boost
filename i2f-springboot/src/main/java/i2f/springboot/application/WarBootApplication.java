package i2f.springboot.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author ltb
 * @date 2022/4/14 18:36
 * @desc 继承 SpringBootServletInitializer 并重写configure方法，使得指向类指向启动类，则可以再war包中启动
 * 在war包中启动，pom.xml需要starter-web排除tomcat
 * 另外打包方式改为war
 */
public class WarBootApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }

    public static String startup(Class mainClass, String[] args) {
        ConfigurableApplicationContext application = SpringApplication.run(mainClass, args);
        Environment env = application.getEnvironment();
        StringBuilder builder = new StringBuilder();
        builder.append("\n----------------------------------------------------------\n")
                .append("\twelcome to this system:\n")
                .append("\tname:\t").append(env.getProperty("spring.application.name")).append("\n")
                .append("\tactive:\t").append(env.getProperty("spring.profiles.active")).append("\n");
        try {

            String ip = InetAddress.getLocalHost().getHostAddress();
            String port = env.getProperty("server.port");


            builder.append("\tlocal url: \thttp://localhost:").append(port).append("/\n")
                    .append("\tnet url: \thttp://").append(ip).append(":").append(port).append("/\n");

            Enumeration<NetworkInterface> allInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allInterfaces.hasMoreElements()) {
                NetworkInterface item = allInterfaces.nextElement();
                if (!item.isUp() || item.isVirtual() || item.isLoopback()) {
                    continue;
                }
                builder.append("\t\t").append(" ").append(item.getName()).append(": ").append(item.getDisplayName()).append("\n");
                Enumeration<InetAddress> addrs = item.getInetAddresses();
                while (addrs.hasMoreElements()) {
                    InetAddress addr = addrs.nextElement();
                    if (addr.isLoopbackAddress()) {
                        continue;
                    }
                    String pip = addr.getHostAddress();
                    if (addr instanceof Inet4Address) {
                        builder.append("\t\t\tipv4\thttp://").append(pip).append(":").append(port).append("/\n");
                    } else if (addr instanceof Inet6Address) {
                        builder.append("\t\t\tipv6\thttp://").append(pip).append(":").append(port).append("/\n");
                    }

                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        builder.append("\tstartup:\t").append(mainClass.getName()).append(".class\n")
                .append("\tos:\t").append(env.getProperty("os.name")).append(" ").append(env.getProperty("os.arch")).append(" ").append(env.getProperty("os.version")).append("\n")
                .append("\tjava:\t").append(env.getProperty("java.version")).append(" ").append(env.getProperty("java.home")).append("\n")
                .append("\tjvm:\t").append(env.getProperty("java.vm.name")).append(" ").append(env.getProperty("java.vm.version")).append("\n")
                .append("\tjava-io-tmpdir:\t").append(env.getProperty("java.io.tmpdir")).append("\n")
                .append("\tuser-name:\t").append(env.getProperty("user.name")).append("\n")
                .append("\tuser-dir:\t").append(env.getProperty("user.dir")).append("\n")
                .append("----------------------------------------------------------\n");

        return builder.toString();
    }
}
