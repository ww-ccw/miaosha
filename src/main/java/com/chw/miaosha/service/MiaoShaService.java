package com.chw.miaosha.service;

import com.chw.miaosha.domain.OrderInfo;
import com.chw.miaosha.domain.User;
import com.chw.miaosha.vo.GoodsVo;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

/**
 * @Author CHW
 * @Date 2022/9/24
 **/
@Service
public interface MiaoShaService {
    /**
     * 减库存 写订单 写入秒杀订单
     * @param user
     * @param goodsVo
     */
    OrderInfo miaoSha(User user, GoodsVo goodsVo);
    
    /**
     * 得到是否已经抢购成功
     * @param id
     * @param goodsId
     * @return
     */
    long getMiaoShaResult(Long id, long goodsId);
    
    /**
     * 检查验证码
     * @param user
     * @param goodsId
     * @param verifyCode
     * @return
     */
    boolean checkVerifyCode(User user, long goodsId, int verifyCode);
    
    /**
     * 生成秒杀路径
     * @param user
     * @param goodsId
     * @return
     */
    String createMiaoShaPath(User user, long goodsId);
    
    /**
     * 生成验证码
     * @param user
     * @param goodsId
     * @return
     */
    BufferedImage createVerifyCode(User user, long goodsId);
    
    /**
     * 验证秒杀路径
     * @param user
     * @param goodsId
     * @param path
     * @return
     */
    boolean checkPath(User user , long goodsId , String path);
}
