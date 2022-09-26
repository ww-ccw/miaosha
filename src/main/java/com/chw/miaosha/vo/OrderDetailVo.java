package com.chw.miaosha.vo;

import com.chw.miaosha.domain.OrderInfo;
import lombok.Data;

/**
 * @Author CHW
 * @Date 2022/9/26
 **/
@Data
public class OrderDetailVo {
    private GoodsVo goods;
    private OrderInfo order;
}
