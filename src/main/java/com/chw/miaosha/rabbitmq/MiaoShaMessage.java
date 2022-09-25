package com.chw.miaosha.rabbitmq;

import com.chw.miaosha.domain.User;
import lombok.Data;

/**
 * @Author CHW
 * @Date 2022/9/24
 **/
@Data
public class MiaoShaMessage {
    /**
     * 购买者的用户信息
     */
    private User user;
    /**
     * 将购买的商品Id
     */
    private long goodsId;
}
