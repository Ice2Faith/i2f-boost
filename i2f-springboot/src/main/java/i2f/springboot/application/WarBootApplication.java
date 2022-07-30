package i2f.springboot.application;

import i2f.spring.slf4j.Slf4jPrintStream;
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
        Slf4jPrintStream.redirectSysoutSyserr();
        ConfigurableApplicationContext application = SpringApplication.run(mainClass, args);
        Environment env = application.getEnvironment();
        StringBuilder builder = new StringBuilder();
        builder.append("\n----------------------------------------------------------\n")
                .append("\twelcome to this system:\n")
                .append("\tapp    :\t").append(env.getProperty("spring.application.name")).append(" | ").append(env.getProperty("spring.profiles.active")).append("\n");
        try {

            String ip = InetAddress.getLocalHost().getHostAddress();
            String port = env.getProperty("server.port");
            String contextPath=env.getProperty("server.servlet.context-path");
            if(contextPath!=null && !"".equals(contextPath)){
                contextPath=contextPath+"/";
            }
            if(contextPath==null){
                contextPath="";
            }


            builder.append("\tlocal  : \thttp://localhost:").append(port).append(contextPath).append("/\n")
                   .append("\tnet    : \thttp://").append(ip).append(":").append(port).append(contextPath).append("/\n");

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
                        builder.append("\t\t\tipv4\thttp://").append(pip).append(":").append(port).append(contextPath).append("/\n");
                    } else if (addr instanceof Inet6Address) {
                        builder.append("\t\t\tipv6\thttp://").append(pip).append(":").append(port).append(contextPath).append("/\n");
                    }

                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Runtime runtime=Runtime.getRuntime();
        builder.append("\tstartup:\t").append(mainClass.getName()).append(".class\n")
               .append("\tsystem :\t").append(env.getProperty("os.name")).append(" | ").append(env.getProperty("os.arch")).append(" | ").append(env.getProperty("os.version")).append("\n")
               .append("\tenv    :\t").append("core:").append(runtime.availableProcessors()).append(" | ").append("useRate:" + (((int) ((1.0 - (runtime.freeMemory() * 1.0 / runtime.totalMemory())) * 10000)) / 100.0)).append("% | ").append("free:").append(runtime.freeMemory()/1024/1024).append("M").append(" | ").append("total:").append(runtime.totalMemory()/1024/1024).append("M").append(" | ").append("max:").append(runtime.maxMemory()/1024/1024).append("M").append("\n")
               .append("\tjava   :\t").append(env.getProperty("java.version")).append(" | ").append(env.getProperty("java.vendor")).append(" | ").append(env.getProperty("java.home")).append("\n")
               .append("\tjvm    :\t").append(env.getProperty("java.vm.name")).append(" | ").append(env.getProperty("java.vm.version")).append(" | ").append(env.getProperty("java.vm.vendor")).append("\n")
               .append("\ttmpdir :\t").append(env.getProperty("java.io.tmpdir")).append("\n")
               .append("\tuser   :\t").append(env.getProperty("user.name")).append(" | ").append(env.getProperty("user.dir")).append("\n")
               .append("----------------------------------------------------------\n");

        return builder.toString();
    }
}
