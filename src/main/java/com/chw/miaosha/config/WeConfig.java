package com.chw.miaosha.config;

import com.chw.miaosha.access.UserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author CHW
 * @Date 2022/9/23
 **/
@Configuration
public class WeConfig implements WebMvcConfigurer {
    @Resource
    UserArgumentResolver userArgumentResolver;
    
    /**
     * 添加解析器
     *
     * @param resolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userArgumentResolver);
    }
    
    
}
