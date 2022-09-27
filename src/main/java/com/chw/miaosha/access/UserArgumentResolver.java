package com.chw.miaosha.access;

import com.chw.miaosha.allEnum.AllName;
import com.chw.miaosha.allEnum.Prefix;
import com.chw.miaosha.domain.User;
import com.chw.miaosha.exception.GlobalException;
import com.chw.miaosha.result.CodeMsg;
import com.chw.miaosha.service.RedisService;
import com.chw.miaosha.service.UserService;
import com.chw.miaosha.util.CookieUtil;
import com.chw.miaosha.util.JsonUtil;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 参数解析器
 * 如果controller需要的参数有User就会触发
 *
 * @Author CHW
 * @Date 2022/9/23
 **/
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    
    @Resource
    RedisService redisService;
    
    /**
     * 验证参数类型是否是User
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
        return clazz == User.class;
    }
    
    /**
     * 校验user
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = CookieUtil.getCookieValue(request, AllName.TOKEN.getName());
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        User user = JsonUtil.jsonToObject(redisService.get(Prefix.TOKEN_USER.getPrefix() + token), User.class);
        if (user == null) {
            return null;
        }
        UserContext.setUser(user);
        return user;
    }
}
