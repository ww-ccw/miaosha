package com.chw.miaosha.allEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 * @Author CHW
 * @Date 2022/9/22
 **/
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public enum Prefix {
    
    /**
     * 用于向redis中添加token_user
     */
    TOKEN_USER("token_user"),
    
    /**
     * 商品详情
     */
    Redis_GoodsDetailVo("goodsDetailVo"),
    
    /**
     * 用户订单信息
     */
    
    User_Order("user_order"),
    
    ;
    
    
    private String prefix;
    
}
