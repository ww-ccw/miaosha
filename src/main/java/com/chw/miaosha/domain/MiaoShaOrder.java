package com.chw.miaosha.domain;

import lombok.Data;

/**
 * @author CW
 */
@Data
public class MiaoShaOrder {
    private Long id;
    private Long userId;
    private Long orderId;
    private Long goodsId;
}
