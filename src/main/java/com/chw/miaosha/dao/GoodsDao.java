package com.chw.miaosha.dao;

import com.chw.miaosha.domain.MiaoShaGoods;
import com.chw.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author CHW
 * @Date 2022/9/23
 **/
@Mapper
public interface GoodsDao {
    
    
    /**
     * 查询全部秒杀商品列表
     *
     * @return
     */
    @Select("select g.* , mg.stock_count , mg.start_date , mg.end_date , mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id")
    List<GoodsVo> listGoodsVo();
    
    /**
     * 通过商品id得到商品详情
     * @param goodsId
     * @return
     */
    @Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id where g.id = #{goodsId}")
    GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);
    
    /**
     * 减少库存
     * @param g
     * @return
     */
    @Update("update miaosha_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count > 0")
    int reduceStock(MiaoShaGoods g);
}
