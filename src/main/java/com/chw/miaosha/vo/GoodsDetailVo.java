package com.chw.miaosha.vo;

import com.chw.miaosha.domain.User;
import lombok.Data;

/**
 * @Author CHW
 * @Date 2022/9/24
 **/
@Data
public class GoodsDetailVo {
    private int miaoshaStatus = 0;
    private int remainSeconds = 0;
    private GoodsVo goods;
    private User user;
}
