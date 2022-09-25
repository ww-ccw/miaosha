package com.chw.miaosha.dao;

import com.chw.miaosha.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Author CHW
 * @Date 2022/9/21
 **/
@Mapper
public interface UserDao {
    /**
     * 通过id得到用户信息
     * @param id
     * @return
     */
    @Select("select * from miaosha_user where id = #{id}")
    User getById(@Param("id") Long id);
}
