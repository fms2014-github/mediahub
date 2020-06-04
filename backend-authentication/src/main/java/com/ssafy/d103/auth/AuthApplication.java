package com.ssafy.d103.auth;

import com.ssafy.d103.auth.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.AbstractEnvironment;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class AuthApplication {

    public static void main(String[] args) {
        // 배포 local 개발 및 테스트 dev
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "local");
        SpringApplication.run(AuthApplication.class, args);
    }
}
