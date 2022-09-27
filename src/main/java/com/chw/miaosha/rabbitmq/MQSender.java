package com.chw.miaosha.rabbitmq;

import com.chw.miaosha.service.RedisService;
import com.chw.miaosha.util.JsonUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author CHW
 * @Date 2022/9/24
 **/
@Service
@Log4j2
public class MQSender {
    
    @Resource
    AmqpTemplate amqpTemplate;
    
    /**
     * RabbitMQ消费者
     * @param miaoshaMessage 信息
     */
    public void sendMiaoShaMessage(MiaoShaMessage miaoshaMessage) {
        String msg = JsonUtil.objectToJson(miaoshaMessage);
        log.info("MQ ---> "+"send message: "+msg);
        amqpTemplate.convertAndSend(MQConfig.QUEUE , msg);
    }
    
    
}
