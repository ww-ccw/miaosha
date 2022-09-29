package com.chw.miaosha.service.impl;

import com.chw.miaosha.allEnum.Prefix;
import com.chw.miaosha.allEnum.TimeEnum;
import com.chw.miaosha.domain.MiaoShaOrder;
import com.chw.miaosha.domain.OrderInfo;
import com.chw.miaosha.domain.User;
import com.chw.miaosha.service.GoodsService;
import com.chw.miaosha.service.MiaoShaService;
import com.chw.miaosha.service.OrderService;
import com.chw.miaosha.service.RedisService;
import com.chw.miaosha.util.JsonUtil;
import com.chw.miaosha.util.MD5Util;
import com.chw.miaosha.util.UUIDUtil;
import com.chw.miaosha.vo.GoodsVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @Author CHW
 * @Date 2022/9/24
 **/
@Service
public class MiaoShaServiceImpl implements MiaoShaService {
    @Resource
    GoodsService goodsService;
    
    @Resource
    OrderService orderService;
    
    @Resource
    RedisService redisService;
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderInfo miaoSha(User user, GoodsVo goodsVo) {
        //减库存 下订单 写入秒杀订单
        boolean success = goodsService.reduceStock(goodsVo);
        
        if (success) {
            //减库存成功创建订单
            return orderService.createOrder(user, goodsVo);
        } else {
            //减库存失败 在redis中存入该商品已经售空
            setGoodsOver(goodsVo.getId());
            return null;
        }
        
    }
    
    @Override
    public long getMiaoShaResult(Long id, long goodsId) {
        MiaoShaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(id, goodsId);
        if (order != null) {
            //秒杀成功
            return order.getOrderId();
        } else {
            //判断商品是否售空
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                return -1;
            } else {
                return 0;
            }
        }
    }
    
    @Override
    public boolean checkVerifyCode(User user, long goodsId, int verifyCode) {
        if (user == null || goodsId <= 0) {
            return false;
        }
        Integer codeOld = JsonUtil.jsonToObject(redisService.get(Prefix.VerifyCode.getPrefix() + user.getId() + "_" + goodsId), Integer.class);
        if (codeOld == null || codeOld - verifyCode != 0) {
            return false;
        }
        redisService.delete(Prefix.VerifyCode.getPrefix() + user.getId() + "_" + goodsId);
        return true;
    }
    
    @Override
    public String createMiaoShaPath(User user, long goodsId) {
        if (user == null || goodsId <= 0) {
            return null;
        }
        
        String str = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisService.set(Prefix.VerifyCode.getPrefix() + user.getId() + "_" + goodsId, str, TimeEnum.Path.getTime());
        return str;
    }
    
    @Override
    public BufferedImage createVerifyCode(User user, long goodsId) {
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(Prefix.VerifyCode.getPrefix()+user.getId()+"_"+goodsId, rnd , TimeEnum.VerifyCode.getTime());
        //输出图片
        return image;
    }
    
    @Override
    public boolean checkPath(User user, long goodsId, String path) {
        if (user == null || path == null){
            return false;
        }
        String pathOld = redisService.get(Prefix.VerifyCode.getPrefix() + user.getId() + "_" + goodsId);
        return path.equals(pathOld);
    }
    
    /**
     * 将商品售空的信息存入redis
     *
     * @param goodsId
     */
    private void setGoodsOver(Long goodsId) {
        redisService.set(Prefix.Goods_Over.getPrefix() + goodsId, true, TimeEnum.EXPIRE_ONE_DAY.getTime());
    }
    
    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(Prefix.Goods_Over.getPrefix() + goodsId);
    }
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }
    
    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    private static char[] ops = new char[] {'+', '-', '*'};
    
}
