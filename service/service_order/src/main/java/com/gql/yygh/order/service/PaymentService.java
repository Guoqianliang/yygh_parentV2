package com.gql.yygh.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gql.yygh.enums.PaymentTypeEnum;
import com.gql.yygh.model.order.OrderInfo;
import com.gql.yygh.model.order.PaymentInfo;

/**
 * @Description:
 * @author Guoqianliang
 */
public interface PaymentService extends IService<PaymentInfo> {
    void savePaymentInfo(OrderInfo order, Integer status);
    // 2.向支付记录表添加记录
}
