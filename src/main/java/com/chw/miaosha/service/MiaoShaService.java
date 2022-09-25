package com.chw.miaosha.service;

import com.chw.miaosha.domain.OrderInfo;
import com.chw.miaosha.domain.User;
import com.chw.miaosha.vo.GoodsVo;
import org.springframework.stereotype.Service;

/**
 * @Author CHW
 * @Date 2022/9/24
 **/
@Service
public interface MiaoShaService {
    /**
     * 减库存 写订单 写入秒杀订单
     * @param user
     * @param goodsVo
     */
    OrderInfo miaoSha(User user, GoodsVo goodsVo);
}
