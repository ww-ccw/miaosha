package com.chw.miaosha.controller;

import com.chw.miaosha.domain.MiaoShaOrder;
import com.chw.miaosha.domain.OrderInfo;
import com.chw.miaosha.domain.User;
import com.chw.miaosha.result.CodeMsg;
import com.chw.miaosha.result.Result;
import com.chw.miaosha.service.*;
import com.chw.miaosha.vo.GoodsVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Author CHW
 * @Date 2022/9/24
 **/
@Controller
@RequestMapping("/miaosha")
public class MiaoShaController {
    
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
        model.addAttribute("goods" ,goodsVo);
        return "order_detail";
    }
    
    /**
     * v2
     * 配合静态化订单使用 , 使用ajax秒杀 , 成功后跳转到 order_detail.htm , 再使用ajax进行初始化
     * 秒杀 + 订单页面静态化
     * 需要搭配goods_detail.htm并书写ajax跳转页面使用
     */
    @RequestMapping("/do_miaosha")
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
}
