package i2f.spring.resource.test;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.spring.resource.PackageScanner;

import java.io.IOException;

public class TestPackageScanner {
    public static void main(String[] args) throws IOException {

        PackageScanner.scanResourcesEndWith(".properties", e -> !String.valueOf(e).contains(".jar!"))
                .forEach(System.out::println);

        System.out.println("===================================================");
        PackageScanner.scanClassesExtendsFrom("com", InitializingBean.class)
                .forEach(System.out::println);

        System.out.println("===================================================");
        PackageScanner.scanClassesWithAnnotation("com",
                Component.class,
                Service.class,
                Controller.class,
                RestController.class,
                Repository.class,
                Configuration.class,
                Aspect.class,
                SpringBootApplication.class)
                .forEach(System.out::println);
    }
}
