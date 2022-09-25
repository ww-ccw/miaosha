package com.chw.miaosha.service.impl;

import com.chw.miaosha.allEnum.Prefix;
import com.chw.miaosha.dao.UserDao;
import com.chw.miaosha.domain.User;
import com.chw.miaosha.exception.GlobalException;
import com.chw.miaosha.result.CodeMsg;
import com.chw.miaosha.service.RedisService;
import com.chw.miaosha.service.UserService;
import com.chw.miaosha.util.CookieUtil;
import com.chw.miaosha.util.JsonUtil;
import com.chw.miaosha.util.MD5Util;
import com.chw.miaosha.util.UUIDUtil;
import com.chw.miaosha.allEnum.AllName;
import com.chw.miaosha.vo.LoginVo;
import com.chw.miaosha.allEnum.TimeEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author CHW
 * @Date 2022/9/22
 **/
@Service
@Log4j2
public class UserServiceImpl implements UserService {
    @Resource
    UserDao userDao;
    
    @Resource
    RedisService redisService;
    
    
    @Override
    public String doLogin(LoginVo loginVo, HttpServletResponse response, HttpServletRequest request) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        
        if (mobile == null || password == null) {
            log.error("出错了");
            throw new GlobalException(CodeMsg.BIND_ERROR);
        }
        
        //根据手机号获取用户
        User user = userDao.getById(Long.parseLong(mobile));
        if (null == user) {
            throw new GlobalException(CodeMsg.LOGIN_ERROR);
        }
        //判断密码是否正确
        String realPass = MD5Util.formPassToDbPass(password, user.getSalt());
        if (!realPass.equals(user.getPassword())) {
            throw new GlobalException(CodeMsg.LOGIN_ERROR);
        }
//        /**
//         * 以输入的第一次加盐的数据作为token的值
//         */
//        String tokenKey = Prefix.TOKEN_USER.getPrefix() + password;
//
//        String token = redisService.get(tokenKey);
//        if (!StringUtils.isEmpty(token)) {
//            return token;
//        }
        // 生成token
        String token = UUIDUtil.uuid();
        addCookie(response, request, token, user);
        
        return token;
    }
    
    @Override
    public User getUserByCookie(String token, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        String json = redisService.get(Prefix.TOKEN_USER.getPrefix() + token);
        User user = JsonUtil.jsonToObject(json, User.class);
        
        return user;
    }
    
    
    /**
     * 将token存放入redis及Session中
     * 放在redis是为了在分布式架构中其他tomcat也可以得到
     *
     * @param response
     * @param request
     * @param token
     * @param user
     */
    private void addCookie(HttpServletResponse response, HttpServletRequest request, String token, User user) {
        
        String realKey = Prefix.TOKEN_USER.getPrefix() + token;
        //将token及用户信息添加到redis
        redisService.set(Prefix.TOKEN_USER.getPrefix() + token, user, TimeEnum.User_Token.getTime());
        //将token添加到cookie
        CookieUtil.setCookie(request, response, AllName.TOKEN.getName(), token);
    }
}
