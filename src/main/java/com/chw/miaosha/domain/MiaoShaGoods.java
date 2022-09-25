package com.chw.miaosha.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author CW
 */
@Data
public class MiaoShaGoods {
    private Long id;
    private Long goodsId;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
