package com.chw.miaosha.service.impl;

import com.chw.miaosha.allEnum.Prefix;
import com.chw.miaosha.dao.GoodsDao;
import com.chw.miaosha.domain.MiaoShaGoods;
import com.chw.miaosha.service.GoodsService;
import com.chw.miaosha.service.RedisService;
import com.chw.miaosha.util.JsonUtil;
import com.chw.miaosha.vo.GoodsVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author CHW
 * @Date 2022/9/22
 **/
@Service
public class GoodsServiceImpl implements GoodsService {
    @Resource
    RedisService redisService;
    
    @Resource
    GoodsDao goodsDao;
    
    @Override
    public List<GoodsVo> listGoodsVo() {
        return goodsDao.listGoodsVo();
    }
    
    
    @Override
    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }
    
    @Override
    public Integer getStockCountByRedis(Long goodsId) {
        String str = redisService.get(Prefix.Goods_StockCount.getPrefix() + goodsId);
        int count = JsonUtil.jsonToObject(str , Integer.class);
        return count;
    }
    
    @Override
    public boolean reduceStock(GoodsVo goods) {
        MiaoShaGoods g = new MiaoShaGoods();
        g.setGoodsId(goods.getId());
        int ret = goodsDao.reduceStock(g);
        return ret > 0;
    }
    
    @Override
    public long decrCount(long goodsId){
        return redisService.decr(Prefix.Goods_StockCount.getPrefix()+goodsId);
    }
    
    @Override
    public long incrCount(long goodsId){
        return redisService.incr(Prefix.Goods_StockCount.getPrefix()+goodsId);
    }
    
}
