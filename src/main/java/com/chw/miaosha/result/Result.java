package com.chw.miaosha.result;

import lombok.Getter;


/**
 * @Author CHW
 * @Date 2022/9/21
 **/
@Getter
public class Result<T> {
    private int code;
    private String msg;
    private T data;
    
    public static <T> Result<T> success(T data){
        return new  Result<T>(data);
    }
    
    public static <T> Result<T> error(CodeMsg cm){
        return new  Result<T>(cm);
    }
    
    /**
     * 构造函数私有, 禁止外部创建对象
     */
    private Result(T data) {
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }
    
    private Result(CodeMsg cm) {
        if(cm == null) {
            return;
        }
        this.code = cm.getCode();
        this.msg = cm.getMsg();
    }

}
