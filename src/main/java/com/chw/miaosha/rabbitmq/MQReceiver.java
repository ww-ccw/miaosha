package com.chw.miaosha.rabbitmq;

import com.chw.miaosha.domain.MiaoShaOrder;
import com.chw.miaosha.domain.User;
import com.chw.miaosha.service.GoodsService;
import com.chw.miaosha.service.MiaoShaService;
import com.chw.miaosha.service.OrderService;
import com.chw.miaosha.service.RedisService;
import com.chw.miaosha.util.JsonUtil;
import com.chw.miaosha.vo.GoodsVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author CHW
 * @Date 2022/9/24
 * MQ信息接收者 负责将信息传给MQ消费者
 **/
@Service
@Log4j2
public class MQReceiver {
    
    @Resource
    RedisService redisService;
    
    @Resource
    GoodsService goodsService;
    
    @Resource
    OrderService orderService;
    
    @Resource
    MiaoShaService miaoShaService;
    
    @RabbitListener(queues = "" + MQConfig.MIAOSHA_QUEUE)
    public void receive(String message) {
        log.info("MQ ---> " + "receive message" + message);
        MiaoShaMessage mm = JsonUtil.jsonToObject(message, MiaoShaMessage.class);
        User user = mm.getUser();
        long goodsId = mm.getGoodsId();
        
        //得到商品库存
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goodsVo.getStockCount();
        if (stock <= 0) {
            //库存为0
            return;
        }
        //判断是否秒杀成功
        MiaoShaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return;
        }
        //减库存 下订单 写入秒杀订单
        miaoShaService.miaoSha(user , goodsVo);
    }
    
    
}
