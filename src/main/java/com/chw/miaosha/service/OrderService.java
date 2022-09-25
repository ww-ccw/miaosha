package com.chw.miaosha.service;

import com.chw.miaosha.domain.MiaoShaOrder;
import com.chw.miaosha.domain.OrderInfo;
import com.chw.miaosha.domain.User;
import com.chw.miaosha.vo.GoodsVo;
import org.springframework.stereotype.Service;

/**
 * @Author CHW
 * @Date 2022/9/24
 **/
@Service
public interface OrderService {
    
    /**
     * 创建订单
     * @param user
     * @param goods
     * @return
     */
    public OrderInfo createOrder(User user, GoodsVo goods);
    
    /**
     * 从redis中得到秒杀订单
     * @param userId
     * @param goodsId
     * @return
     */
    public MiaoShaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId);
    
}
