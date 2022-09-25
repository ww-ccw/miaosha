package com.chw.miaosha.controller;


import com.chw.miaosha.allEnum.Prefix;
import com.chw.miaosha.allEnum.TimeEnum;
import com.chw.miaosha.domain.User;
import com.chw.miaosha.result.Result;
import com.chw.miaosha.service.GoodsService;
import com.chw.miaosha.service.RedisService;
import com.chw.miaosha.util.JsonUtil;
import com.chw.miaosha.vo.GoodsDetailVo;
import com.chw.miaosha.vo.GoodsVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author CHW
 * @Date 2022/9/22
 **/
@Controller
@RequestMapping("/goods")
@Log4j2
public class GoodsController {
    
    @Resource
    GoodsService goodsService;
    
    @Resource
    RedisService redisService;
    
    @Resource
    ThymeleafViewResolver thymeleafViewResolver;
    
    
    /**
     * 从数据库中得到商品列表
     * Win qbs 250
     */
    @RequestMapping(value = "/to_list-", method = RequestMethod.GET)
    public String DbList(Model model, User user) {
        
        model.addAttribute("user", user);
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        
        return "goods_list";
    }
    
    
    /**
     * 页面静态化
     * 从redis中得到商品列表
     * Win qbs 250
     */
    @RequestMapping(value = "/to_list", produces = "text/html;charset=utf-8", method = RequestMethod.GET)
    @ResponseBody
    public String CacheList(HttpServletRequest request, HttpServletResponse response, Model model, User user) {
        
        //从redis中取goodList，如果不为空直接返回
        String html = redisService.get("goods_list");
        if (!StringUtils.isEmpty(html)) {
            html = JsonUtil.jsonToObject(html, String.class);
            return html;
        }
        
        model.addAttribute("user", user);
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        
        //如果为空，手动渲染，存入Redis并返回
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(),
                model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", context);
        //存入redis
        if (!StringUtils.isEmpty(html)) {
            html = JsonUtil.objectToJson(html);
            redisService.set("goods_list", html, TimeEnum.Goods_List.getTime());
        }
        return html;
    }
    
    @RequestMapping(value = "/detail-/{goodsId}", method = RequestMethod.GET)
    @ResponseBody
    public Result<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response, Model model, User user,
                                        @PathVariable("goodsId") long goodsId) {
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        
        //参数解析判断
        long startAt = goodsVo.getStartDate().getTime();
        long endAt = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        
        //判断时间
        if (now < startAt) {
            //秒杀还没开始
            miaoshaStatus = 0;
            remainSeconds = (int) ((startAt - now) / 1000);
        } else if (now > endAt) {
            //秒杀已结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        //设置返回
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goodsVo);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setMiaoshaStatus(miaoshaStatus);
        return Result.success(vo);
    }
    
    //    @GetMapping("/to_detail2/{goodsId}")
//    @ResponseBody
    @RequestMapping(value = "/detail/{goodsId}", method = RequestMethod.GET)
    public String detail2(HttpServletRequest request, HttpServletResponse response, Model model, User user,
                          @PathVariable("goodsId") long goodsId) {
        model.addAttribute("user", user);
        //取缓存
//        String html = redisService.get(Prefix.Redis_GoodsDetailVo.getPrefix() + goodsId);
//        if (!StringUtils.isEmpty(html)) {
//            return html;
//        }
        
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goodsVo);
        
        //参数解析判断
        long startAt = goodsVo.getStartDate().getTime();
        long endAt = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        
        //秒杀时间判断
        if (now < startAt) {
            //秒杀还没开始
            miaoshaStatus = 0;
            remainSeconds = (int) ((startAt - now) / 1000);
        } else if (now > endAt) {
            //秒杀已结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        //手动渲染----->为了存进redis
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(),
                model.asMap());
        
        String html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", context);
        
        if (!StringUtils.isEmpty(html)) {
            redisService.set(Prefix.Redis_GoodsDetailVo.getPrefix() + goodsId, html, TimeEnum.EXPIRE_TOW_HOUR.getTime());
        }

//        return html;
        return "goods_detail";
    }
    
}
