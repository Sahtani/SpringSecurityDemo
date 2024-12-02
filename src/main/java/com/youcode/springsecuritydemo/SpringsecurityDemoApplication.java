package com.youcode.springsecuritydemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SpringsecurityDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringsecurityDemoApplication.class, args);
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("password");
        System.out.println("Encoded password: " + encodedPassword);
    }

}
