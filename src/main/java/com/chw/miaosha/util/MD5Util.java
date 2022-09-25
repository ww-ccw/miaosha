package com.chw.miaosha.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

/**
 * @Author CHW
 * @Date 2022/9/22
 **/
@Component
public class MD5Util {
    
    private static final String SALT = "1a2b3c4d";
    
    /**
     * 将字符串转换成md5
     */
    public static String md5(String str) {
        return DigestUtils.md5Hex(str);
    }
    
    public static String inputPassToFromPass(String inputPass) {
        String str = "" + SALT.charAt(0) + SALT.charAt(2) + inputPass + SALT.charAt(5) + SALT.charAt(4);
        return md5(str);
    }
    
    public static String formPassToDbPass(String formPass, String salt) {
        String str = salt + formPass;
        return md5(str);
    }
    
    /**
     * 将输入密码转换成存入DB的密码
     * @param inputPass 输入密码
     * @param salt      盐
     * @return
     */
    public static String inputPassToDbPass(String inputPass, String salt) {
        String formPass = inputPassToFromPass(inputPass);
        return formPassToDbPass(formPass, salt);
    }
    
    public static void main(String[] args) {
        System.out.println(inputPassToFromPass("123456789" ));
        System.out.println(formPassToDbPass(inputPassToFromPass("123456789" ) , "zzaa"));
        System.out.println(inputPassToDbPass("123456789" , "zzaa"));
    }
    
}
