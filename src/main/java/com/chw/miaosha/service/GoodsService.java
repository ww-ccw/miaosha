package com.chw.miaosha.service;

import com.chw.miaosha.vo.GoodsVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author CHW
 * @Date 2022/9/22
 **/
@Service
public interface GoodsService {
    /**
     * 从数据库中获取商品列表
     *
     * @return
     */
    public List<GoodsVo> listGoodsVo();
    
    /**
     * 从数据库通过商品id得到详情
     *
     * @param goodsId
     * @return
     */
    public GoodsVo getGoodsVoByGoodsId(long goodsId);
    
    /**
     * 从redis中获得商品库存
     * @param goodsId
     * @return
     */
    public Integer getStockCountByRedis(Long goodsId);
    
    
    /**
     * db减库存
     *
     * @param goods
     * @return
     */
    public boolean reduceStock(GoodsVo goods);
    
    /**
     * 从redis中减库存
     * @param goodsId
     * @return
     */
    public long decrCount(long goodsId);
    
    /**
     * 从redis中增加库存
     * @param goodsId
     * @return
     */
    public long incrCount(long goodsId);
    
}
