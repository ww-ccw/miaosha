package com.chw.miaosha.controller;

import com.chw.miaosha.result.Result;
import com.chw.miaosha.service.UserService;
import com.chw.miaosha.vo.LoginVo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @Author CHW
 * @Date 2022/9/22
 **/
@RequestMapping("/login")
@Log4j2
@Controller
public class LoginController {
    @Resource
    UserService userService;
    
    @RequestMapping("to_login")
    public String toLogin() {
        return "login";
    }
    
    @PostMapping("/do_login")
    @ResponseBody
    public Result<String> doLogin(HttpServletRequest request, HttpServletResponse response, @Valid LoginVo loginVo) {
        log.info(loginVo.toString());
        //生成token并保存
        String token = userService.doLogin(loginVo, response, request);
        return Result.success(token);
    }
    
}
