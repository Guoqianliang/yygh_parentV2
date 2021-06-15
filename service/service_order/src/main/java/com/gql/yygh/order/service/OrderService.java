package com.gql.yygh.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gql.yygh.model.order.OrderInfo;
import com.gql.yygh.vo.order.OrderQueryVo;

/**
 * @Description:
 * @author Guoqianliang
 */
public interface OrderService extends IService<OrderInfo> {
    // 生成挂号订单
    Long saveOrder(String scheduleId, Long patientId);
    // 根据订单id查询订单详情
    OrderInfo getOrder(String orderId);
    // 订单列表接口(条件分页带查询)
    IPage<OrderInfo> selectPage(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo);
    // 取消预约
    Boolean cancelOrder(Long orderId);
}
