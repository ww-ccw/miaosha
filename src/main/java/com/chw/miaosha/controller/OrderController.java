package com.chw.miaosha.controller;

import com.chw.miaosha.domain.MiaoShaOrder;
import com.chw.miaosha.domain.OrderInfo;
import com.chw.miaosha.domain.User;
import com.chw.miaosha.result.CodeMsg;
import com.chw.miaosha.result.Result;
import com.chw.miaosha.service.GoodsService;
import com.chw.miaosha.service.OrderService;
import com.chw.miaosha.vo.GoodsVo;
import com.chw.miaosha.vo.OrderDetailVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Author CHW
 * @Date 2022/9/26
 **/
@Controller
@RequestMapping("/order")
public class OrderController {
    
    @Resource
    GoodsService goodsService;
    
    @Resource
    OrderService orderService;
    
    /**
     * 订单信息静态化
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ResponseBody
    public Result<OrderDetailVo> info(Model model, User user, @RequestParam("orderId") long orderId) {
        if (user == null) {
            return Result.error(CodeMsg.NO_LOGIN);
        }
        
        //从数据库中得到订单信息
        OrderInfo orderInfo = orderService.getOrderById(orderId);
        
        if (orderInfo == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        //根据订单的商品号得到商品信息
        long goodsId = orderInfo.getGoodsId();
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        //封装入返回信息中
        OrderDetailVo vo = new OrderDetailVo();
        vo.setGoods(goodsVo);
        vo.setOrder(orderInfo);
        
        return Result.success(vo);
    }
    
}
