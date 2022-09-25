package com.chw.miaosha.controller;

import com.chw.miaosha.domain.MiaoShaOrder;
import com.chw.miaosha.domain.OrderInfo;
import com.chw.miaosha.domain.User;
import com.chw.miaosha.result.CodeMsg;
import com.chw.miaosha.service.*;
import com.chw.miaosha.vo.GoodsVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
     * 点击秒杀，使用这个控制器
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping("/do_miaosha")
    public String list(Model model, User user, @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return "login";
        }
        //判断库存
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goodsVo.getGoodsStock();
        if (stock <= 0) {
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
}
