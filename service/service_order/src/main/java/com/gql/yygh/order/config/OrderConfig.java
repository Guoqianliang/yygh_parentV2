package com.gql.yygh.order.config;

import com.gql.yygh.common.result.Result;
import com.gql.yygh.order.service.OrderService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @Description:
 * @author Guoqianliang
 */
@Configuration
@MapperScan("com.gql.yygh.order.mapper")
public class OrderConfig {
    @Autowired
    private OrderService orderService;

    // 生成挂号订单
    @PostMapping("auth/submitOrder/{scheduleId}/{patientId}")
    public Result saveOrders(@PathVariable String scheduleId,
                             @PathVariable Long patientId) {
        Long orderId = orderService.saveOrder(scheduleId,patientId);
        return Result.ok(orderId);
    }
}
