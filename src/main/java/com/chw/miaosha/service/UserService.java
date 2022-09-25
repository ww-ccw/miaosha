package com.chw.miaosha.service;

import com.chw.miaosha.domain.User;
import com.chw.miaosha.vo.LoginVo;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author CHW
 * @Date 2022/9/22
 **/
@Service
public interface UserService {
    
    /**
     * 登录及验证
     *
     * @param loginVo
     * @param response
     * @param request
     * @return
     */
    public String doLogin(LoginVo loginVo, HttpServletResponse response, HttpServletRequest request);
    
    /**
     * 根据Cookie得到User
     * @param token
     * @param request
     * @param response
     * @return
     */
    User getUserByCookie(String token, HttpServletRequest request, HttpServletResponse response);
}
