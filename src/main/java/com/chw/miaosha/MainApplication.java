package com.chw.miaosha;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author CHW
 * @Date 2022/9/21
 **/
@SpringBootApplication
@MapperScan("com.chw.miaosha.dao")
public class MainApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class , args);
    }
}
