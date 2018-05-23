package com.shanhh.siberia.web;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.shanhh")
@EnableDiscoveryClient
@EnableScheduling
@EnableFeignClients(basePackages = "com.shanhh")
@EnableJpaRepositories("com.shanhh")
@EntityScan("com.shanhh")
public class Application extends SpringApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

}
