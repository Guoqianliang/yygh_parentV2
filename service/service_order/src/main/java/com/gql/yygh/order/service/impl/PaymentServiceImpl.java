package com.gql.yygh.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gql.yygh.enums.PaymentStatusEnum;
import com.gql.yygh.enums.PaymentTypeEnum;
import com.gql.yygh.model.order.OrderInfo;
import com.gql.yygh.model.order.PaymentInfo;
import com.gql.yygh.order.mapper.PaymentMapper;
import com.gql.yygh.order.service.PaymentService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Description:
 * @author Guoqianliang
 */
@Service
public class PaymentServiceImpl extends
        ServiceImpl<PaymentMapper, PaymentInfo> implements PaymentService {
    @Override
    public void savePaymentInfo(OrderInfo order, Integer status) {
        //根据订单id和支付类型，查询支付记录表是否存在相同订单
        QueryWrapper<PaymentInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", order.getId());
        wrapper.eq("payment_type", status);
        Integer count = baseMapper.selectCount(wrapper);
        if (count > 0) {
            return;
        }
        //添加记录
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCreateTime(new Date());
        paymentInfo.setOrderId(order.getId());
        paymentInfo.setPaymentType(status);
        paymentInfo.setOutTradeNo(order.getOutTradeNo());
        paymentInfo.setPaymentStatus(PaymentStatusEnum.UNPAID.getStatus());
        String subject = new DateTime(order.getReserveDate()).toString("yyyy-MM-dd") + "|" + order.getHosname() + "|" + order.getDepname() + "|" + order.getTitle();
        paymentInfo.setSubject(subject);
        paymentInfo.setTotalAmount(order.getAmount());
        baseMapper.insert(paymentInfo);
    }
    // 2.向支付记录表添加记录

}
