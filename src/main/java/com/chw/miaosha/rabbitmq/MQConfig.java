package com.chw.miaosha.rabbitmq;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.Queue;

/**
 * @Author CHW
 * @Date 2022/9/24
 **/
@Configuration
public class MQConfig {
    
    public static final String MIAOSHA_QUEUE = "miaosha.queue";
    public static final String QUEUE = "queue";
    
    /**
     * Hello模式
     */
    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true);
    }
    
    @Bean
    public Queue miaoShaQueue() {
        return new Queue(MIAOSHA_QUEUE, true);
    }
    
    
}
