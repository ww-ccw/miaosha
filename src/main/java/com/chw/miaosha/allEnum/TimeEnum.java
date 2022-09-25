package com.chw.miaosha.allEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @Author CHW
 * @Date 2022/9/22
 **/
@Getter
@AllArgsConstructor
@ToString
public enum TimeEnum {
    /**
     * 2小时
     */
    EXPIRE_TOW_HOUR(3600 * 2),
    
    /**
     * 一分钟
     */
    EXPIRE_MINUTE(60),
    
    
    /**
     * 一天
     */
    EXPIRE_ONE_DAY(3600 * 24),
    
    /**
     * 永久
     */
    PERMANENT(0),
    
    /**
     * 用户订单信息
     */
    UserOrderTime(3600 * 24 * 3),
    /**
     * goods_list失效时间
     */
    Goods_List(60),
    
    /**
     * 用户token
     */
    User_Token(3600 * 3),
    
    ;
    
    
    private int time;
}
