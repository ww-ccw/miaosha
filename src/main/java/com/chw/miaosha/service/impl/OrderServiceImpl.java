package com.chw.miaosha.service.impl;

import com.chw.miaosha.allEnum.Prefix;
import com.chw.miaosha.allEnum.TimeEnum;
import com.chw.miaosha.dao.OrderDao;
import com.chw.miaosha.domain.MiaoShaOrder;
import com.chw.miaosha.domain.OrderInfo;
import com.chw.miaosha.domain.User;
import com.chw.miaosha.service.OrderService;
import com.chw.miaosha.service.RedisService;
import com.chw.miaosha.util.JsonUtil;
import com.chw.miaosha.vo.GoodsVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author CHW
 * @Date 2022/9/24
 **/
@Service
public class OrderServiceImpl implements OrderService {
    
    @Resource
    OrderDao orderDao;
    
    @Resource
    RedisService redisService;
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderInfo createOrder(User user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        //将订单信息存入数据库
        orderDao.insert(orderInfo);
        
        MiaoShaOrder miaoShaOrder = new MiaoShaOrder();
        miaoShaOrder.setGoodsId(goods.getId());
        miaoShaOrder.setOrderId(orderInfo.getId());
        miaoShaOrder.setUserId(user.getId());
        //将秒杀订单信息存入数据库
        orderDao.insertMiaoshaOrder(miaoShaOrder);
        //将秒杀订单信息传入redis
        redisService.set(Prefix.User_Order.getPrefix() + user.getId() + "_" + goods.getId(), miaoShaOrder, TimeEnum.UserOrderTime.getTime());
        
        return orderInfo;
    }
    
    @Override
    public MiaoShaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId) {
        String json = redisService.get(Prefix.User_Order.getPrefix() + userId + "_" + goodsId);
        MiaoShaOrder miaoShaOrder = null;
        if (!StringUtils.isEmpty(json)) {
            miaoShaOrder = JsonUtil.jsonToObject(json, MiaoShaOrder.class);
        }
        return miaoShaOrder;
    }
    
    @Override
    public OrderInfo getOrderById(long orderId){
        return orderDao.getOrderById(orderId);
    }
    
    
}
