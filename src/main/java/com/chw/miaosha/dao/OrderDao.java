package com.chw.miaosha.dao;

import com.chw.miaosha.domain.MiaoShaOrder;
import com.chw.miaosha.domain.OrderInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

/**
 * @Author CHW
 * @Date 2022/9/24
 **/
public interface OrderDao {
    
    /**
     * 创建订单信息
     * @SelectKey 是用于在插入数据时得到插入数据的的主键
     * @param orderInfo 订单信息
     * @return
     */
    @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date)values("
            + "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
    @SelectKey(keyColumn="id", keyProperty="id", resultType=long.class, before=false, statement="select last_insert_id()")
    long insert(OrderInfo orderInfo);
    
    /**
     * 创建秒杀订单
     * @param miaoshaOrder
     * @return
     */
    @Insert("insert into miaosha_order (user_id, goods_id, order_id)values(#{userId}, #{goodsId}, #{orderId})")
    int insertMiaoshaOrder(MiaoShaOrder miaoshaOrder);
    
    /**
     * 得到订单信息
     * @param orderId
     * @return
     */
    @Select("select * from order_info where id = #{orderId}")
    OrderInfo getOrderById(long orderId);
}
