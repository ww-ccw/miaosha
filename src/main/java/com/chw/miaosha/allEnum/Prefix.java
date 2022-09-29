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
    TOKEN_USER("token_user_"),
    
    /**
     * 商品详情
     */
    Redis_GoodsDetailVo("goodsDetailVo_"),
    
    /**
     * 用户订单信息
     * user_order_+userId+goodsId
     */
    
    User_Order("user_order_"),
    
    /**
     * 商品售空
     * goods_over_ + id
     */
    Goods_Over("goods_over_"),
    
    /**
     * 商品库存
     * goods_stockCount_ + goodsId
     */
    Goods_StockCount("goods_stockCount_"),
    
    /**
     * 验证码
     * VerifyCode_ + userId + goodsId
     */
    VerifyCode("verifyCode_"),
    
    /**
     * 计数器
     * access
     */
    Access("access"),
    ;
    
    
    private String prefix;
    
}
