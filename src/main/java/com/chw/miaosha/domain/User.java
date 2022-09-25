package com.chw.miaosha.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author CHW
 * @Date 2022/9/21
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Component
public class User {
    
    private Long id;
    private String nickname;
    private String password;
    private String salt;
    private String head;
    private Date registerDate;
    private Date lastLoginDate;
    private Integer loginCount;
}
