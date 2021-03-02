package org.remo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
//        SpringApplication.run(ServerApplication.class, args);
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(ServerApplication.class, args);
        Map<String, Object> grpcServiceBeanMap =  configurableApplicationContext.getBeansWithAnnotation(GrpcService.class);
        GrpcLauncher grpcLauncher = configurableApplicationContext.getBean("grpcLauncher",GrpcLauncher.class);
        grpcLauncher.grpcStart(grpcServiceBeanMap);
    }

}
