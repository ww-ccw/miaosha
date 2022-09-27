package com.chw.miaosha.rabbitmq;


import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author CHW
 * @Date 2022/9/24
 **/
@Configuration
public class MQConfig {
    
    public static final String QUEUE = "msQueue";

    @Bean
    public Queue msQueue() {
        return new Queue(QUEUE, true);
    }
    
    
}
