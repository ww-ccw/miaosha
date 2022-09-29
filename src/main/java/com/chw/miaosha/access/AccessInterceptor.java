package com.chw.miaosha.access;

import java.io.OutputStream;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chw.miaosha.allEnum.AllName;
import com.chw.miaosha.allEnum.Prefix;
import com.chw.miaosha.allEnum.TimeEnum;
import com.chw.miaosha.domain.User;
import com.chw.miaosha.result.CodeMsg;
import com.chw.miaosha.result.Result;
import com.chw.miaosha.service.RedisService;
import com.chw.miaosha.service.UserService;
import com.chw.miaosha.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;

@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {
    
    @Resource
    UserService userService;
    
    @Resource
    RedisService redisService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            User user = getUser(request, response);
            UserContext.setUser(user);
            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            String key = request.getRequestURI();
            if (needLogin) {
                if (user == null) {
                    render(response, CodeMsg.REQUEST_ILLEGAL);
                    return false;
                }
                key += "_" + user.getId();
            } else {
                //do nothing
            }
            //AccessKey ak = AccessKey.withExpire(seconds);
            Integer count = JsonUtil.jsonToObject(redisService.get(Prefix.Access.getPrefix() + key) , Integer.class);
            if (count == null) {
                redisService.set(Prefix.Access.getPrefix() + key, 1 , TimeEnum.access.getTime());
            } else if (count < maxCount) {
                redisService.incr(Prefix.Access.getPrefix() + key);
            } else {
                render(response, CodeMsg.ACCESS_LIMIT_REAHCED);
                return false;
            }
        }
        return true;
    }
    
    private void render(HttpServletResponse response, CodeMsg cm) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(Result.error(cm));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }
    
    private User getUser(HttpServletRequest request, HttpServletResponse response) {
        String paramToken = request.getParameter(AllName.TOKEN.getName());
        String cookieToken = getCookieValue(request, AllName.TOKEN.getName());
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return null;
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        return userService.getUserByCookie(token, request, response);
    }
    
    /**
     * 从 HTTP 请求中获取 cookie 值
     *
     * @param request
     * @param cookieName
     * @return
     */
    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }
    
}
