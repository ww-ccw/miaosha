package com.chw.miaosha.vo;

import com.chw.miaosha.domain.Goods;
import lombok.*;

import java.util.Date;

/**
 * @author CW
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoodsVo extends Goods {
	private Double miaoshaPrice;
	private Integer stockCount;
	private Date startDate;
	private Date endDate;
	
	public static void main(String[] args) {
		GoodsVo goodsVo = new GoodsVo();
		goodsVo.setGoodsImg("/opt/jsp");
		System.out.println(goodsVo.getGoodsImg());
	}
}
