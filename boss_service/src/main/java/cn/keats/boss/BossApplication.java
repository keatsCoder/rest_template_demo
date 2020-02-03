package cn.keats.boss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: keats_coder
 * @Date: 2020/1/28
 * @Version 1.0
 */
@SpringBootApplication
public class BossApplication {
    public static void main(String[] args) {
        SpringApplication.run(BossApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
