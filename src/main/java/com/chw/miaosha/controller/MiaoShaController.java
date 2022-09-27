package com.chw.miaosha.controller;

import com.chw.miaosha.allEnum.Prefix;
import com.chw.miaosha.domain.Goods;
import com.chw.miaosha.domain.MiaoShaOrder;
import com.chw.miaosha.domain.OrderInfo;
import com.chw.miaosha.domain.User;
import com.chw.miaosha.rabbitmq.MQSender;
import com.chw.miaosha.rabbitmq.MiaoShaMessage;
import com.chw.miaosha.result.CodeMsg;
import com.chw.miaosha.result.Result;
import com.chw.miaosha.service.*;
import com.chw.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @Author CHW
 * @Date 2022/9/24
 **/
@Controller
@RequestMapping("/miaosha")
public class MiaoShaController implements InitializingBean {
    
    @Resource
    UserService userService;
    
    @Resource
    RedisService redisService;
    
    @Resource
    GoodsService goodsService;
    
    @Resource
    OrderService orderService;
    
    @Resource
    MiaoShaService miaoShaService;
    
    @Resource
    MQSender sender;
    
    /**
     * 系统初始化的时候会回调这个函数
     * 将各个秒杀商品的库存写入redis
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        if (goodsVoList == null) {
            return;
        }
        //将秒杀商品数量读入redis
        for (GoodsVo goodsVo : goodsVoList) {
            redisService.set(Prefix.Goods_StockCount.getPrefix() + goodsVo.getId(), goodsVo.getStockCount(), (int) (System
                    .currentTimeMillis() - goodsVo.getEndDate().getTime()) / 1000);
        }
    }
    
    
    /**
     * v1
     * 秒杀 + 非静态订单详情页
     */
    @RequestMapping("/do_miaosha-")
    public String miaoSha(Model model, User user, @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return "login";
        }
        //判断库存
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        int count = goodsVo.getStockCount();
        if (count <= 0) {
            model.addAttribute("errmsg", CodeMsg.EMPTY_STOCK.getMsg());
            return "miaosha_fail";
        }
        //判断是否已经秒杀成功
        MiaoShaOrder miaoShaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (miaoShaOrder != null) {
            model.addAttribute("errmsg", CodeMsg.REPEATE_ERROR.getMsg());
            return "miaosha_fail";
        }
        //减库存 写订单 写入秒杀订单
        OrderInfo orderInfo = miaoShaService.miaoSha(user, goodsVo);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goodsVo);
        return "order_detail";
    }
    
    /**
     * v2
     * 配合静态化订单使用 , 使用ajax秒杀 , 成功后跳转到 order_detail.htm , 再使用ajax进行初始化
     * 秒杀 + 订单页面静态化
     * 需要搭配goods_detail.htm并书写ajax跳转页面使用
     */
    @RequestMapping("/do_miaosha--")
    @ResponseBody
    public Result<OrderInfo> staticMiaoSha(User user, @RequestParam("goodsId") long goodsId) {
        
        if (user == null) {
            return Result.error(CodeMsg.NO_LOGIN);
        }
        //判断库存
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        int count = goodsVo.getStockCount();
        if (count <= 0) {
            return Result.error(CodeMsg.EMPTY_STOCK);
            
        }
        //判断是否已经秒杀成功
        MiaoShaOrder miaoShaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (miaoShaOrder != null) {
            return Result.error(CodeMsg.REPEATE_ERROR);
            
        }
        //减库存 写订单 写入秒杀订单
        OrderInfo orderInfo = miaoShaService.miaoSha(user, goodsVo);
        return Result.success(orderInfo);
    }
    
    /**
     * v3使用RabbitMq优化订单 redis遇预减库存
     *
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/do_miaosha" , method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> mqMiaoSha(User user, @RequestParam(value = "goodsId") long goodsId) {
        
        if (user == null) {
            return Result.error(CodeMsg.NO_LOGIN);
        }
        //预减库存
        long count = goodsService.decrCount(goodsId);
        if (count < 0) {
            //如果库存不足则手动回滚
            goodsService.incrCount(goodsId);
            return Result.error(CodeMsg.EMPTY_STOCK);
        }
        
        //判断是否已经秒杀成功
        MiaoShaOrder miaoShaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (miaoShaOrder != null) {
            return Result.error(CodeMsg.REPEATE_ERROR);
        }
        //入队
        MiaoShaMessage miaoShaMessage = new MiaoShaMessage();
        miaoShaMessage.setUser(user);
        miaoShaMessage.setGoodsId(goodsId);
        sender.sendMiaoShaMessage(miaoShaMessage);
        //派对中
        return Result.success(0);
    }
    
    
    /**
     * orderId :成功
     * -1 :秒杀失败
     * 0 :排队中
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping("/result")
    @ResponseBody
    public Result<Long> miaoShaResult(Model model, User user, @RequestParam("goodsId") long goodsId) {
        if (user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        
        long result = miaoShaService.getMiaoShaResult(user.getId() , goodsId);
        return Result.success(result);
        
    }
    
    
}
