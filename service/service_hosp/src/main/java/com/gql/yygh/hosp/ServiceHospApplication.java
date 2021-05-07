package com.gql.yygh.hosp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Description: 医院设置微服务启动类
 * @author Guoqianliang
 * @date 20:17 - 2021/4/3
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.gql")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.gql")
public class ServiceHospApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceHospApplication.class, args);

    }
}
