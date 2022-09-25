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
public enum AllName {
    
    /**
     * 用于存放用户登录 token
     */
    TOKEN("token"),
    
    /**
     * redis中商品列表
     */
    Good_list("goodsList"),
    ;
    
    private String name;
}
