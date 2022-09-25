package com.chw.miaosha.service.impl;

import com.chw.miaosha.dao.GoodsDao;
import com.chw.miaosha.domain.MiaoShaGoods;
import com.chw.miaosha.service.GoodsService;
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
    public boolean reduceStock(GoodsVo goods) {
        MiaoShaGoods g = new MiaoShaGoods();
        g.setGoodsId(goods.getId());
        int ret = goodsDao.reduceStock(g);
        return ret > 0;
    }
    
}
