package com.chw.miaosha.access;

import com.chw.miaosha.domain.User;

/**
 * ThreadLocal是本地线程变量，它可以为每个线程都创建一个单独的存储空间（不同的线程调用同一个ThreadLocal得到的都不一样，所以它可以是static）。
 * --用于存放变量。
 *
 * 这里用于存放用户上下文
 *
 * @Author CHW
 * @Date 2022/9/23
 **/
public class UserContext {
    
    private static ThreadLocal<User> userHolder = new ThreadLocal<User>();
    
    /**
     * 设置变量
     */
    public static void setUser(User user) {
        userHolder.set(user);
    }
    
    /**
     * 得到变量
     */
    public static User getUser() {
        return userHolder.get();
    }
    
}
