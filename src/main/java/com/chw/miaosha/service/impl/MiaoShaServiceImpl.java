package com.chw.miaosha.service.impl;

import com.chw.miaosha.allEnum.Prefix;
import com.chw.miaosha.allEnum.TimeEnum;
import com.chw.miaosha.domain.OrderInfo;
import com.chw.miaosha.domain.User;
import com.chw.miaosha.service.GoodsService;
import com.chw.miaosha.service.MiaoShaService;
import com.chw.miaosha.service.OrderService;
import com.chw.miaosha.service.RedisService;
import com.chw.miaosha.vo.GoodsVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Author CHW
 * @Date 2022/9/24
 **/
@Service
public class MiaoShaServiceImpl implements MiaoShaService {
    @Resource
    GoodsService goodsService;
    
    @Resource
    OrderService orderService;
    
    @Resource
    RedisService redisService;
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderInfo miaoSha(User user, GoodsVo goodsVo) {
        //减库存 下订单 写入秒杀订单
        boolean success = goodsService.reduceStock(goodsVo);
        
        if (success) {
            //减库存成功创建订单
            return orderService.createOrder(user, goodsVo);
        } else {
            //减库存失败 在redis中存入该商品已经售空
            setGoodsOver(goodsVo.getId());
            return null;
        }
        
    }
    
    /**
     * 将商品售空的信息存入redis
     * @param goodsId
     */
    private void setGoodsOver(Long goodsId) {
        
        redisService.set(Prefix.Goods_Over.getPrefix() + goodsId, true, TimeEnum.EXPIRE_ONE_DAY.getTime());
    }
    
}
