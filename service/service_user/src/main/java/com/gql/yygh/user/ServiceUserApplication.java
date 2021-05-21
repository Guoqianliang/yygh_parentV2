package com.gql.yygh.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Description:
 * @author Guoqianliang
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.gql")
// 将用户微服务注册到Nacos
@EnableDiscoveryClient
// 开启远程调用
@EnableFeignClients(basePackages = "com.gql")
public class ServiceUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUserApplication.class, args);
    }
}
