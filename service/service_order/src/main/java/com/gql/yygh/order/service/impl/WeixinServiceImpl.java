package com.gql.yygh.order.service.impl;

import com.github.wxpay.sdk.WXPayUtil;
import com.gql.yygh.enums.PaymentStatusEnum;
import com.gql.yygh.enums.PaymentTypeEnum;
import com.gql.yygh.model.order.OrderInfo;
import com.gql.yygh.order.service.OrderService;
import com.gql.yygh.order.service.PaymentService;
import com.gql.yygh.order.service.WeixinService;
import com.gql.yygh.order.utils.ConstantPropertiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @author Guoqianliang
 */
@Service
public class WeixinServiceImpl implements WeixinService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    /**
     * 下单 生成微信支付二维码
     */
    @Override
    public Map createNative(Long orderId) {
        // 1.根据orderId获取订单信息
        OrderInfo order = orderService.getById(orderId);
        // 2.向支付记录表添加记录
        paymentService.savePaymentInfo(order, PaymentTypeEnum.WEIXIN.getStatus());
        // 3.设置参数,调用微信生成二维码接口
        // 需要将参数转换为xml格式,使用商户key进行加密
        Map paramMap = new HashMap();
        paramMap.put("appid", ConstantPropertiesUtils.APPID);
        paramMap.put("mch_id", ConstantPropertiesUtils.PARTNER);
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        String body = order.getReserveDate() + "就诊"+ order.getDepname();
        paramMap.put("body", body);
        paramMap.put("out_trade_no", order.getOutTradeNo());
        //paramMap.put("total_fee", order.getAmount().multiply(new BigDecimal("100")).longValue()+"");
        // 当前设置为一分钱,为了测试使用
        paramMap.put("total_fee", "1");
        paramMap.put("spbill_create_ip", "127.0.0.1");
        paramMap.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify");
        paramMap.put("trade_type", "NATIVE");



        return null;
    }
}















