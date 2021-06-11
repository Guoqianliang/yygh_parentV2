package com.gql.yygh.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gql.yygh.model.order.OrderInfo;

/**
 * @Description:
 * @author Guoqianliang
 */
public interface OrderService extends IService<OrderInfo> {
    //保存订单
    Long saveOrder(String scheduleId, Long patientId);
    // 根据订单id查询订单详情
    OrderInfo getOrder(String orderId);
}
